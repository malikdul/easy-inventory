package com.example.easy.inventory.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.inventory.dto.SaleDTO;

/**
 * Service Interface for managing Sale.
 */
public interface ISaleService {

    /**
     * Save a sale.
     *
     * @param saleDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    SaleDTO save(SaleDTO saleDTO) throws CustomBadRequestException;

    /**
     *  Get all the sales.
     *  
     *  @return the list of entities
     */
    List<SaleDTO> findAll();

    /**
     *  Get the "id" sale.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SaleDTO findOne(Integer id);

    /**
     *  Delete the "id" sale.
     *
     *  @param id the id of the entity
     */
    void delete(Integer id);

	List<SaleDTO> getAll(Integer accountId, Pageable pageable);

	SaleDTO findByBookingId(Integer accountId, Integer bookingId);
	
	//void releaseReservedStockBySale(Integer saleId);
	
	void consumeStockBySale(Integer id);
}
