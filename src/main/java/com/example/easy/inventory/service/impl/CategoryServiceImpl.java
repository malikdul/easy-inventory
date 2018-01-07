package com.example.easy.inventory.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.easy.inventory.dto.CategoryDTO;
import com.example.easy.inventory.mapper.ICategoryMapper;
import com.example.easy.inventory.model.CategoryEntity;
import com.example.easy.inventory.repository.CategoryRepository;
import com.example.easy.inventory.service.ICategoryService;

/**
 * Service Implementation for managing Category.
 */
@Service
@Transactional
public class CategoryServiceImpl implements ICategoryService{

    private final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
    
    @Autowired
    private CategoryRepository categoryRepository;


    /**
     * Save a category.
     *
     * @param categoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        log.debug("Request to save Category : {}", categoryDTO);
        CategoryEntity category = ICategoryMapper.INSTANCE.categoryDTOToCategory(categoryDTO);
        category = categoryRepository.save(category);
        CategoryDTO result = ICategoryMapper.INSTANCE.categoryToCategoryDTO(category);
        return result;
    }

    /**
     *  Get all the categories.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        log.debug("Request to get all Categories");
        List<CategoryDTO> result = categoryRepository.findAll().stream()
            .map(ICategoryMapper.INSTANCE::categoryToCategoryDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one category by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryDTO findOne(Integer id) {
        log.debug("Request to get Category : {}", id);
        CategoryEntity category = categoryRepository.findOne(id);
        CategoryDTO categoryDTO = ICategoryMapper.INSTANCE.categoryToCategoryDTO(category);
        return categoryDTO;
    }

    /**
     *  Delete the  category by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Integer id) {
        log.debug("Request to delete Category : {}", id);
        categoryRepository.delete(id);
    }
}
