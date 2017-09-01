/**
 *Class Name: PortalUserRole
 *Project Name: Gapso-ejb
 *Developer: Onyedika Okafor
 *Version Info:
 *Create Date: Apr 13, 2017 2:07:59 PM
 */
package com.gapso.persistence.entity.portal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * @author Onyedika Okafor
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortalUserRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Business business;

    @ManyToOne
    private Station station;

    @ManyToOne
    private PortalUser portalUser;

    @Transient
    private DispensingPointAttendant dispensingPointAttendant;

    @Enumerated(EnumType.STRING)
    private Designation designation;

    private boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public PortalUser getPortalUser() {
        return portalUser;
    }

    public void setPortalUser(PortalUser portalUser) {
        this.portalUser = portalUser;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DispensingPointAttendant getDispensingPointAttendant() {
        return dispensingPointAttendant;
    }

    public void setDispensingPointAttendant(DispensingPointAttendant dispensingPointAttendant) {
        this.dispensingPointAttendant = dispensingPointAttendant;
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
        if (!(object instanceof PortalUserRole)) {
            return false;
        }
        PortalUserRole other = (PortalUserRole) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gapso.persistence.entity.portal.PortalUserRole[ id=" + id + " ]";
    }

    public enum Designation {
        SUPERADMIN("Super Admin"),
        BUSINESSMANAGER("Business Manager"),
        STATIONMANAGER("Station Manager"),
        ATTENDANT("Attendant");

        private final String designation;

        private Designation(String designation) {
            this.designation = designation;
        }

        public String getDesignation() {
            return designation;
        }
    }

}
