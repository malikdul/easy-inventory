package com.example.easy.inventory.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.SaleDetailsDTO;
import com.example.easy.inventory.model.SaleDetailsEntity;

/**
 * Mapper for the entity SaleDetails and its DTO SaleDetailsDTO.
 */
@Mapper
public interface ISaleDetailsMapper {

	ISaleDetailsMapper INSTANCE = Mappers.getMapper(ISaleDetailsMapper.class);
	
	SaleDetailsDTO saleDetailsToSaleDetailsDTO(SaleDetailsEntity saleDetails);

    List<SaleDetailsDTO> saleDetailsToSaleDetailsDTOs(List<SaleDetailsEntity> saleDetails);
    
    List<SaleDetailsDTO> saleDetailsPagedToSaleDetailsDTOs(Page<SaleDetailsEntity> saleDetails);

    SaleDetailsEntity saleDetailsDTOToSaleDetails(SaleDetailsDTO saleDetailsDTO);

    List<SaleDetailsEntity> saleDetailsDTOsToSaleDetails(List<SaleDetailsDTO> saleDetailsDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default SaleDetailsEntity saleDetailsFromId(Integer id) {
        if (id == null) {
            return null;
        }
        SaleDetailsEntity saleDetails = new SaleDetailsEntity();
        saleDetails.setId(id);
        return saleDetails;
    }
    

}
