/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.service;

import com.gapso.interfaces.DeleteInterface;
import com.gapso.persistence.entity.portal.Business;
import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.PortalUserRole;
import com.gapso.persistence.services.PortalPersistence;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

/**
 *
 * @author Onyedika Okafor
 */
@Stateless
public class Delete implements DeleteInterface {

    @EJB
    PortalPersistence portalPersistence;

    @Override
    public Response deletePortalUser(PortalUser pu) {
        portalPersistence.delete(pu);
        return Response.status(200).build();

    }

    @Override
    public Response deleteBusiness(Business b) {
        portalPersistence.delete(b);
        return Response.status(200).build();
    }

    @Override
    public Response deletePortalUserRole(PortalUserRole pur) {
        portalPersistence.delete(pur);
        return Response.status(200).build();
    }

}
