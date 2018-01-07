package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.easy.commons.model.IBaseDTO;
import com.example.easy.inventory.model.OrderEntity;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Order entity.
 */
@SuppressWarnings("unused")
public interface OrderRepository extends JpaRepository<OrderEntity,Integer> {

	@Modifying
	@Query("update OrderEntity u set u.deleted = 1 where u.id = :id")
	void deleteOrder(@Param("id") Integer id);
	
	@Query("SELECT u FROM OrderEntity u WHERE u.accountId = :accountId AND u.deleted = 0 ")
	Page<OrderEntity> findAllByAccountId(@Param("accountId") Integer accountId, Pageable pageable);

	@Query("SELECT u FROM OrderEntity u WHERE u.accountId = :accountId AND u.supplierId = :supplierId AND u.deleted = 0 ")
	Page<OrderEntity> findAllByAccountIdAndSupplierId(@Param("accountId") Integer accountId, @Param("supplierId") Integer supplierId, Pageable pageable);
	
}
