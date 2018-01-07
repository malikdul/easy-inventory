package com.example.easy.inventory.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the Telecom entity.
 */
public class TelecomDTO implements Serializable {

	private Integer id;

    private String telecomString;

    private Integer code;

    private Integer country;
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
	private String uniqueParentId;;
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getTelecomString() {
        return telecomString;
    }

    public void setTelecomString(String telecomString) {
        this.telecomString = telecomString;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TelecomDTO telecomDTO = (TelecomDTO) o;

        if ( ! Objects.equals(id, telecomDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TelecomDTO{" +
            "id=" + id +
            ", telecomString='" + telecomString + "'" +
            ", code='" + code + "'" +
            ", country='" + country + "'" +
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

	public String getUniqueParentId() {
		return uniqueParentId;
	}

	public void setUniqueParentId(String uniqueParentId) {
		this.uniqueParentId = uniqueParentId;
	}
}
