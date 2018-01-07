package com.example.easy.inventory.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.inventory.dto.AddressDTO;

/**
 * Service Interface for managing Address.
 */
public interface IAddressService {

    /**
     * Save a address.
     *
     * @param addressDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    AddressDTO save(AddressDTO addressDTO) throws CustomBadRequestException;

    /**
     *  Get all the addresses.
     *  
     *  @return the list of entities
     */
    List<AddressDTO> findAll();

    /**
     *  Get the "id" address.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AddressDTO findOne(Integer id);

    /**
     *  Delete the "id" address.
     *
     *  @param id the id of the entity
     */
    void delete(Integer id);

    /**
     *  Get all the addresses.
     *  
     *  @return the list of entities
     */
	List<AddressDTO> getAll(Integer accountId, String  uniqueParentId, Pageable pageable);
}
