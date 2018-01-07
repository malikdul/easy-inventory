package com.example.easy.inventory.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.ProductDTO;
import com.example.easy.inventory.model.ProductEntity;

/**
 * Mapper for the entity Product and its DTO ProductDTO.
 */
@Mapper
public interface IProductMapper {

	IProductMapper INSTANCE = Mappers.getMapper(IProductMapper.class);
	
    ProductDTO productToProductDTO(ProductEntity product);

    List<ProductDTO> productsToProductDTOs(List<ProductEntity> products);
    
    List<ProductDTO> productsPagedToProductDTOs(Page<ProductEntity> products);

    ProductEntity productDTOToProduct(ProductDTO productDTO);

    List<ProductEntity> productDTOsToProducts(List<ProductDTO> productDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default ProductEntity productFromId(Integer id) {
        if (id == null) {
            return null;
        }
        ProductEntity product = new ProductEntity();
        product.setId(id);
        return product;
    }
    
    

}
