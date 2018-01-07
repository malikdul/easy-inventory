package com.example.easy.inventory.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.OrderDTO;
import com.example.easy.inventory.model.*;

import java.util.List;

/**
 * Mapper for the entity Order and its DTO OrderDTO.
 */
@Mapper
public interface IOrderMapper {

	IOrderMapper INSTANCE = Mappers.getMapper(IOrderMapper.class);
	
    OrderDTO orderToOrderDTO(OrderEntity order);

    List<OrderDTO> ordersToOrderDTOs(List<OrderEntity> orders);
    
    List<OrderDTO> ordersPagedToOrderDTOs(Page<OrderEntity> orders);

    OrderEntity orderDTOToOrder(OrderDTO orderDTO);

    List<OrderEntity> orderDTOsToOrders(List<OrderDTO> orderDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default OrderEntity orderFromId(Integer id) {
        if (id == null) {
            return null;
        }
        OrderEntity order = new OrderEntity();
        order.setId(id);
        return order;
    }
    

}
