package com.example.easy.inventory.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.easy.commons.helper.Utility;
import com.example.easy.inventory.dto.LookupDTO;
import com.example.easy.inventory.mapper.ILookupMapper;
import com.example.easy.inventory.model.LookupEntity;
import com.example.easy.inventory.model.ProductEntity;
import com.example.easy.inventory.repository.LookupRepository;
import com.example.easy.inventory.service.ILookupService;

/**
 * Service Implementation for managing Lookup.
 */
@Service
@Transactional
public class LookupServiceImpl implements ILookupService {

    private final Logger log = LoggerFactory.getLogger(LookupServiceImpl.class);
    
    @Autowired
    private LookupRepository lookupRepository;


    /**
     * Save a category.
     *
     * @param lookupEntity the entity to save
     * @return the persisted entity
     */
    @Override
    public LookupDTO save(LookupDTO lookupDTO) {
    	if (lookupDTO.getId() == null)
		{
    		lookupDTO.setCreatedOn(Utility.getCurrentDate());

		} else
		{
			lookupDTO.setModifiedOn(Utility.getCurrentDate());

			LookupEntity savedEntity = lookupRepository.findOne(lookupDTO.getId());

			if (savedEntity != null)
			{
				if (lookupDTO.getCreatedOn() == null)
				{
					lookupDTO.setCreatedOn(savedEntity.getCreatedOn());
				}
			}
		}
        log.debug("Request to save Category : {}", lookupDTO);
        LookupEntity lookup = ILookupMapper.INSTANCE.lookupDTOToLookup(lookupDTO);
        lookup = lookupRepository.save(lookup);
        LookupDTO result = ILookupMapper.INSTANCE.lookupToLookupDTO(lookup);
        return result;
    }

    /**
     *  Get all Lookup values.
     *  
     *  @return the list of entities
     */
	@Override
	@Transactional(readOnly = true)
	public List<LookupDTO> getAll(Integer accountId, Pageable pageable) {
		 log.debug("Request to get all Products");

	        return ILookupMapper.INSTANCE.lookupToLookupDTOs(
	        		lookupRepository.findAllByAccountId(accountId, pageable));
	}

    /**
     *  Get one category by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LookupDTO findOne(Integer id) {
        log.debug("Request to get Category : {}", id);
        LookupEntity category = lookupRepository.findOne(id);
        LookupDTO lookupDTO = ILookupMapper.INSTANCE.lookupToLookupDTO(category);
        return lookupDTO;
    }

    /**
     *  Delete the  category by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Integer id) {
        log.debug("Request to delete Category : {}", id);
        lookupRepository.delete(id);
    }
}
