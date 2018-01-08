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

import com.example.easy.commons.exceptions.CustomBadRequestException;
import com.example.easy.commons.helper.Utility;
import com.example.easy.commons.model.NameCountDTO;
import com.example.easy.inventory.dto.AddressDTO;
import com.example.easy.inventory.dto.TelecomDTO;
import com.example.easy.inventory.dto.WarehouseDTO;
import com.example.easy.inventory.mapper.IWarehouseMapper;
import com.example.easy.inventory.model.WarehouseEntity;
import com.example.easy.inventory.repository.WarehouseRepository;
import com.example.easy.inventory.service.IAddressService;
import com.example.easy.inventory.service.ITelecomService;
import com.example.easy.inventory.service.IWarehouseService;

/**
 * Service Implementation for managing Warehouse.
 */
@Service
@Transactional
public class WarehouseServiceImpl implements IWarehouseService{

	private final Logger log = LoggerFactory.getLogger(WarehouseServiceImpl.class);
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private ITelecomService telecomService;
    @Autowired
    private IAddressService addressService;

    
    /**
     * Save a warehouse.
     *
     * @param warehouseDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    @Override
    public WarehouseDTO save(WarehouseDTO warehouseDTO) throws CustomBadRequestException {
    	if (warehouseDTO.getId() == null)
		{
    		WarehouseEntity warehouse = warehouseRepository.findByName(warehouseDTO.getName());
    		if(null != warehouse) {
        		throw new CustomBadRequestException("Warehouse with this name already exists. \n " + warehouse);
        	}
    		
    		warehouseDTO.setCreatedOn(Utility.getCurrentDate());

		} else
		{
			warehouseDTO.setModifiedOn(Utility.getCurrentDate());

			WarehouseEntity savedEntity = warehouseRepository.findOne(warehouseDTO.getId());

			if (savedEntity != null)
			{
				if (warehouseDTO.getCreatedOn() == null)
				{
					warehouseDTO.setCreatedOn(savedEntity.getCreatedOn());
				}
			}
		}
        log.debug("Request to save Warehouse : {}", warehouseDTO);
        WarehouseEntity warehouse = warehouseRepository.save(IWarehouseMapper.INSTANCE.warehouseDTOToWarehouse(warehouseDTO));
        
        List<TelecomDTO> telecoms = warehouseDTO.getTelecoms()
		.stream().map(telecom -> {
        	telecom.setUniqueParentId(warehouse.getIdString());
        	telecom.setAccountId(warehouse.getAccountId());
        	try {
				return telecomService.save(telecom);
			} catch (CustomBadRequestException e) {
				throw new RuntimeException(e);
			}
        })
		.collect(Collectors.toCollection(LinkedList::new));
        List<AddressDTO> addresses = null;
        
        if(null != warehouseDTO.getAddresses()) {
			addresses = warehouseDTO.getAddresses().stream().map(address -> {
				address.setUniqueParentId(warehouse.getIdString());
				address.setAccountId(warehouse.getAccountId());
				try {
					return addressService.save(address);
				} catch (CustomBadRequestException e) {
					throw new RuntimeException(e);
				}
			}).collect(Collectors.toCollection(LinkedList::new));
		}
        WarehouseDTO result = IWarehouseMapper.INSTANCE.warehouseToWarehouseDTO(warehouse);
        result.setAddresses(addresses);
        result.setTelecoms(telecoms);
        
        return result;
    }

    /**
     *  Get all the warehouses.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<WarehouseDTO> findAll() {
        log.debug("Request to get all Warehouses");
        List<WarehouseDTO> result = warehouseRepository.findAll().stream()
            .map(IWarehouseMapper.INSTANCE::warehouseToWarehouseDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one warehouse by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WarehouseDTO findOne(Integer id) {
        log.debug("Request to get Warehouse : {}", id);
        WarehouseEntity warehouseEntity = warehouseRepository.findOne(id);
        if(null == warehouseEntity) {
        	return null;
        }
        WarehouseDTO warehouse = IWarehouseMapper.INSTANCE.warehouseToWarehouseDTO(warehouseEntity);
        
        List<TelecomDTO> telecoms = telecomService.getAll(warehouse.getAccountId(), warehouse.getIdString(), null);
		
        List<AddressDTO> addresses = addressService.getAll(warehouse.getAccountId(), warehouse.getIdString(), null);
        
        warehouse.setTelecoms(telecoms);
        warehouse.setAddresses(addresses);
        warehouse.setFrequency(warehouseRepository.getWarehouseFrequencyById(warehouse.getAccountId(), warehouse.getId()));
        return warehouse;
    }

    /**
     *  Delete the  Warehouse by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Integer id) {
        log.debug("Request to delete Warehouse : {}", id);
        warehouseRepository.deleteWarehouse(id);
    }
    
    /**
     *  Get all the products.
     *  
     *  @return the list of entities
     */
	@Override
	@Transactional(readOnly = true)
	public Collection<WarehouseDTO> getAll(Integer accountId, Pageable pageable) {
		 log.debug("Request to get all Products");

        return IWarehouseMapper.INSTANCE.warehousesPagedToWarehouseDTOs(
        		warehouseRepository.findAllByAccountId(accountId, pageable)).stream()
        		.map(warehouse -> {
        			List<TelecomDTO> telecoms = telecomService.getAll(accountId, warehouse.getIdString(), pageable);
					
			        List<AddressDTO> addresses = addressService.getAll(accountId, warehouse.getIdString(), pageable);
			        
			        warehouse.setTelecoms(telecoms);
			        warehouse.setAddresses(addresses);
			        warehouse.setFrequency(warehouseRepository.getWarehouseFrequencyById(warehouse.getAccountId(), warehouse.getId()));
			        return warehouse;
        		})
        		.collect(Collectors.toCollection(LinkedList::new));
	}

	@Override
	public Collection<NameCountDTO> getWarehouseFrequencyReport(Integer accountId) {
		return warehouseRepository.getWarehouseFrequencyReport(accountId);
	}
}
