package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.easy.commons.model.IBaseDTO;
import com.example.easy.inventory.model.SaleEntity;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Sale entity.
 */
@SuppressWarnings("unused")
public interface SaleRepository extends JpaRepository<SaleEntity,Integer> {

	@Modifying
	@Query("update SaleEntity u set u.deleted = 1 where u.id = :id")
	void deleteSale(@Param("id") Integer id);
	
	@Query("SELECT u FROM SaleEntity u WHERE u.accountId = :accountId AND u.deleted = 0 ")
	Page<SaleEntity> findAllByAccountId(@Param("accountId") Integer accountId, Pageable pageable);

	@Query("SELECT u FROM SaleEntity u WHERE u.accountId = :accountId AND u.bookingId = :bookingId AND u.deleted = 0 ")
	SaleEntity findByBookingId(@Param("accountId") Integer accountId, @Param("bookingId") Integer bookingId);

}
