package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.easy.commons.model.IBaseDTO;
import com.example.easy.commons.model.NameCountDTO;
import com.example.easy.inventory.model.SupplierEntity;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Supplier entity.
 */
@SuppressWarnings("unused")
public interface SupplierRepository extends JpaRepository<SupplierEntity,Integer> {

	SupplierEntity findByName(@Param("name") String name);
	
	@Modifying
	@Query("update SupplierEntity u set u.deleted = 1 where u.id = :id")
	void deleteSupplier(@Param("id") Integer id);
	
	
	@Query("SELECT u FROM SupplierEntity u WHERE u.accountId = :accountId AND u.deleted = 0 ")
	Page<SupplierEntity> findAllByAccountId(@Param("accountId") Integer accountId, Pageable pageable);

	@Query(name = "getSupplierFrequencyReport", nativeQuery = true)
	Collection<NameCountDTO> getSupplierFrequencyReport(@Param("accountId") Integer accountId);
	
	@Query(name = "getSupplierFrequencyById", nativeQuery = true)
	Integer getSupplierFrequencyById(@Param("accountId") Integer accountId, @Param("supplierId") Integer supplierId);
	
}
