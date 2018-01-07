package com.example.easy.inventory.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.OrderDetailsDTO;
import com.example.easy.inventory.model.OrderDetailsEntity;

/**
 * Mapper for the entity OrderDetails and its DTO OrderDetailsDTO.
 */
@Mapper
public interface IOrderDetailsMapper {

	IOrderDetailsMapper INSTANCE = Mappers.getMapper(IOrderDetailsMapper.class);
	
    OrderDetailsDTO orderDetailsToOrderDetailsDTO(OrderDetailsEntity orderDetails);

    List<OrderDetailsDTO> orderDetailsToOrderDetailsDTOs(List<OrderDetailsEntity> orderDetails);
    
    List<OrderDetailsDTO> orderDetailsPagedToOrderDetailsDTOs(Page<OrderDetailsEntity> orderDetails);

    OrderDetailsEntity orderDetailsDTOToOrderDetails(OrderDetailsDTO orderDetailsDTO);

    List<OrderDetailsEntity> orderDetailsDTOsToOrderDetails(List<OrderDetailsDTO> orderDetailsDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default OrderDetailsEntity orderDetailsFromId(Integer id) {
        if (id == null) {
            return null;
        }
        OrderDetailsEntity orderDetails = new OrderDetailsEntity();
        orderDetails.setId(id);
        return orderDetails;
    }
    

}
