package com.example.easy.inventory.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.example.easy.commons.model.IBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A DTO for the OrderDetails entity.
 */
public class OrderDetailsDTO implements IBaseDTO, Serializable {

	private Integer id;

	private Integer productId;

	private String purchaseUnit;

	private Integer purchasePrice;

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
	@JsonIgnore
	private Integer orderId;

	private ProductDTO product;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Integer purchasePrice) {
		this.purchasePrice = purchasePrice;
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

		OrderDetailsDTO orderDetailsDTO = (OrderDetailsDTO) o;

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
		return "OrderDetailsDTO{" + "id=" + id + ", purchasePrice='" + purchasePrice + "'" + ", quantity='" + quantity
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

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
}
