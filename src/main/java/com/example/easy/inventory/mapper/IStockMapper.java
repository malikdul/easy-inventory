package com.example.easy.inventory.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.StockDTO;
import com.example.easy.inventory.model.StockEntity;

/**
 * Mapper for the entity Stock and its DTO StockDTO.
 */
@Mapper
public interface IStockMapper {

	IStockMapper INSTANCE = Mappers.getMapper(IStockMapper.class);
	
    StockDTO stockToStockDTO(StockEntity stock);

    List<StockDTO> stocksToStockDTOs(List<StockEntity> stocks);
    
    List<StockDTO> stocksPagedToStockDTOs(Page<StockEntity> stocks);

    StockEntity stockDTOToStock(StockDTO stockDTO);

    List<StockEntity> StockDTOsToStocks(List<StockDTO> stockDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default StockEntity stockFromId(Integer id) {
        if (id == null) {
            return null;
        }
        StockEntity stock = new StockEntity();
        stock.setId(id);
        return stock;
    }
    
    

}
