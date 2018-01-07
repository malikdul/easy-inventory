package com.example.easy.inventory.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.TelecomDTO;
import com.example.easy.inventory.model.*;

import java.util.List;

/**
 * Mapper for the entity Telecom and its DTO TelecomDTO.
 */
@Mapper
public interface ITelecomMapper {

	ITelecomMapper INSTANCE = Mappers.getMapper(ITelecomMapper.class);
	
    TelecomDTO telecomToTelecomDTO(TelecomEntity telecom);

    List<TelecomDTO> telecomsToTelecomDTOs(List<TelecomEntity> telecoms);
    
    List<TelecomDTO> telecomsPagedToTelecomDTOs(Page<TelecomEntity> telecoms);

    TelecomEntity telecomDTOToTelecom(TelecomDTO telecomDTO);

    List<TelecomEntity> telecomDTOsToTelecoms(List<TelecomDTO> telecomDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default TelecomEntity telecomFromId(Integer id) {
        if (id == null) {
            return null;
        }
        TelecomEntity telecom = new TelecomEntity();
        telecom.setId(id);
        return telecom;
    }
    

}
