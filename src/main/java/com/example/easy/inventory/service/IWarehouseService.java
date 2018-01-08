package com.example.easy.inventory.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.commons.model.NameCountDTO;
import com.example.easy.inventory.dto.WarehouseDTO;

/**
 * Service Interface for managing Warehouse.
 */
public interface IWarehouseService {

    /**
     * Save a warehouse.
     *
     * @param warehouseDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
	WarehouseDTO save(WarehouseDTO warehouseDTO) throws CustomBadRequestException;

    /**
     *  Get all the warehouses.
     *  
     *  @return the list of entities
     */
    List<WarehouseDTO> findAll();

    /**
     *  Get the "id" warehouse.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    WarehouseDTO findOne(Integer id);

    /**
     *  Delete the "id" warehouse.
     *
     *  @param id the id of the entity
     */
    void delete(Integer id);

	Collection<WarehouseDTO> getAll(Integer accountId, Pageable pageable);
	
	Collection<NameCountDTO> getWarehouseFrequencyReport(Integer accountId);
}
