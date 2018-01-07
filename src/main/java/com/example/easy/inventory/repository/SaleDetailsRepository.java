package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.easy.commons.model.IBaseDTO;
import com.example.easy.inventory.model.SaleDetailsEntity;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the SaleDetails entity.
 */
@SuppressWarnings("unused")
public interface SaleDetailsRepository extends JpaRepository<SaleDetailsEntity,Integer> {

	@Modifying
	@Query("update SaleDetailsEntity u set u.deleted = 1 where u.id = :id")
	void deleteSaleDetails(@Param("id") Integer id);
	
	@Query("SELECT u FROM SaleDetailsEntity u WHERE u.accountId = :accountId AND u.saleId = :saleId AND u.deleted = 0 ")
	Page<SaleDetailsEntity> findAllByAccountIdAndSaleId(@Param("accountId") Integer accountId, @Param("saleId") Integer saleId, Pageable pageable);
	
	@Query("SELECT u FROM SaleDetailsEntity u WHERE u.accountId = :accountId AND u.saleId = :saleId AND u.productId = :productId AND u.deleted = 0 ")
	SaleDetailsEntity findByAccountIdAndSaleIdAndProductId(@Param("accountId") Integer accountId, @Param("saleId") Integer saleId, @Param("productId") Integer productId);
	
}
