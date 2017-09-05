/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.service;

import com.gapso.email.services.EmailService;
import com.gapso.interfaces.ReadInterface;
import com.gapso.persistence.entity.portal.DispensingPointAttendant;
import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.PortalUserRole;
import com.gapso.persistence.services.PortalPersistence;
import com.gapso.persistence.services.Util;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Onyedika Okafor
 */
@Stateless
public class Read implements ReadInterface {

    @EJB
    PortalPersistence portalPersistence;

    @EJB
    Util util;

    @EJB
    EmailService emailService;

    @Override
    public PortalUserRole getUserRole(Long userId) {
        PortalUser pu = portalPersistence.find(PortalUser.class, userId);
        PortalUserRole pur = null;
        if (pu != null) {
            pur = portalPersistence.getActivePortalUserRole(pu);
            if (!pur.isActive()) {
                return null;
            }
            if (pur.getDesignation().equals(PortalUserRole.Designation.ATTENDANT)) {
                DispensingPointAttendant dpa = portalPersistence.getDispensingPointAttendantByActiveAttendant(pur.getPortalUser());
                if (dpa != null) {
                    pur.setDispensingPointAttendant(dpa);
                }
            }
        }
        return pur;
    }

}
