package com.example.easy.inventory.service;

import java.util.List;

import com.example.easy.inventory.dto.CategoryDTO;

/**
 * Service Interface for managing Category.
 */
public interface ICategoryService {

    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save
     * @return the persisted entity
     */
    CategoryDTO save(CategoryDTO categoryDTO);

    /**
     *  Get all the categories.
     *  
     *  @return the list of entities
     */
    List<CategoryDTO> findAll();

    /**
     *  Get the "id" category.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    CategoryDTO findOne(Integer id);

    /**
     *  Delete the "id" category.
     *
     *  @param id the id of the entity
     */
    void delete(Integer id);
}
