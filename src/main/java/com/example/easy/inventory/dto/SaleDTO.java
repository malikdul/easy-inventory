package com.example.easy.inventory.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.example.easy.commons.model.IBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the Sale entity.
 */
public class SaleDTO implements IBaseDTO, Serializable {

    private Integer id;

    private Integer bookingId;
	
	private Date eventDate;
	
	private boolean isDraft;
    
	private List<SaleDetailsDTO> details;
    
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SaleDTO saleDTO = (SaleDTO) o;

        if ( ! Objects.equals(id, saleDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SaleDTO{" +
            "id=" + id +
            ", bookingId='" + bookingId + "'" +
            ", eventDate='" + eventDate + "'" +
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

	public Integer getBookingId() {
		return bookingId;
	}

	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public List<SaleDetailsDTO> getDetails() {
		return details;
	}

	public void setDetails(List<SaleDetailsDTO> details) {
		this.details = details;
	}

	public boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}

}
