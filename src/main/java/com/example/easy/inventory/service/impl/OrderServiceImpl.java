package com.example.easy.inventory.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.commons.helper.Utility;
import com.example.easy.inventory.dto.OrderDTO;
import com.example.easy.inventory.dto.OrderDetailsDTO;
import com.example.easy.inventory.dto.StockDTO;
import com.example.easy.inventory.mapper.IOrderDetailsMapper;
import com.example.easy.inventory.mapper.IOrderMapper;
import com.example.easy.inventory.model.OrderDetailsEntity;
import com.example.easy.inventory.model.OrderEntity;
import com.example.easy.inventory.model.OrderStatus;
import com.example.easy.inventory.model.StockEntity;
import com.example.easy.inventory.repository.OrderDetailsRepository;
import com.example.easy.inventory.repository.OrderRepository;
import com.example.easy.inventory.repository.StockRepository;
import com.example.easy.inventory.service.IOrderService;
import com.example.easy.inventory.service.IProductService;
import com.example.easy.inventory.service.IStockService;
import com.example.easy.inventory.service.ISupplierService;

/**
 * Service Implementation for managing Order.
 */
@Service
@Transactional
public class OrderServiceImpl implements IOrderService{

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private IStockService stockService;
    
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    
    @Autowired
    private IProductService productService;
    
    @Autowired
    private ISupplierService supplierService;


    /**
     * Save a order.
     *
     * @param orderDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    @Override
    @Transactional
    public OrderDTO save(OrderDTO orderDTO) throws CustomBadRequestException {
    	if (orderDTO.getId() == null)
		{
    		orderDTO.setCreatedOn(Utility.getCurrentDate());

		} else
		{
			orderDTO.setModifiedOn(Utility.getCurrentDate());

			OrderEntity savedEntity = orderRepository.findOne(orderDTO.getId());

			if (savedEntity != null)
			{
	        	if(OrderStatus.RECEIVED.equals(savedEntity.getStatus())) {
        	        throw new CustomBadRequestException("can't update, order is already received.");
	        	}
	        	
				if (orderDTO.getCreatedOn() == null)
				{
					orderDTO.setCreatedOn(savedEntity.getCreatedOn());
				}
			} else {
				throw new CustomBadRequestException("Order with id: " + orderDTO.getId() + " doesn't exists.");
			}
		}
    	
        log.debug("Request to save Order : {}", orderDTO);
        final OrderEntity order = orderRepository.save(IOrderMapper.INSTANCE.orderDTOToOrder(orderDTO));
        
        List<OrderDetailsDTO> details = IOrderDetailsMapper.INSTANCE.orderDetailsDTOsToOrderDetails(orderDTO.getDetails())
		.stream().map(detail -> {
			if (detail.getId() == null)
			{
	        	detail.setOrderId(order.getId());
	        	detail.setAccountId(order.getAccountId());
	        	detail.setCreatedBy(order.getCreatedBy());
	        	detail.setCreatedOn(order.getCreatedOn());
			} else {
				OrderDetailsEntity savedDetail = orderDetailsRepository.findOne(detail.getId());
				if (savedDetail != null)
				{
					if (detail.getCreatedOn() == null)
					{
						detail.setCreatedOn(savedDetail.getCreatedOn());
						detail.setCreatedBy(savedDetail.getCreatedBy());
					}
					
					detail.setOrderId(savedDetail.getOrderId());
				}
				detail.setModifiedBy(order.getModifiedBy());
				detail.setModifiedOn(order.getModifiedOn());
			}
			
        	final OrderDetailsDTO dtresult = IOrderDetailsMapper.INSTANCE.orderDetailsToOrderDetailsDTO(orderDetailsRepository.save(detail));
        	
        	//update stock updateStock
        	if(OrderStatus.RECEIVED.equals(order.getStatus())) {
        		StockDTO stock = stockService.findByAccountIdAndProductId(detail.getAccountId(), detail.getProductId());
        		if(null == stock) {
        			stock = new StockDTO();
        			stock.setAccountId(order.getAccountId());
        			stock.setCreatedBy(order.getCreatedBy());
        			stock.setCreatedOn(order.getCreatedOn());
        			stock.setTotalQty(0);
        			stock.setAvailableQty(0);
        			stock.setProductId(detail.getProductId());
        		} else {
        			stock.setModifiedBy(order.getModifiedBy());
        			stock.setModifiedOn(order.getModifiedOn());
        		}
        		stock.setUnitPrice(detail.getPurchasePrice());
        		stock.setTotalQty(stock.getTotalQty() + detail.getQuantity());
        		
        		if(0 == stock.getAvailableQty()) { // first time total is available, no reserve :)
        			stock.setAvailableQty(stock.getTotalQty());
        		} else {
        			stock.setAvailableQty(stock.getAvailableQty() + detail.getQuantity());
        		}
        		
            	try {
					stockService.save(stock);
				} catch (CustomBadRequestException e) {
					log.error("STOCK NOT SAVED ...", e);
				}
            }
        	
        	return dtresult;
        })
		.collect(Collectors.toCollection(LinkedList::new));
        
        OrderDTO result = IOrderMapper.INSTANCE.orderToOrderDTO(order);
        result.setDetails(details);
        return result;
    }

    /**
     *  Get all the orders.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        log.debug("Request to get all Orders");
        List<OrderDTO> result = orderRepository.findAll().stream()
        .map(order -> {
        	final OrderDTO dto = IOrderMapper.INSTANCE.orderToOrderDTO(order);
        	dto.setDetails(IOrderDetailsMapper.INSTANCE.orderDetailsPagedToOrderDetailsDTOs(
        			orderDetailsRepository.findAllByAccountIdAndOrderId(dto.getAccountId(), dto.getId(), null)
			));
        	return dto;
        })
        .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one order by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDTO findOne(Integer id) {
        log.debug("Request to get Order : {}", id);
        OrderEntity order = orderRepository.findOne(id);
        final OrderDTO orderDTO = IOrderMapper.INSTANCE.orderToOrderDTO(order);
        orderDTO.setDetails(
				IOrderDetailsMapper.INSTANCE.orderDetailsPagedToOrderDetailsDTOs(orderDetailsRepository
						.findAllByAccountIdAndOrderId(orderDTO.getAccountId(), orderDTO.getId(), null))
				.stream().map(detail -> {
					detail.setProduct(productService.findOne(detail.getProductId()));
					return detail;
				})
				.collect(Collectors.toCollection(LinkedList::new))
		);
		orderDTO.setSupplier(supplierService.findOne(orderDTO.getSupplierId()));
        return orderDTO;
    }

    /**
     *  Delete the  order by id.
     *
     *  @param id the id of the entity
     * @throws CustomBadRequestException 
     */
    @Override
    @Transactional
    public void delete(Integer id) throws CustomBadRequestException {
        log.debug("Request to delete Order : {}", id);
        OrderEntity order = orderRepository.findOne(id);
        if(OrderStatus.RECEIVED.equals(order.getStatus())) {
	        throw new CustomBadRequestException("can't delete, order is already received.");
        }
        orderRepository.deleteOrder(id);
    }
    
    
    /**
     *  Get all the products.
     *  
     *  @return the list of entities
     */
	@Override
	@Transactional(readOnly = true)
	public Collection<OrderDTO> getAll(Integer accountId, Pageable pageable) {
		log.debug("Request to get all Products");

		return IOrderMapper.INSTANCE.ordersPagedToOrderDTOs(orderRepository.findAllByAccountId(accountId, pageable))
				.stream().map(dto -> {
					final List<OrderDetailsDTO> details = IOrderDetailsMapper.INSTANCE.orderDetailsPagedToOrderDetailsDTOs(orderDetailsRepository
							.findAllByAccountIdAndOrderId(dto.getAccountId(), dto.getId(), null)).stream().map(detail -> {
						log.debug("loading Product with id {} for detail id {}", detail.getProductId(), detail.getId());
						detail.setProduct(productService.findOne(detail.getProductId()));
						return detail;
					}).collect(Collectors.toCollection(LinkedList::new));
					dto.setDetails(details);
					dto.setSupplier(supplierService.findOne(dto.getSupplierId()));
					return dto;
				}).collect(Collectors.toCollection(LinkedList::new));
	}

	@Override
	public Collection<OrderDTO> getAllBySupplier(Integer accountId, Integer supplierId, Pageable pageable) {
		return IOrderMapper.INSTANCE.ordersPagedToOrderDTOs(orderRepository.findAllByAccountIdAndSupplierId(accountId, supplierId, pageable))
				.stream().map(dto -> {
					final List<OrderDetailsDTO> details = IOrderDetailsMapper.INSTANCE.orderDetailsPagedToOrderDetailsDTOs(orderDetailsRepository
							.findAllByAccountIdAndOrderId(dto.getAccountId(), dto.getId(), null)).stream().map(detail -> {
						log.debug("loading Product with id {} for detail id {}", detail.getProductId(), detail.getId());
						detail.setProduct(productService.findOne(detail.getProductId()));
						return detail;
					}).collect(Collectors.toCollection(LinkedList::new));
					dto.setDetails(details);
					dto.setSupplier(supplierService.findOne(dto.getSupplierId()));
					return dto;
				}).collect(Collectors.toCollection(LinkedList::new));
	}
	
	
}
