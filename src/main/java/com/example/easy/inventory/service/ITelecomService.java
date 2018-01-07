package com.example.easy.inventory.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.inventory.dto.TelecomDTO;

/**
 * Service Interface for managing Telecom.
 */
public interface ITelecomService {

    /**
     * Save a telecom.
     *
     * @param telecomDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    TelecomDTO save(TelecomDTO telecomDTO) throws CustomBadRequestException;

    /**
     *  Get all the telecoms.
     *  
     *  @return the list of entities
     */
    List<TelecomDTO> findAll();

    /**
     *  Get the "id" telecom.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TelecomDTO findOne(Integer id);

    /**
     *  Delete the "id" telecom.
     *
     *  @param id the id of the entity
     */
    void delete(Integer id);
    
    /**
     *  Get all the telecoms.
     *  
     *  @return the list of entities
     */
    List<TelecomDTO> getAll(Integer accountId, String uniqueParentId, Pageable pageable);

}
