package com.example.easy.inventory.dto;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.example.easy.commons.model.IBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements IBaseDTO, Serializable {

    private Integer id;

    private String name;

    private String description;

    private String category;

    private Boolean disposable;

    private String unit;
    
    private Integer reorderPoint;
    
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;

        if ( ! Objects.equals(id, productDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", category='" + category + "'" +
            ", disposable='" + disposable + "'" +
            ", unit='" + unit + "'" +
            '}';
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getDisposable() {
		return disposable;
	}

	public void setDisposable(Boolean disposable) {
		this.disposable = disposable;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public Integer getReorderPoint() {
		return reorderPoint;
	}

	public void setReorderPoint(Integer reorderPoint) {
		this.reorderPoint = reorderPoint;
	}

	
}
