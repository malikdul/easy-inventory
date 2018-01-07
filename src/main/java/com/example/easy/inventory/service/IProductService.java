package com.example.easy.inventory.service;

import java.util.Collection;

import org.springframework.data.domain.Pageable;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.inventory.dto.ProductDTO;

/**
 * Service Interface for managing Product.
 */
public interface IProductService {

    /**
     * Save a product.
     *
     * @param productDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    ProductDTO save(ProductDTO productDTO) throws CustomBadRequestException;

    /**
     *  Get the "id" product.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ProductDTO findOne(Integer id);

    /**
     *  Delete the "id" product.
     *
     *  @param id the id of the entity
     * @throws CustomBadRequestException 
     */
    void delete(Integer accountId, Integer id) throws CustomBadRequestException;
    
    
    Collection<ProductDTO> getAll(Integer accountId, Pageable pageable);

}
