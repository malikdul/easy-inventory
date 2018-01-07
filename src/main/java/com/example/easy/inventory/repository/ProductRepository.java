package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.easy.commons.model.IBaseDTO;
import com.example.easy.inventory.model.ProductEntity;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Product entity.
 */
@SuppressWarnings("unused")
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

	@Query("SELECT u FROM ProductEntity u WHERE u.accountId = :accountId AND u.deleted = 0 ")
	Page<ProductEntity> findAllByAccountId(@Param("accountId") Integer accountId, Pageable pageable);
	
	@Modifying
	@Query("update ProductEntity u set u.deleted = 1 where u.id = :id")
	void deleteProduct(@Param("id") Integer id);
	
	@Query("SELECT u FROM ProductEntity u WHERE u.accountId = :accountId AND u.name = :name AND u.deleted = 0 ")
	ProductEntity findByAccountIdAndName(@Param("accountId") Integer accountId, @Param("name") String name);
	

}
