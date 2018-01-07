package com.example.easy.inventory.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.easy.commons.model.IBaseDTO;
import com.example.easy.inventory.model.TelecomEntity;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data JPA repository for the Telecom entity.
 */
@SuppressWarnings("unused")
public interface TelecomRepository extends JpaRepository<TelecomEntity,Integer> {

	@Modifying
	@Query("update TelecomEntity u set u.deleted = 1 where u.id = :id")
	void deleteTelecom(@Param("id") Integer id);
	
	@Query("SELECT u FROM TelecomEntity u WHERE u.accountId = :accountId AND u.uniqueParentId = :uniqueParentId AND u.deleted = 0 ")
	Page<TelecomEntity> findAllByAccountIdAndParentId(@Param("accountId") Integer accountId, @Param("uniqueParentId") String uniqueParentId, Pageable pageable);
	
	@Query("SELECT u FROM TelecomEntity u WHERE u.accountId = :accountId AND u.uniqueParentId = :uniqueParentId AND u.telecomString = :telecomString AND u.deleted = 0 ")
	TelecomEntity findAllByAccountIdAndParentIdAndTelecomString(@Param("accountId") Integer accountId, @Param("uniqueParentId") String uniqueParentId, @Param("telecomString") String telecomString);
	 
}
