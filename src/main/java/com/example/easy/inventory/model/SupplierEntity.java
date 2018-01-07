package com.example.easy.inventory.model;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.example.easy.commons.model.NameCountDTO;

/**
 * A Supplier.
 */
@Entity
@Table(name = "supplier")


@SqlResultSetMappings({ 
	@SqlResultSetMapping(name = "getSupplierFrequencyReport", classes = {
		@ConstructorResult(targetClass = NameCountDTO.class, columns = {
				@ColumnResult(name = "name", type = String.class), 
				@ColumnResult(name = "count", type = Integer.class)
		}) 
	}),
	
	@SqlResultSetMapping(name = "getSupplierFrequencyById", columns = {
		@ColumnResult(name = "count", type = Integer.class)
	})

})

@NamedNativeQueries({

	@NamedNativeQuery(name = "getSupplierFrequencyReport", query = "SELECT s.name name, COUNT(s.name) count "
			+ "FROM supplier s "
			+ "LEFT JOIN orders o on o.supplierId = s.id "
			+ "WHERE s.accountId = :accountId "
			+ "GROUP BY s.name ", resultSetMapping = "getSupplierFrequencyReport"),
	
	
	@NamedNativeQuery(name = "getSupplierFrequencyById", query = "SELECT COUNT(s.name) count "
			+ "FROM supplier s "
			+ "LEFT JOIN orders o on o.supplierId = s.id "
			+ "WHERE s.accountId = :accountId AND s.id = :supplierId "
			+ "GROUP BY s.name ", resultSetMapping = "getSupplierFrequencyById"),

})
public class SupplierEntity implements Serializable {

	private static final String SUPPLIER = "SUPPLIER::";
	
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "INT(11) UNSIGNED")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "contactPerson")
    private String contactPerson;
    
    @Column(name = "description")
    private String description;

    @Column(name = "deleted")
	private Boolean deleted = false;
    @Column(name = "type")
    private String type;
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

    public SupplierEntity name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public SupplierEntity description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public SupplierEntity type(String type) {
        this.type = type;
        return this;
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
        SupplierEntity supplier = (SupplierEntity) o;
        if (supplier.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, supplier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Supplier{" +
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

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

}
