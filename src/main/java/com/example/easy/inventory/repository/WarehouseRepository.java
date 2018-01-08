package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.easy.commons.model.IBaseDTO;
import com.example.easy.commons.model.NameCountDTO;
import com.example.easy.inventory.model.WarehouseEntity;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Warehouse entity.
 */
@SuppressWarnings("unused")
public interface WarehouseRepository extends JpaRepository<WarehouseEntity,Integer> {

	WarehouseEntity findByName(@Param("name") String name);
	
	@Modifying
	@Query("update WarehouseEntity u set u.deleted = 1 where u.id = :id")
	void deleteWarehouse(@Param("id") Integer id);
	
	
	@Query("SELECT u FROM WarehouseEntity u WHERE u.accountId = :accountId AND u.deleted = 0 ")
	Page<WarehouseEntity> findAllByAccountId(@Param("accountId") Integer accountId, Pageable pageable);

	@Query(name = "getWarehouseFrequencyReport", nativeQuery = true)
	Collection<NameCountDTO> getWarehouseFrequencyReport(@Param("accountId") Integer accountId);
	
	@Query(name = "getWarehouseFrequencyById", nativeQuery = true)
	Integer getWarehouseFrequencyById(@Param("accountId") Integer accountId, @Param("warehouseId") Integer warehouseId);
	
}
