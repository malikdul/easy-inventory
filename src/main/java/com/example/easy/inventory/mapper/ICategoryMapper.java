package com.example.easy.inventory.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.easy.inventory.dto.CategoryDTO;
import com.example.easy.inventory.model.CategoryEntity;

/**
 * Mapper for the entity Category and its DTO CategoryDTO.
 */
@Mapper
public interface ICategoryMapper {

	ICategoryMapper INSTANCE = Mappers.getMapper(ICategoryMapper.class);
	
    CategoryDTO categoryToCategoryDTO(CategoryEntity category);

    List<CategoryDTO> categoriesToCategoryDTOs(List<CategoryEntity> categories);

    CategoryEntity categoryDTOToCategory(CategoryDTO categoryDTO);

    List<CategoryEntity> categoryDTOsToCategories(List<CategoryDTO> categoryDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default CategoryEntity categoryFromId(Integer id) {
        if (id == null) {
            return null;
        }
        CategoryEntity category = new CategoryEntity();
        category.setId(id);
        return category;
    }
    

}
