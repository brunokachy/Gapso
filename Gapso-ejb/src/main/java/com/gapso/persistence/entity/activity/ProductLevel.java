/**
 *Class Name: DailyProductLevel
 *Project Name: Gapso-ejb
 *Developer: Onyedika Okafor
 *Version Info:
 *Create Date: Apr 13, 2017 3:35:45 PM
 */
package com.gapso.persistence.entity.activity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.StationProduct;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 * @author Onyedika Okafor
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @ManyToOne
    private StationProduct stationProduct;

    private Double newLevel = 0.0;

    private Double previousLevel = 0.0;

    private Double newSupply = 0.0;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public StationProduct getStationProduct() {
        return stationProduct;
    }

    public void setStationProduct(StationProduct stationProduct) {
        this.stationProduct = stationProduct;
    }

    public Double getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(Double newLevel) {
        this.newLevel = newLevel;
    }

    public Double getPreviousLevel() {
        return previousLevel;
    }

    public void setPreviousLevel(Double previousLevel) {
        this.previousLevel = previousLevel;
    }

    public Double getNewSupply() {
        return newSupply;
    }

    public void setNewSupply(Double newSupply) {
        this.newSupply = newSupply;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProductLevel)) {
            return false;
        }
        ProductLevel other = (ProductLevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gapso.persistence.entity.activity.DailyProductLevel[ id=" + id + " ]";
    }

}
