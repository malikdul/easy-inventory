package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.easy.inventory.model.StockEntity;

/**
 * Spring Data JPA repository for the Product entity.
 */
//@SuppressWarnings("unused")
public interface StockRepository extends JpaRepository<StockEntity, Integer> {

	@Query("SELECT u FROM StockEntity u WHERE u.accountId = :accountId AND u.deleted = 0 ")
	Page<StockEntity> findAllByAccountId(@Param("accountId") Integer accountId, Pageable pageable);
	
	@Modifying
	@Query("update StockEntity u set u.deleted = 1 where u.id = :id")
	void deleteStock(@Param("id") Integer id);
	
	@Query("SELECT u FROM StockEntity u WHERE u.accountId = :accountId AND u.productId = :productId AND u.deleted = 0 ")
	StockEntity findByAccountIdAndProductId(@Param("accountId") Integer accountId, @Param("productId") Integer productId);
	
	@Query("SELECT COUNT(u) > 0 FROM StockEntity u WHERE u.accountId = :accountId AND u.productId = :productId AND u.deleted = 0 ")
	boolean existsByAccountIdAndProductId(@Param("accountId") Integer accountId, @Param("productId") Integer productId);
	

}
