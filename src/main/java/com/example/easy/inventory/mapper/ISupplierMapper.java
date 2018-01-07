package com.example.easy.inventory.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.SupplierDTO;
import com.example.easy.inventory.model.*;

import java.util.List;

/**
 * Mapper for the entity Supplier and its DTO SupplierDTO.
 */
@Mapper
public interface ISupplierMapper {

	ISupplierMapper INSTANCE = Mappers.getMapper(ISupplierMapper.class);
	
    SupplierDTO supplierToSupplierDTO(SupplierEntity supplier);

    List<SupplierDTO> suppliersToSupplierDTOs(List<SupplierEntity> suppliers);
    
    List<SupplierDTO> suppliersPagedToSupplierDTOs(Page<SupplierEntity> suppliers);

    SupplierEntity supplierDTOToSupplier(SupplierDTO supplierDTO);

    List<SupplierEntity> supplierDTOsToSuppliers(List<SupplierDTO> supplierDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default SupplierEntity supplierFromId(Integer id) {
        if (id == null) {
            return null;
        }
        SupplierEntity supplier = new SupplierEntity();
        supplier.setId(id);
        return supplier;
    }
    

}
