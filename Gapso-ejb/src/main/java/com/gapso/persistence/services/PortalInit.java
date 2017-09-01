/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.persistence.services;

import com.gapso.persistence.entity.portal.PaymentOption;
import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.PortalUserRole;
import com.gapso.persistence.entity.portal.Product;
import com.gapso.persistence.entity.portal.Station;
import com.gapso.persistence.entity.portal.StationPaymentOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

/**
 *
 * @author Onyedika Okafor
 */
@Singleton
@LocalBean
@Startup
public class PortalInit {

    @EJB
    private PortalPersistence portalPersistence;

    @PostConstruct
    public void init() {
        setUpGenericProduct();
        setUpSetting();
        setUpSuperAdmin();
        setUpGenericPaymentOption();
    }

    @PreDestroy
    public void destroy() {
        // Do stuff during webapp's shutdown.
    }

    private void setUpSuperAdmin() {
        PortalUser pu = portalPersistence.getUserByEmailAddress(ProjectConstant.SUPER_ADMIN_EMAIL_ADDRESS);
        if (pu == null) {
            try {
                Date today = new Date();

                pu = new PortalUser();
                pu.setFirstName("Super");
                pu.setSurname("Admin");
                pu.setActive(true);
                pu.setCreatedDate(today);
                pu.setEmailAddress(ProjectConstant.SUPER_ADMIN_EMAIL_ADDRESS);
                pu.setModifiedDate(today);
                pu.setPassword(Util.hashPassword("admin"));
                pu.setPhoneNumber("07032778564");
                pu.setToken("07032778564");
                portalPersistence.create(pu);

                PortalUserRole pur = new PortalUserRole();
                pur.setPortalUser(pu);
                pur.setDesignation(PortalUserRole.Designation.SUPERADMIN);
                portalPersistence.create(pur);
            } catch (Exception e) {
                System.err.println("setUpSuperAdmin Error :::: " + e.getLocalizedMessage());
            }
        } else {
            // System.out.println("Skip superAdmin Creation :::::::::::::::");
        }
    }

    private void setUpSetting() {
    }

    private void setUpGenericProduct() {
        Date today = new Date();

        try {
            Product p = portalPersistence.getProductByCode(ProjectConstant.AGO);
            if (p == null) {
                p = new Product();
                p.setActive(true);
                p.setCreatedDate(today);
                p.setDescription("Diesel");
                p.setModifiedDate(today);
                p.setUnit("litre");
                p.setProductCode(ProjectConstant.AGO);
                p.setName("Automated Gas Oil");
                portalPersistence.create(p);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        try {
            Product p = portalPersistence.getProductByCode(ProjectConstant.DPK);
            if (p == null) {
                p = new Product();
                p.setActive(true);
                p.setCreatedDate(today);
                p.setDescription("Kerosene");
                p.setModifiedDate(today);
                p.setUnit("litre");
                p.setProductCode(ProjectConstant.DPK);
                p.setName("Dual Purpose Kerosene");
                portalPersistence.create(p);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        try {
            Product p = portalPersistence.getProductByCode(ProjectConstant.PMS);
            if (p == null) {
                p = new Product();
                p.setActive(true);
                p.setCreatedDate(today);
                p.setDescription("Petrol");
                p.setModifiedDate(today);
                p.setUnit("litre");
                p.setProductCode(ProjectConstant.PMS);
                p.setName("Premium Motor Spirit");
                portalPersistence.create(p);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        try {
            Product p = portalPersistence.getProductByCode(ProjectConstant.LPG);
            if (p == null) {
                p = new Product();
                p.setActive(true);
                p.setCreatedDate(today);
                p.setDescription("Cooking Gas");
                p.setModifiedDate(today);
                p.setUnit("kg");
                p.setProductCode(ProjectConstant.LPG);
                p.setName("Liquefied Petroleum Gas");
                portalPersistence.create(p);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    private void createGenericStationPaymentOption(PaymentOption po) {
        List<Station> stations = portalPersistence.getAllStations();
        for (Station s : stations) {
            StationPaymentOption spo = new StationPaymentOption();
            spo.setActive(false);
            spo.setAddedDate(new Date());
            spo.setPaymentOption(po);
            spo.setStation(s);
            portalPersistence.create(spo);
        }
    }

    private void setUpGenericPaymentOption() {
        Date today = new Date();

        try {
            PaymentOption p = portalPersistence.getPaymentOptionByName(ProjectConstant.BANKTRANSFER);
            if (p == null) {
                p = new PaymentOption();
                p.setActive(true);
                p.setCreatedDate(today);
                p.setDescription(ProjectConstant.BANKTRANSFER);
                p.setModifiedDate(today);
                p.setName(ProjectConstant.BANKTRANSFER);
                portalPersistence.create(p);
                createGenericStationPaymentOption(p);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        try {
            PaymentOption p = portalPersistence.getPaymentOptionByName(ProjectConstant.POS);
            if (p == null) {
                p = new PaymentOption();
                p.setActive(true);
                p.setCreatedDate(today);
                p.setDescription(ProjectConstant.POS);
                p.setModifiedDate(today);
                p.setName(ProjectConstant.POS);
                portalPersistence.create(p);
                createGenericStationPaymentOption(p);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        try {
            PaymentOption p = portalPersistence.getPaymentOptionByName(ProjectConstant.CASH);
            if (p == null) {
                p = new PaymentOption();
                p.setActive(true);
                p.setCreatedDate(today);
                p.setDescription(ProjectConstant.CASH);
                p.setModifiedDate(today);
                p.setName(ProjectConstant.CASH);
                portalPersistence.create(p);
                createGenericStationPaymentOption(p);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        try {
            Product p = portalPersistence.getProductByCode(ProjectConstant.LPG);
            if (p == null) {
                p = new Product();
                p.setActive(true);
                p.setCreatedDate(today);
                p.setDescription("Cooking Gas");
                p.setModifiedDate(today);
                p.setUnit("kg");
                p.setProductCode(ProjectConstant.LPG);
                p.setName("Liquefied Petroleum Gas");
                portalPersistence.create(p);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
}
