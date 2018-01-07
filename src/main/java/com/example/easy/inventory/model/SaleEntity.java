package com.example.easy.inventory.model;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * A Sale.
 */
@Entity
@Table(name = "sale")
public class SaleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private Integer id;
//    @Basic(optional = false)
//    @NotNull
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "saleDate")
//    private Date saleDate;
    @Column(name = "deleted")
	private Boolean deleted = false;
    @Column(name = "isDraft")
	private boolean isDraft = false;
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
	
	@NotNull
	@JoinColumn(name = "bookingId", referencedColumnName = "id", columnDefinition = "INT(11) UNSIGNED")
	private Integer bookingId;
	@Basic(optional = false)
    @NotNull
    @Column(name = "eventDate")
    @Temporal(TemporalType.TIMESTAMP)
	private Date eventDate;
	
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
        SaleEntity sale = (SaleEntity) o;
        if (sale.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sale.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sale{" +
            "id=" + id +
            ", bookingId='" + bookingId + "'" +
            ", createdOn='" + createdOn + "'" +
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

	public boolean getIsDraft() {
		return isDraft;
	}

	public void setIsDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}
}
