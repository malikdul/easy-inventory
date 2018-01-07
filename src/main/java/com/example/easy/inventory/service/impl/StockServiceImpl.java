package com.example.easy.inventory.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.commons.helper.Utility;
import com.example.easy.inventory.dto.StockDTO;
import com.example.easy.inventory.mapper.IStockMapper;
import com.example.easy.inventory.model.StockEntity;
import com.example.easy.inventory.repository.StockRepository;
import com.example.easy.inventory.service.IProductService;
import com.example.easy.inventory.service.IStockService;

/**
 * Service Implementation for managing Stock.
 */
@Service
@Transactional
public class StockServiceImpl implements IStockService {

    private final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private IProductService productService;

    /**
     * Save a Stock.
     *
     * @param StockDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    @Override
    public StockDTO save(StockDTO stockDTO) throws CustomBadRequestException {
    	if (stockDTO.getId() == null)
		{
    		if(null != stockRepository.findByAccountIdAndProductId(stockDTO.getAccountId(), stockDTO.getProductId())) {
        		throw new CustomBadRequestException(stockDTO.getProductId() + " already exists.");
        	}
    		
    		stockDTO.setCreatedOn(Utility.getCurrentDate());

		} else
		{
			stockDTO.setModifiedOn(Utility.getCurrentDate());

			StockEntity savedEntity = stockRepository.findOne(stockDTO.getId());

			if (savedEntity != null)
			{
				if (stockDTO.getCreatedOn() == null)
				{
					stockDTO.setCreatedOn(savedEntity.getCreatedOn());
				}
			} else {
				throw new CustomBadRequestException("Stock with id: " + stockDTO.getId() + " doesn't exists.");
			}
		}
        log.debug("Request to save Stock : {}", stockDTO);
        StockEntity stock = IStockMapper.INSTANCE.stockDTOToStock(stockDTO);
        stock = stockRepository.saveAndFlush(stock);
        StockDTO result = IStockMapper.INSTANCE.stockToStockDTO(stock);
        return result;
    }

    /**
     *  Get one Stock by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public StockDTO findOne(Integer id) {
        log.debug("Request to get Stock : {}", id);
        StockEntity stock = stockRepository.findOne(id);
        StockDTO stockDTO = IStockMapper.INSTANCE.stockToStockDTO(stock);
        return stockDTO;
    }

    /**
     *  Delete the  Stock by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Integer id) {
    	throw new NotImplementedException("delete not supported yet ...");
        //log.debug("Request to delete Stock : {}", id);
        //stockRepository.deleteStock(id);
    }

    /**
     *  Get all the Stocks.
     *  
     *  @return the list of entities
     */
	@Override
	@Transactional(readOnly = true)
	public Collection<StockDTO> getAll(Integer accountId, Pageable pageable) {
		 log.debug("Request to get all Stocks");

	        return IStockMapper.INSTANCE.stocksPagedToStockDTOs(
	        		stockRepository.findAllByAccountId(accountId, pageable)).stream().map(detail -> {
					detail.setProduct(productService.findOne(detail.getProductId()));
					return detail;
				})
				.collect(Collectors.toCollection(LinkedList::new));
	}

	@Override
	public StockDTO findByAccountIdAndProductId(Integer accountId, Integer productId) {
		log.debug("Request to get Stock by accountId, productId: {}, {}", accountId, productId);
        StockEntity stock = stockRepository.findByAccountIdAndProductId(accountId, productId);
        StockDTO stockDTO = IStockMapper.INSTANCE.stockToStockDTO(stock);
		if (null != stockDTO) {
			stockDTO.setProduct(productService.findOne(stock.getProductId()));
		}
        return stockDTO;
	}
}
