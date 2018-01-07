package com.example.easy.inventory.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.commons.helper.Utility;
import com.example.easy.commons.model.NameCountDTO;
import com.example.easy.inventory.dto.AddressDTO;
import com.example.easy.inventory.dto.SupplierDTO;
import com.example.easy.inventory.dto.TelecomDTO;
import com.example.easy.inventory.mapper.IAddressMapper;
import com.example.easy.inventory.mapper.ISupplierMapper;
import com.example.easy.inventory.mapper.ITelecomMapper;
import com.example.easy.inventory.model.SupplierEntity;
import com.example.easy.inventory.repository.AddressRepository;
import com.example.easy.inventory.repository.SupplierRepository;
import com.example.easy.inventory.repository.TelecomRepository;
import com.example.easy.inventory.service.IAddressService;
import com.example.easy.inventory.service.ISupplierService;
import com.example.easy.inventory.service.ITelecomService;

/**
 * Service Implementation for managing Supplier.
 */
@Service
@Transactional
public class SupplierServiceImpl implements ISupplierService{

	private final Logger log = LoggerFactory.getLogger(SupplierServiceImpl.class);
    
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ITelecomService telecomService;
    @Autowired
    private IAddressService addressService;

    
    /**
     * Save a supplier.
     *
     * @param supplierDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    @Override
    public SupplierDTO save(SupplierDTO supplierDTO) throws CustomBadRequestException {
    	if (supplierDTO.getId() == null)
		{
    		SupplierEntity supplier = supplierRepository.findByName(supplierDTO.getName());
    		if(null != supplier) {
        		throw new CustomBadRequestException("supplier with this name already exists. \n " + supplier);
        	}
    		
    		supplierDTO.setCreatedOn(Utility.getCurrentDate());

		} else
		{
			supplierDTO.setModifiedOn(Utility.getCurrentDate());

			SupplierEntity savedEntity = supplierRepository.findOne(supplierDTO.getId());

			if (savedEntity != null)
			{
				if (supplierDTO.getCreatedOn() == null)
				{
					supplierDTO.setCreatedOn(savedEntity.getCreatedOn());
				}
			}
		}
        log.debug("Request to save Supplier : {}", supplierDTO);
        SupplierEntity supplier = supplierRepository.save(ISupplierMapper.INSTANCE.supplierDTOToSupplier(supplierDTO));
        
        List<TelecomDTO> telecoms = supplierDTO.getTelecoms()
		.stream().map(telecom -> {
        	telecom.setUniqueParentId(supplier.getIdString());
        	telecom.setAccountId(supplier.getAccountId());
        	try {
				return telecomService.save(telecom);
			} catch (CustomBadRequestException e) {
				throw new RuntimeException(e);
			}
        })
		.collect(Collectors.toCollection(LinkedList::new));
        List<AddressDTO> addresses = null;
        
        if(null != supplierDTO.getAddresses()) {
			addresses = supplierDTO.getAddresses().stream().map(address -> {
				address.setUniqueParentId(supplier.getIdString());
				address.setAccountId(supplier.getAccountId());
				try {
					return addressService.save(address);
				} catch (CustomBadRequestException e) {
					throw new RuntimeException(e);
				}
			}).collect(Collectors.toCollection(LinkedList::new));
		}
        SupplierDTO result = ISupplierMapper.INSTANCE.supplierToSupplierDTO(supplier);
        result.setAddresses(addresses);
        result.setTelecoms(telecoms);
        
        return result;
    }

    /**
     *  Get all the suppliers.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<SupplierDTO> findAll() {
        log.debug("Request to get all Suppliers");
        List<SupplierDTO> result = supplierRepository.findAll().stream()
            .map(ISupplierMapper.INSTANCE::supplierToSupplierDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one supplier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public SupplierDTO findOne(Integer id) {
        log.debug("Request to get Supplier : {}", id);
        SupplierEntity supplierEntity = supplierRepository.findOne(id);
        if(null == supplierEntity) {
        	return null;
        }
        SupplierDTO supplier = ISupplierMapper.INSTANCE.supplierToSupplierDTO(supplierEntity);
        
        List<TelecomDTO> telecoms = telecomService.getAll(supplier.getAccountId(), supplier.getIdString(), null);
		
        List<AddressDTO> addresses = addressService.getAll(supplier.getAccountId(), supplier.getIdString(), null);
        
        supplier.setTelecoms(telecoms);
        supplier.setAddresses(addresses);
        supplier.setFrequency(supplierRepository.getSupplierFrequencyById(supplier.getAccountId(), supplier.getId()));
        return supplier;
    }

    /**
     *  Delete the  supplier by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Integer id) {
        log.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteSupplier(id);
    }
    
    /**
     *  Get all the products.
     *  
     *  @return the list of entities
     */
	@Override
	@Transactional(readOnly = true)
	public Collection<SupplierDTO> getAll(Integer accountId, Pageable pageable) {
		 log.debug("Request to get all Products");

        return ISupplierMapper.INSTANCE.suppliersPagedToSupplierDTOs(
        		supplierRepository.findAllByAccountId(accountId, pageable)).stream()
        		.map(supplier -> {
        			List<TelecomDTO> telecoms = telecomService.getAll(accountId, supplier.getIdString(), pageable);
					
			        List<AddressDTO> addresses = addressService.getAll(accountId, supplier.getIdString(), pageable);
			        
			        supplier.setTelecoms(telecoms);
			        supplier.setAddresses(addresses);
			        supplier.setFrequency(supplierRepository.getSupplierFrequencyById(supplier.getAccountId(), supplier.getId()));
			        return supplier;
        		})
        		.collect(Collectors.toCollection(LinkedList::new));
	}

	@Override
	public Collection<NameCountDTO> getSupplierFrequencyReport(Integer accountId) {
		return supplierRepository.getSupplierFrequencyReport(accountId);
	}
}
