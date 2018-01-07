package com.example.easy.inventory.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.easy.inventory.dto.LookupDTO;

/**
 * Service Interface for managing Category.
 */
public interface ILookupService {

    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save
     * @return the persisted entity
     */
    LookupDTO save(LookupDTO categoryDTO);

    /**
     *  Get all the categories.
     *  
     *  @return the list of entities
     */
    List<LookupDTO> getAll(Integer accountId, Pageable pageable);

    /**
     *  Get the "id" category.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    LookupDTO findOne(Integer id);

    /**
     *  Delete the "id" category.
     *
     *  @param id the id of the entity
     */
    void delete(Integer id);
}
