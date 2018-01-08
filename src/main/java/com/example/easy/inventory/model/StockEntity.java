package com.example.easy.inventory.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * A Product stock.
 */
@Entity
@Table(name = "stock")
public class StockEntity {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private Integer id;
    
    @NotNull(message="productId is null")
    @JoinColumn(name = "productId", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
	private Integer productId;
    
    @NotNull(message="totalQty is null")
    @Column(name = "totalQty")
    private Integer totalQty;
    
    @NotNull(message="unitPrice is null")
    @Column(name = "unitPrice")
    private Integer unitPrice;
    /**
     * Quantity actually available for use. Reserved quantity is accounted for here (available = total - reserved).
     */
    @Column(name = "availableQty")
    private Integer availableQty;
    
    @Column(name = "consumedQty")
    private Integer consumedQty;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "createdOn")
    @Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;
	@JoinColumn(name = "createdBy", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
	private Integer createdBy;
	@Column(name = "modifiedOn")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedOn;
	@JoinColumn(name = "modifiedBy", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
	private Integer modifiedBy;
	@JoinColumn(name = "accountId", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
	private Integer accountId;
    public Integer getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}
	@JoinColumn(name = "warehouseId", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
    private Integer warehouseId;
	
	@Column(name = "deleted")
	private Boolean deleted = false;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getTotalQty() {
		return totalQty;
	}
	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}
	
	/**
     * Quantity actually available for use. Reserved quantity is accounted for here (available = total - reserved).
     */
	public Integer getAvailableQty() {
		return availableQty;
	}
	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
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
	public Boolean getDeleted() {
		return deleted;
	}
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	public Integer getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getConsumedQty() {
		return consumedQty;
	}
	public void setConsumedQty(Integer consumedQty) {
		this.consumedQty = consumedQty;
	}
}
