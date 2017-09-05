/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.service;

import com.gapso.email.services.EmailService;
import com.gapso.interfaces.CreateInterface;
import com.gapso.main.HelperClass;
import com.gapso.persistence.entity.activity.DailySales;
import com.gapso.persistence.entity.portal.Business;
import com.gapso.persistence.entity.portal.DispensingPointAttendant;
import com.gapso.persistence.entity.portal.PaymentOption;
import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.PortalUserRole;
import com.gapso.persistence.entity.portal.Product;
import com.gapso.persistence.entity.portal.ProductDispensingPoint;
import com.gapso.persistence.entity.portal.Station;
import com.gapso.persistence.services.PortalPersistence;
import com.gapso.persistence.services.Util;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

/**
 *
 * @author Onyedika Okafor
 */
@Stateless
public class Create implements CreateInterface {

    @EJB
    PortalPersistence portalPersistence;

    @EJB
    Util util;

    @EJB
    EmailService emailService;

    @EJB
    HelperClass hc;

    @Override
    public Response createPortalUser(PortalUser pu) {
        if (pu == null) {
            return Response.status(400).entity(new StringBuilder("Error Creating User. User is null")).build();
        }
        try {
            pu.setActive(true);
            pu.setCreatedDate(new Date());
            pu.setModifiedDate(new Date());
            portalPersistence.create(pu);

            return Response.status(200).entity(pu).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(new StringBuilder("Error Creating User")).build();
        }
    }

    @Override
    public Response createBusiness(Business b) {
        if (b == null) {
            return Response.status(400).entity(new StringBuilder("Error Creating Business. Business is null")).build();
        }
        try {
            b.setActive(true);
            b.setCreatedDate(new Date());
            b.setModifiedDate(new Date());
            b.setApproved(false);
            portalPersistence.create(b);

            return Response.status(200).entity(b).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(new StringBuilder("Error Creating Business")).build();
        }
    }

    @Override
    public Response createPortalUserRole(PortalUserRole pur) {
        if (pur == null) {
            return Response.status(400).entity(new StringBuilder("Error Creating Portal User Role. Portal User Role is null")).build();
        }
        try {
            pur.setActive(true);
            pur.setDesignation(hc.findDesignationByValue(pur.getPortalUser().getDesignation()));
            portalPersistence.create(pur);

            return Response.status(200).entity(pur).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(new StringBuilder("Error Creating Portal User Role")).build();
        }
    }

    @Override
    public Response createPaymentOption(PaymentOption po) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response createProduct(Product p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response createStation(Station s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response createStationProduct(List<Long> sps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response recordSales(DailySales ds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response createStationPaymentOption(List<Long> sps) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response createProductDispensingPoint(ProductDispensingPoint pdp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response createDispensingPointAttendant(DispensingPointAttendant dpa) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
