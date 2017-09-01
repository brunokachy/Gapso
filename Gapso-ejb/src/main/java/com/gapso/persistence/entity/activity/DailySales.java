/**
 *Class Name: DailySales
 *Project Name: Gapso-ejb
 *Developer: Onyedika Okafor
 *Version Info:
 *Create Date: Apr 13, 2017 3:33:36 PM
 */
package com.gapso.persistence.entity.activity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gapso.persistence.entity.portal.DispensingPointAttendant;
import com.gapso.persistence.entity.portal.StationPaymentOption;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 * @author Onyedika Okafor
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailySales implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date saleDate;

    private Double quantity;

    @ManyToOne
    private DispensingPointAttendant dispensingPointAttendant;

    @ManyToOne
    private StationProductPrice stationProductPrice;

    @ManyToOne
    private StationPaymentOption stationPaymentOption;
    
    @Transient
    private Long paymentOptionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public DispensingPointAttendant getDispensingPointAttendant() {
        return dispensingPointAttendant;
    }

    public void setDispensingPointAttendant(DispensingPointAttendant dispensingPointAttendant) {
        this.dispensingPointAttendant = dispensingPointAttendant;
    }

    public StationProductPrice getStationProductPrice() {
        return stationProductPrice;
    }

    public void setStationProductPrice(StationProductPrice stationProductPrice) {
        this.stationProductPrice = stationProductPrice;
    }

    public StationPaymentOption getStationPaymentOption() {
        return stationPaymentOption;
    }

    public void setStationPaymentOption(StationPaymentOption stationPaymentOption) {
        this.stationPaymentOption = stationPaymentOption;
    }

    public Long getPaymentOptionId() {
        return paymentOptionId;
    }

    public void setPaymentOptionId(Long paymentOptionId) {
        this.paymentOptionId = paymentOptionId;
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
        if (!(object instanceof DailySales)) {
            return false;
        }
        DailySales other = (DailySales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gaspo.persistence.entity.activity.DailySales[ id=" + id + " ]";
    }

}
