package com.example.easy.inventory.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.commons.model.NameCountDTO;
import com.example.easy.inventory.dto.SupplierDTO;

/**
 * Service Interface for managing Supplier.
 */
public interface ISupplierService {

    /**
     * Save a supplier.
     *
     * @param supplierDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    SupplierDTO save(SupplierDTO supplierDTO) throws CustomBadRequestException;

    /**
     *  Get all the suppliers.
     *  
     *  @return the list of entities
     */
    List<SupplierDTO> findAll();

    /**
     *  Get the "id" supplier.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SupplierDTO findOne(Integer id);

    /**
     *  Delete the "id" supplier.
     *
     *  @param id the id of the entity
     */
    void delete(Integer id);

	Collection<SupplierDTO> getAll(Integer accountId, Pageable pageable);
	
	Collection<NameCountDTO> getSupplierFrequencyReport(Integer accountId);
}
