package com.example.easy.inventory.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.LookupDTO;
import com.example.easy.inventory.model.LookupEntity;

/**
 * Mapper for the entity Lookup and its DTO LookupDTO.
 */
@Mapper
public interface ILookupMapper {

	ILookupMapper INSTANCE = Mappers.getMapper(ILookupMapper.class);
	
	LookupDTO lookupToLookupDTO(LookupEntity lookup);

    List<LookupDTO> lookupToLookupDTOs(List<LookupEntity> lookup);

    LookupEntity lookupDTOToLookup(LookupDTO lookupDTO);

    List<LookupEntity> lookupDTOsToLookup(List<LookupDTO> lookupDTOs);
    
    List<LookupDTO> lookupToLookupDTOs(Page<LookupEntity> lookup);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default LookupEntity lookupFromId(Integer id) {
        if (id == null) {
            return null;
        }
        LookupEntity lookup = new LookupEntity();
        lookup.setId(id);
        return lookup;
    }
    

}
