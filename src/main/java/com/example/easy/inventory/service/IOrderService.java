package com.example.easy.inventory.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.inventory.dto.OrderDTO;

/**
 * Service Interface for managing Order.
 */
public interface IOrderService {

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    OrderDTO save(OrderDTO orderDTO) throws CustomBadRequestException;

    /**
     *  Get all the orders.
     *  
     *  @return the list of entities
     */
    List<OrderDTO> findAll();

    /**
     *  Get the "id" order.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    OrderDTO findOne(Integer id);

    /**
     *  Delete the "id" order.
     *
     *  @param id the id of the entity
     * @throws CustomBadRequestException 
     */
    void delete(Integer id) throws CustomBadRequestException;

	Collection<OrderDTO> getAll(Integer accountId, Pageable pageable);

	Collection<OrderDTO> getAllBySupplier(Integer accountId, Integer supplierId, Pageable pageable);
}
