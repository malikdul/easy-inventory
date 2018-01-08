package com.example.easy.inventory.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.WarehouseDTO;
import com.example.easy.inventory.model.*;

import java.util.List;

/**
 * Mapper for the entity Warehouse and its DTO WarehouseDTO.
 */
@Mapper
public interface IWarehouseMapper {

	IWarehouseMapper INSTANCE = Mappers.getMapper(IWarehouseMapper.class);
	
	WarehouseDTO warehouseToWarehouseDTO(WarehouseEntity warehouse);

    List<WarehouseDTO> warehousesToWarehouseDTOs(List<WarehouseEntity> warehouses);
    
    List<WarehouseDTO> warehousesPagedToWarehouseDTOs(Page<WarehouseEntity> warehouses);

    WarehouseEntity warehouseDTOToWarehouse(WarehouseDTO warehouseDTO);

    List<WarehouseEntity> warehouseDTOsToWarehouses(List<WarehouseDTO> warehouseDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default WarehouseEntity warehouseFromId(Integer id) {
        if (id == null) {
            return null;
        }
        WarehouseEntity e = new WarehouseEntity();
        e.setId(id);
        return e;
    }
    

}
