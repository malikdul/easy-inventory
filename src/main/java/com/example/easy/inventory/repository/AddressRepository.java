package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.easy.commons.model.IBaseDTO;
import com.example.easy.inventory.model.AddressEntity;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Address entity.
 */
@SuppressWarnings("unused")
public interface AddressRepository extends JpaRepository<AddressEntity,Integer> {

	@Modifying
	@Query("update AddressEntity u set u.deleted = 1 where u.id = :id")
	void deleteAddress(@Param("id") Integer id);
	
	@Query("SELECT u FROM AddressEntity u WHERE u.accountId = :accountId AND u.uniqueParentId = :uniqueParentId AND  u.deleted = 0 ")
	Page<AddressEntity> findAllByAccountIdAndParentId(@Param("accountId") Integer accountId, @Param("uniqueParentId") String uniqueParentId, Pageable pageable);

	@Query("SELECT u FROM AddressEntity u WHERE u.accountId = :accountId AND u.uniqueParentId = :uniqueParentId AND u.line1 = :line1 AND u.deleted = 0 ")
	AddressEntity findAllByAccountIdAndParentIdAndLine1(@Param("accountId") Integer accountId, @Param("uniqueParentId") String uniqueParentId, @Param("line1") String line1);
	
}
