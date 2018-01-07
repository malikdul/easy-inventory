package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.easy.commons.model.IBaseDTO;
import com.example.easy.inventory.model.OrderDetailsEntity;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the OrderDetails entity.
 */
@SuppressWarnings("unused")
public interface OrderDetailsRepository extends JpaRepository<OrderDetailsEntity,Integer> {

	@Modifying
	@Query("update OrderDetailsEntity u set u.deleted = 1 where u.id = :id")
	void deleteOrderDetails(@Param("id") Integer id);
	
	@Query("SELECT u FROM OrderDetailsEntity u WHERE u.accountId = :accountId AND u.orderId = :orderId AND u.deleted = 0 ")
	Page<OrderDetailsEntity> findAllByAccountIdAndOrderId(@Param("accountId") Integer accountId, @Param("orderId") Integer orderId, Pageable pageable);
	
}
