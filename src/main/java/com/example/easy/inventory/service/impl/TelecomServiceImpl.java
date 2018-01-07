package com.example.easy.inventory.service.impl;

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
import com.example.easy.inventory.dto.TelecomDTO;
import com.example.easy.inventory.mapper.ITelecomMapper;
import com.example.easy.inventory.model.TelecomEntity;
import com.example.easy.inventory.repository.TelecomRepository;
import com.example.easy.inventory.service.ITelecomService;

/**
 * Service Implementation for managing Telecom.
 */
@Service
@Transactional
public class TelecomServiceImpl implements ITelecomService{

    private final Logger log = LoggerFactory.getLogger(TelecomServiceImpl.class);
    
    @Autowired
    private TelecomRepository telecomRepository;


    /**
     * Save a telecom.
     *
     * @param telecomDTO the entity to save
     * @return the persisted entity
     * @throws CustomBadRequestException 
     */
    @Override
    public TelecomDTO save(TelecomDTO telecomDTO) throws CustomBadRequestException {
    	if(StringUtils.isEmpty(telecomDTO.getTelecomString())) {
    		throw new CustomBadRequestException("telecom string is missing.");
    	} 
    	
    	if (telecomDTO.getId() == null)
		{
    		TelecomEntity old = telecomRepository.findAllByAccountIdAndParentIdAndTelecomString(telecomDTO.getAccountId(), 
    				telecomDTO.getUniqueParentId(), telecomDTO.getTelecomString());
    		if(null != old) {
    			throw new CustomBadRequestException("telecom string already exists: \n" + old);
    		}
    		
    		telecomDTO.setCreatedOn(Utility.getCurrentDate());

		} else
		{
			telecomDTO.setModifiedOn(Utility.getCurrentDate());

			TelecomEntity savedEntity = telecomRepository.findOne(telecomDTO.getId());

			if (savedEntity != null)
			{
				if (telecomDTO.getCreatedOn() == null)
				{
					telecomDTO.setCreatedOn(savedEntity.getCreatedOn());
				}
			}
		}
    	
        log.debug("Request to save Telecom : {}", telecomDTO);
        TelecomEntity telecom = ITelecomMapper.INSTANCE.telecomDTOToTelecom(telecomDTO);
        telecom = telecomRepository.save(telecom);
        TelecomDTO result = ITelecomMapper.INSTANCE.telecomToTelecomDTO(telecom);
        return result;
    }

    /**
     *  Get all the telecoms.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TelecomDTO> findAll() {
        log.debug("Request to get all Telecoms");
        List<TelecomDTO> result = telecomRepository.findAll().stream()
            .map(ITelecomMapper.INSTANCE::telecomToTelecomDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one telecom by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TelecomDTO findOne(Integer id) {
        log.debug("Request to get Telecom : {}", id);
        TelecomEntity telecom = telecomRepository.findOne(id);
        TelecomDTO telecomDTO = ITelecomMapper.INSTANCE.telecomToTelecomDTO(telecom);
        return telecomDTO;
    }

    /**
     *  Delete the  telecom by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Integer id) {
        log.debug("Request to delete Telecom : {}", id);
        telecomRepository.delete(id);
    }

	@Override
	public List<TelecomDTO> getAll(Integer accountId, String uniqueParentId, Pageable pageable) {
        return ITelecomMapper.INSTANCE.telecomsPagedToTelecomDTOs(telecomRepository.findAllByAccountIdAndParentId(accountId, uniqueParentId, pageable));
	}
}
