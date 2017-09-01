/**
 *Class Name: SellingPointAttendant
 *Project Name: Gapso-ejb
 *Developer: Onyedika Okafor
 *Version Info:
 *Create Date: Apr 13, 2017 2:49:21 PM
 */
package com.gapso.persistence.entity.portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class DispensingPointAttendant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private ProductDispensingPoint dispensingPoint;

    @ManyToOne
    private PortalUser attendant;
    
    private boolean active;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDispensingPoint getDispensingPoint() {
        return dispensingPoint;
    }

    public void setDispensingPoint(ProductDispensingPoint dispensingPoint) {
        this.dispensingPoint = dispensingPoint;
    }

    public PortalUser getAttendant() {
        return attendant;
    }

    public void setAttendant(PortalUser attendant) {
        this.attendant = attendant;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        if (!(object instanceof DispensingPointAttendant)) {
            return false;
        }
        DispensingPointAttendant other = (DispensingPointAttendant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gapso.persistence.entity.portal.SellingPointAttendant[ id=" + id + " ]";
    }

}
