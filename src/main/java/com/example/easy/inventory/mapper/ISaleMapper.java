package com.example.easy.inventory.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.OrderDTO;
import com.example.easy.inventory.dto.SaleDTO;
import com.example.easy.inventory.model.*;

import java.util.List;

/**
 * Mapper for the entity Sale and its DTO SaleDTO.
 */
@Mapper
public interface ISaleMapper {

	ISaleMapper INSTANCE = Mappers.getMapper(ISaleMapper.class);
	
    SaleDTO saleToSaleDTO(SaleEntity sale);

    List<SaleDTO> salesToSaleDTOs(List<SaleEntity> sales);

    SaleEntity saleDTOToSale(SaleDTO saleDTO);
    
    List<SaleDTO> salesPagedToSaleDTOs(Page<SaleEntity> orders);

    List<SaleEntity> saleDTOsToSales(List<SaleDTO> saleDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default SaleEntity saleFromId(Integer id) {
        if (id == null) {
            return null;
        }
        SaleEntity sale = new SaleEntity();
        sale.setId(id);
        return sale;
    }
    

}
