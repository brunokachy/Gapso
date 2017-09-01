/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.persistence.entity.activity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 *
 * @author Onyedika Okafor
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class StationProductMinLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private StationProduct stationProduct;

    private boolean active;

    private Double mininumLevel = 0.0;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StationProduct getStationProduct() {
        return stationProduct;
    }

    public void setStationProduct(StationProduct stationProduct) {
        this.stationProduct = stationProduct;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Double getMininumLevel() {
        return mininumLevel;
    }

    public void setMininumLevel(Double mininumLevel) {
        this.mininumLevel = mininumLevel;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
        if (!(object instanceof StationProductMinLevel)) {
            return false;
        }
        StationProductMinLevel other = (StationProductMinLevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gapso.persistence.entity.activity.StationProductMinLevel[ id=" + id + " ]";
    }

}
