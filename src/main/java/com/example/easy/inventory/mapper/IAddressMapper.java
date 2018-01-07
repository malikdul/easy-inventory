package com.example.easy.inventory.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.easy.inventory.dto.AddressDTO;
import com.example.easy.inventory.model.*;

import java.util.List;

/**
 * Mapper for the entity Address and its DTO AddressDTO.
 */
@Mapper
public interface IAddressMapper {

	IAddressMapper INSTANCE = Mappers.getMapper(IAddressMapper.class);
	
    AddressDTO addressToAddressDTO(AddressEntity address);

    List<AddressDTO> addressesToAddressDTOs(List<AddressEntity> addresses);
    
    List<AddressDTO> addressesPagedToAddressDTOs(Page<AddressEntity> addresses);

    AddressEntity addressDTOToAddress(AddressDTO addressDTO);

    List<AddressEntity> addressDTOsToAddresses(List<AddressDTO> addressDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default AddressEntity addressFromId(Integer id) {
        if (id == null) {
            return null;
        }
        AddressEntity address = new AddressEntity();
        address.setId(id);
        return address;
    }
    

}
