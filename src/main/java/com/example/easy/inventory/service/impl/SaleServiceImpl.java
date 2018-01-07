package com.example.easy.inventory.service.impl;

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
import com.example.easy.inventory.dto.OrderDetailsDTO;
import com.example.easy.inventory.dto.ProductDTO;
import com.example.easy.inventory.dto.SaleDTO;
import com.example.easy.inventory.dto.SaleDetailsDTO;
import com.example.easy.inventory.dto.StockDTO;
import com.example.easy.inventory.mapper.IOrderDetailsMapper;
import com.example.easy.inventory.mapper.ISaleDetailsMapper;
import com.example.easy.inventory.mapper.ISaleMapper;
import com.example.easy.inventory.model.SaleDetailsEntity;
import com.example.easy.inventory.model.SaleEntity;
import com.example.easy.inventory.repository.SaleDetailsRepository;
import com.example.easy.inventory.repository.SaleRepository;
import com.example.easy.inventory.service.IProductService;
import com.example.easy.inventory.service.ISaleService;
import com.example.easy.inventory.service.IStockService;

/**
 * Service Implementation for managing Sale.
 */
@Service
@Transactional
public class SaleServiceImpl implements ISaleService {

	private final Logger log = LoggerFactory.getLogger(SaleServiceImpl.class);

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private SaleDetailsRepository saleDetailsRepository;

	@Autowired
	private IStockService stockService;
	
	@Autowired
    private IProductService productService;

	/**
	 * Save a sale.
	 *
	 * @param saleDTO
	 *            the entity to save
	 * @return the persisted entity
	 * @throws CustomBadRequestException
	 */
	@Override
	@Transactional
	public SaleDTO save(SaleDTO saleDTO) throws CustomBadRequestException {
		
		if(null == saleDTO.getBookingId()) {
			throw new CustomBadRequestException("no booking information present.");
		}
		
		final SaleEntity saved = saleRepository.findByBookingId(saleDTO.getAccountId(), saleDTO.getBookingId());
		
		if (null == saved && saleDTO.getId() == null) {
			saleDTO.setCreatedOn(Utility.getCurrentDate());
		} else {
			if(!saved.getBookingId().equals(saleDTO.getBookingId())) {
				throw new CustomBadRequestException("booking information doesn't match.");
			}
			
			if (null != saved && saleDTO.getId() == null) {
				//saleDTO.setId(saved.getId());
				throw new CustomBadRequestException("Duplicate add for this booking. Update information instead.");
			}
			
			saleDTO.setModifiedOn(Utility.getCurrentDate());
			
			SaleEntity savedEntity = saleRepository.findOne(saleDTO.getId());

			if (savedEntity != null) {
				if (saleDTO.getCreatedOn() == null) {
					saleDTO.setCreatedOn(savedEntity.getCreatedOn());
				}
				
				//release old stock information and also release old state
				releaseReservedStockBySale(saved.getId()); 
				
			} else {
				throw new CustomBadRequestException("Sale with id: " + saleDTO.getId() + " doesn't exists.");
			}
		}

		log.debug("Request to save Sale : {}", saleDTO);
		
		SaleEntity sale = saleRepository.save(ISaleMapper.INSTANCE.saleDTOToSale(saleDTO));
		
		List<SaleDetailsDTO> details = ISaleDetailsMapper.INSTANCE.saleDetailsDTOsToSaleDetails(saleDTO.getDetails())
			.stream().map(detail -> {
				detail.setSaleId(sale.getId());
				detail.setAccountId(sale.getAccountId());
				detail.setCreatedBy(sale.getCreatedBy());
				detail.setCreatedOn(sale.getCreatedOn());
				detail.setModifiedBy(sale.getModifiedBy());
				detail.setModifiedOn(sale.getModifiedOn());
				final SaleDetailsDTO dtresult = ISaleDetailsMapper.INSTANCE
						.saleDetailsToSaleDetailsDTO(saleDetailsRepository.save(detail));

				if(sale.getIsDraft()) {
					return dtresult;
				}
				// update stock updateStock
				try {
					StockDTO stock = stockService.findByAccountIdAndProductId(detail.getAccountId(),
							detail.getProductId());
					
//					if (null != saved && saleDTO.getId() != null) {
//						//more of a hack, to avoid internal/child transaction not being commit by Spring :(
//						final SaleDetailsEntity savedDetail = saleDetailsRepository.findByAccountIdAndSaleIdAndProductId(detail.getAccountId(), detail.getSaleId(), detail.getProductId());
//						stock.setAvailableQty(stock.getAvailableQty() + savedDetail.getQuantity());
//						stock.setConsumedQty(stock.getConsumedQty() - savedDetail.getQuantity());
//					}
					
					if (null == stock) {
						throw new CustomBadRequestException("Stock doesn't exists.");
					} else {
						stock.setModifiedBy(sale.getModifiedBy());
						stock.setModifiedOn(sale.getModifiedOn());
					}
					ProductDTO product = productService.findOne(detail.getProductId());
					int available = stock.getAvailableQty() - detail.getQuantity();

					if(available <= 0 || (available < product.getReorderPoint())) {
						StringBuilder message = new StringBuilder();
						message.append("Reorder Limit:: ");
						message.append(product.getName());
						message.append(" (id: ");
						message.append(product.getId());
						message.append(") with available quantity ");
						message.append(stock.getAvailableQty() );
						message.append(" and reorder point ");
						message.append(product.getReorderPoint());
						message.append(". The requested quantity is ");
						message.append(detail.getQuantity());
						message.append(" so ");
						message.append(stock.getAvailableQty());
						message.append(" - ");
						message.append(detail.getQuantity());
						message.append(" is ");
						message.append(stock.getAvailableQty() - detail.getQuantity());
						message.append(". The avaiable stock shouldn't reach reorder point or zero or negative. Consider reorder first.");
						
						throw new CustomBadRequestException( message.toString() );
					}
					stock.setAvailableQty(available);
					int consumedQty = stock.getConsumedQty() == null ? 0 : stock.getConsumedQty() + detail.getQuantity();
					stock.setConsumedQty(consumedQty);
					stockService.save(stock);
				} catch (CustomBadRequestException e) {
					log.error("Bad request, " + e.getMessage(), e);
					throw new RuntimeException("Bad request, " + e.getMessage(), e);
				}
				
				return dtresult;
			}).collect(Collectors.toCollection(LinkedList::new));

		SaleDTO result = ISaleMapper.INSTANCE.saleToSaleDTO(sale);
		result.setDetails(details);
		return result;
	}

	/**
	 * Get all the sales.
	 * 
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SaleDTO> findAll() {
		log.debug("Request to get all Sales");
		List<SaleDTO> result = saleRepository.findAll().stream().map(ISaleMapper.INSTANCE::saleToSaleDTO)
				.collect(Collectors.toCollection(LinkedList::new));

		return result;
	}

	/**
	 * Get one sale by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public SaleDTO findOne(Integer id) {
		log.debug("Request to get Sale by id : {}", id);
		SaleEntity sale = saleRepository.findOne(id);
		SaleDTO dto = ISaleMapper.INSTANCE.saleToSaleDTO(sale);
		final List<SaleDetailsDTO> details = ISaleDetailsMapper.INSTANCE.saleDetailsPagedToSaleDetailsDTOs(saleDetailsRepository
				.findAllByAccountIdAndSaleId(dto.getAccountId(), dto.getId(), null)).stream().map(detail -> {
			log.debug("loading Product with id {} for detail id {}", detail.getProductId(), detail.getId());
			detail.setProduct(productService.findOne(detail.getProductId()));
			return detail;
		}).collect(Collectors.toCollection(LinkedList::new));
		dto.setDetails(details);
		return dto;
	}

	/**
	 * Delete the sale by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	@Override
	@Transactional
	public void delete(Integer id) {
		log.debug("Request to delete Sale : {}", id);
		releaseReservedStockBySale(id);
		saleRepository.deleteSale(id);
	}

	@Transactional
	private void releaseReservedStockBySale(Integer saleId) {
		SaleEntity sale = saleRepository.findOne(saleId);
		if(!sale.getIsDraft() && !sale.getDeleted()) {
			ISaleDetailsMapper.INSTANCE.saleDetailsPagedToSaleDetailsDTOs(saleDetailsRepository
					.findAllByAccountIdAndSaleId(sale.getAccountId(), sale.getId(), null)).stream().forEach(detail -> {
						StockDTO stock = stockService.findByAccountIdAndProductId(detail.getAccountId(),
								detail.getProductId());
						stock.setAvailableQty(stock.getAvailableQty() + detail.getQuantity());
						stock.setConsumedQty(stock.getConsumedQty() - detail.getQuantity());
						try {
							stockService.save(stock);
							saleDetailsRepository.deleteSaleDetails(detail.getId());
						} catch (CustomBadRequestException e) {
							log.error("Bad request, " + e.getMessage(), e);
							throw new RuntimeException("Bad request, " + e.getMessage(), e);
						}
			});
		}
	}
	
	@Override
	@Transactional
	public void consumeStockBySale(Integer saleId) {
		SaleEntity sale = saleRepository.findOne(saleId);
		if(!sale.getIsDraft() && !sale.getDeleted()) {
			ISaleDetailsMapper.INSTANCE.saleDetailsPagedToSaleDetailsDTOs(saleDetailsRepository
					.findAllByAccountIdAndSaleId(sale.getAccountId(), sale.getId(), null)).stream().forEach(detail -> {
						StockDTO stock = stockService.findByAccountIdAndProductId(detail.getAccountId(),
								detail.getProductId());
						stock.setTotalQty(stock.getTotalQty() - detail.getQuantity());
						if(null != stock.getConsumedQty() && stock.getConsumedQty() > 0) {
							stock.setConsumedQty(stock.getConsumedQty() + detail.getQuantity());
						} else {
							stock.setConsumedQty(detail.getQuantity());
						}
						try {
							stockService.save(stock);
						} catch (CustomBadRequestException e) {
							log.error("Bad request, " + e.getMessage(), e);
							throw new RuntimeException("Bad request, " + e.getMessage(), e);
						}
						//saleDetailsRepository.deleteSaleDetails(detail.getId());
			});
		}
	}
	
	/**
	 * Get all the sales for account.
	 * 
	 * @return the list of SaleDTO
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SaleDTO> getAll(Integer accountId, Pageable pageable) {
		log.debug("Request to get all Sales");
		List<SaleDTO> result = ISaleMapper.INSTANCE.salesPagedToSaleDTOs(saleRepository.findAllByAccountId(accountId, pageable))
				.stream().map(dto -> {
					final List<SaleDetailsDTO> details = ISaleDetailsMapper.INSTANCE.saleDetailsPagedToSaleDetailsDTOs(saleDetailsRepository
							.findAllByAccountIdAndSaleId(dto.getAccountId(), dto.getId(), null)).stream().map(detail -> {
						log.debug("loading Product with id {} for detail id {}", detail.getProductId(), detail.getId());
						detail.setProduct(productService.findOne(detail.getProductId()));
						return detail;
					}).collect(Collectors.toCollection(LinkedList::new));
					dto.setDetails(details);
					return dto;
				}).collect(Collectors.toCollection(LinkedList::new));

		return result;
	}
	
	
	/**
	 * Get one sale by bookingId.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public SaleDTO findByBookingId(Integer accountId, Integer bookingId) {
		log.debug("Request to get Sale by bookingId : {}", bookingId);
		SaleEntity sale = saleRepository.findByBookingId(accountId, bookingId);
		SaleDTO dto = ISaleMapper.INSTANCE.saleToSaleDTO(sale);
		if(null != dto) {
			final List<SaleDetailsDTO> details = ISaleDetailsMapper.INSTANCE.saleDetailsPagedToSaleDetailsDTOs(saleDetailsRepository
					.findAllByAccountIdAndSaleId(dto.getAccountId(), dto.getId(), null)).stream().map(detail -> {
				log.debug("loading Product with id {} for detail id {}", detail.getProductId(), detail.getId());
				detail.setProduct(productService.findOne(detail.getProductId()));
				return detail;
			}).collect(Collectors.toCollection(LinkedList::new));
			dto.setDetails(details);
		}
		return dto;
	}
}
