package com.example.easy.inventory.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.example.easy.commons.model.IBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the OrderDetails entity.
 */
public class SaleDetailsDTO implements IBaseDTO, Serializable {

	private Integer id;

	private Integer productId;

	private String saleUnit;

	private Integer salePrice;

	private Integer quantity;

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

	private ProductDTO product;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		SaleDetailsDTO orderDetailsDTO = (SaleDetailsDTO) o;

		if (!Objects.equals(id, orderDetailsDTO.id)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "OrderDetailsDTO{" + "id=" + id + ", salePrice='" + salePrice + "'" + ", quantity='" + quantity
				+ "'" + '}';
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

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getSaleUnit() {
		return saleUnit;
	}

	public void setSaleUnit(String saleUnit) {
		this.saleUnit = saleUnit;
	}

	public Integer getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Integer salePrice) {
		this.salePrice = salePrice;
	}
}
