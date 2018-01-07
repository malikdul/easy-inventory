package com.example.easy.inventory.service;

import java.util.Collection;

import org.springframework.data.domain.Pageable;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.inventory.dto.StockDTO;

/**
 * Service Interface for managing Stock.
 */
public interface IStockService {

    /**
     * Save a Stock.
     *
     * @param StockDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
	StockDTO save(StockDTO stockDTO) throws CustomBadRequestException;

    /**
     *  Get the "id" Stock.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
	StockDTO findOne(Integer id);
	
    /**
     *  Delete the "id" Stock.
     *
     *  @param id the id of the entity
     */
    void delete(Integer id);
    
    
    Collection<StockDTO> getAll(Integer accountId, Pageable pageable);
    
    StockDTO findByAccountIdAndProductId(Integer accountId, Integer productId);

}
