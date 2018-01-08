package com.example.easy.inventory.model;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * A Product.
 */
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private Integer id;

    @Column(name = "notes")
    private String notes;
    
    @Convert( converter=OrderStatusConverter.class )
    @Column(name = "status")
    private OrderStatus status;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "paymentMode")
    private String paymentMode;
    
    @Column(name = "paymentStatus")
    private String paymentStatus;
    
    @Column(name = "totalCost")
    private Integer totalCost;
    
    @Column(name = "totalPaid")
    private Integer totalPaid;
    
    @Column(name = "totalBalance")
    private Integer totalBalance;
    
    @Column(name = "expectedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expectedDate;
    
    @Column(name = "deliveryDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;

    @Column(name = "deleted")
	private Boolean deleted = false;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orderDate")
    @Temporal(TemporalType.TIMESTAMP)
	private Date orderDate;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "createdOn")
    @Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;
	
    @NotNull
    @JoinColumn(name = "createdBy", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
	private Integer createdBy;
    
    @Column(name = "modifiedOn")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedOn;
    
    @JoinColumn(name = "modifiedBy", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
	private Integer modifiedBy;
    
    @JoinColumn(name = "accountId", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
    private Integer accountId;
    
    @NotNull
    @JoinColumn(name = "supplierId", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
    private Integer supplierId;
    
//    @NotNull
    @JoinColumn(name = "warehouseId", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
    private Integer warehouseId;
    
    public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
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
        OrderEntity product = (OrderEntity) o;
        if (product.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", orderDate='" + orderDate + "'" +
            ", notes='" + notes + "'" +
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

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getExpectedDate() {
		return expectedDate;
	}

	public void setExpectedDate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Integer getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Integer totalCost) {
		this.totalCost = totalCost;
	}

	public Integer getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(Integer totalPaid) {
		this.totalPaid = totalPaid;
	}

	public Integer getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Integer totalBalance) {
		this.totalBalance = totalBalance;
	}


}
