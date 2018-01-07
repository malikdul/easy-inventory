package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.easy.commons.model.IBaseDTO;
import com.example.easy.inventory.model.CategoryEntity;
import com.example.easy.inventory.model.LookupEntity;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Category entity.
 */
@SuppressWarnings("unused")
public interface LookupRepository extends JpaRepository<LookupEntity,Integer> {

	@Modifying
	@Query("update LookupEntity u set u.deleted = 1 where u.id = :id")
	void deleteLookup(@Param("id") Integer id);
	
	@Query("SELECT u FROM LookupEntity u WHERE u.accountId = :accountId AND u.deleted = 0 ")
	Page<LookupEntity> findAllByAccountId(@Param("accountId") Integer accountId, Pageable pageable);
	
}
