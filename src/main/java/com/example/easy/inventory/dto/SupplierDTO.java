package com.example.easy.inventory.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.example.easy.commons.model.IBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the Supplier entity.
 */
public class SupplierDTO implements IBaseDTO, Serializable {

	private static final String SUPPLIER = "SUPPLIER::";
			
    private Integer id;

    private String name;
    
    private String contactPerson;

    private String description;
    
    private Integer frequency;
    
    private List<TelecomDTO> telecoms;
    
    private List<AddressDTO> addresses;

    private String type;
    @JsonIgnore
    private Date createdOn;
    @JsonIgnore
    private Integer createdBy;
    @JsonIgnore
    private Date modifiedOn;
    @JsonIgnore
	private Integer modifiedBy;
    @JsonIgnore
    private Integer accountId;

    @JsonIgnore
    public String getIdString() {
        return SUPPLIER + id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SupplierDTO supplierDTO = (SupplierDTO) o;

        if ( ! Objects.equals(id, supplierDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SupplierDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", type='" + type + "'" +
            '}';
    }

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public List<TelecomDTO> getTelecoms() {
		return telecoms;
	}

	public void setTelecoms(List<TelecomDTO> telecoms) {
		this.telecoms = telecoms;
	}

	public List<AddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<AddressDTO> addresses) {
		this.addresses = addresses;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
}
