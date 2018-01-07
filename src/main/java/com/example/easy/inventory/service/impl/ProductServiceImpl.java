package com.example.easy.inventory.service.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.commons.helper.Utility;
import com.example.easy.inventory.dto.ProductDTO;
import com.example.easy.inventory.mapper.IProductMapper;
import com.example.easy.inventory.model.ProductEntity;
import com.example.easy.inventory.model.StockEntity;
import com.example.easy.inventory.repository.ProductRepository;
import com.example.easy.inventory.repository.StockRepository;
import com.example.easy.inventory.service.IProductService;

/**
 * Service Implementation for managing Product.
 */
@Service
@Transactional
public class ProductServiceImpl implements IProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;
    
    /**
     * Save a product.
     *
     * @param productDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    @Override
    public ProductDTO save(ProductDTO productDTO) throws CustomBadRequestException {
    	if (productDTO.getId() == null)
		{
    		if(null != productRepository.findByAccountIdAndName(productDTO.getAccountId(), productDTO.getName())) {
        		throw new CustomBadRequestException(productDTO.getName() + " already exists.");
        	}
    		
    		productDTO.setCreatedOn(Utility.getCurrentDate());

		} else
		{
			productDTO.setModifiedOn(Utility.getCurrentDate());

			ProductEntity savedEntity = productRepository.findOne(productDTO.getId());

			if (savedEntity != null)
			{
				if (productDTO.getCreatedOn() == null)
				{
					productDTO.setCreatedOn(savedEntity.getCreatedOn());
				}
			} else {
				throw new CustomBadRequestException("Product with id: " + productDTO.getId() + " doesn't exists.");
			}
		}
        log.debug("Request to save Product : {}", productDTO);
        ProductEntity product = IProductMapper.INSTANCE.productDTOToProduct(productDTO);
        product = productRepository.save(product);
        ProductDTO result = IProductMapper.INSTANCE.productToProductDTO(product);
        return result;
    }

    /**
     *  Get one product by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProductDTO findOne(Integer id) {
        log.debug("Request to get Product : {}", id);
        ProductEntity product = productRepository.findOne(id);
        ProductDTO productDTO = IProductMapper.INSTANCE.productToProductDTO(product);
        return productDTO;
    }

    /**
     *  Delete the  product by id.
     *
     *  @param id the id of the entity
     * @throws CustomBadRequestException 
     */
    @Override
    public void delete(Integer accountId, Integer id) throws CustomBadRequestException {
        log.debug("Request to delete Product : {}", id);
        final boolean stock = stockRepository.existsByAccountIdAndProductId(accountId, id);
        if(stock) {
        	throw new CustomBadRequestException("this product is being used in stock, first remove from stock.");
        }
        productRepository.deleteProduct(id);
    }

    /**
     *  Get all the products.
     *  
     *  @return the list of entities
     */
	@Override
	@Transactional(readOnly = true)
	public Collection<ProductDTO> getAll(Integer accountId, Pageable pageable) {
		 log.debug("Request to get all Products");

	        return IProductMapper.INSTANCE.productsPagedToProductDTOs(
	        		productRepository.findAllByAccountId(accountId, pageable));
	}
}
