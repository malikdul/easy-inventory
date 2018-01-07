package com.example.easy.inventory.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.commons.helper.Utility;
import com.example.easy.inventory.dto.AddressDTO;
import com.example.easy.inventory.mapper.IAddressMapper;
import com.example.easy.inventory.model.AddressEntity;
import com.example.easy.inventory.model.TelecomEntity;
import com.example.easy.inventory.repository.AddressRepository;
import com.example.easy.inventory.service.IAddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Address.
 */
@Service
@Transactional
public class AddressServiceImpl implements IAddressService{

    private final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);
    
    @Autowired
    private AddressRepository addressRepository;


    /**
     * Save a address.
     *
     * @param addressDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AddressDTO save(AddressDTO addressDTO)  throws CustomBadRequestException {
    	if(StringUtils.isEmpty(addressDTO.getLine1())) {
    		throw new CustomBadRequestException("address line1 missing.");
    	} 
    	
    	if (addressDTO.getId() == null)
		{
    		AddressEntity old = addressRepository.findAllByAccountIdAndParentIdAndLine1(addressDTO.getAccountId(), 
    				addressDTO.getUniqueParentId(), addressDTO.getLine1());
    		if(null != old) {
    			throw new CustomBadRequestException("address already exists: " + old);
    		}
    		
    		addressDTO.setCreatedOn(Utility.getCurrentDate());

		} else
		{
			addressDTO.setModifiedOn(Utility.getCurrentDate());

			AddressEntity savedEntity = addressRepository.findOne(addressDTO.getId());

			if (savedEntity != null)
			{
				if (addressDTO.getCreatedOn() == null)
				{
					addressDTO.setCreatedOn(savedEntity.getCreatedOn());
				}
			}
		}
    	
        log.debug("Request to save Address : {}", addressDTO);
        AddressEntity address = IAddressMapper.INSTANCE.addressDTOToAddress(addressDTO);
        address = addressRepository.save(address);
        AddressDTO result = IAddressMapper.INSTANCE.addressToAddressDTO(address);
        return result;
    }

    /**
     *  Get all the addresses.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<AddressDTO> findAll() {
        log.debug("Request to get all Addresses");
        List<AddressDTO> result = addressRepository.findAll().stream()
            .map(IAddressMapper.INSTANCE::addressToAddressDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one address by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public AddressDTO findOne(Integer id) {
        log.debug("Request to get Address : {}", id);
        AddressEntity address = addressRepository.findOne(id);
        AddressDTO addressDTO = IAddressMapper.INSTANCE.addressToAddressDTO(address);
        return addressDTO;
    }

    /**
     *  Delete the  address by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Integer id) {
        log.debug("Request to delete Address : {}", id);
        addressRepository.delete(id);
    }

	@Override
	public List<AddressDTO> getAll(Integer accountId, String uniqueParentId, Pageable pageable) {
		return IAddressMapper.INSTANCE.addressesPagedToAddressDTOs(addressRepository.findAllByAccountIdAndParentId(accountId, uniqueParentId, pageable));
	}
}
