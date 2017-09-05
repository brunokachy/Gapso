/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.service;

import com.gapso.email.services.EmailService;
import com.gapso.interfaces.GenericInterface;
import com.gapso.persistence.entity.portal.Business;
import com.gapso.persistence.entity.portal.DispensingPointAttendant;
import com.gapso.persistence.entity.portal.PasswordChangeLog;
import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.PortalUserRole;
import com.gapso.persistence.entity.portal.PortalUserSessionLog;
import com.gapso.persistence.entity.portal.Station;
import com.gapso.persistence.services.PortalPersistence;
import com.gapso.persistence.services.Util;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

/**
 *
 * @author Onyedika Okafor
 */
@Stateless
public class Generic implements GenericInterface {

    @EJB
    PortalPersistence portalPersistence;

    @EJB
    Util util;

    @EJB
    EmailService emailService;

    @Override
    public Response sendNewUserEmail(PortalUser pu) {
        String password = Util.hashPassword(pu.getFirstName() + "-" + pu.getSurname());
        pu.setPassword(password);
        portalPersistence.update(pu);
        String message = "Business Registration Request Received. Admin will contact you once your request is approved. \n"
                + "\n"
                + "Your temprary password is " + password + "\n"
                + "You are adviced to change your password once you log in. \n"
                + "Thank you for using Gapso.";
        String receipient = pu.getEmailAddress();
        String subject = "Business Registration Request Received";
        emailService.prepareEmail(message, subject, receipient);
        return Response.status(200).entity(new StringBuilder("Email Sent!!!")).build();
    }

    @Override
    public Response login(String emailAddress, String password) {

        if (emailAddress.isEmpty()) {
            return Response.status(400).entity(new StringBuilder("Email Address is Empty")).build();
        }

        if (password.isEmpty()) {
            return Response.status(400).entity(new StringBuilder("Password is Empty")).build();
        }

        PortalUser currentUser = util.login(emailAddress, password);
        if (currentUser == null) {
            return Response.status(400).entity("User Does not Exist").build();
        }
        return Response.status(200).entity(currentUser).build();
    }

    @Override
    public Response validateLoginEmail(String emailAddress) {
        if (emailAddress.isEmpty()) {
            return Response.status(400).entity(new StringBuilder("Email Address is Empty")).build();
        }

        PortalUser p = portalPersistence.getUserByEmailAddress(emailAddress);

        if (p == null) {
            return Response.status(400).entity("Email Address is not Valid").build();
        }
        return Response.status(200).entity(p).build();
    }

    @Override
    public Response logUserSession(String ipaddress, String userEmail, Long userId) {
        try {
            PortalUserSessionLog log = new PortalUserSessionLog();
            log.setDateCreated(new Date());
            log.setIpaddress(ipaddress);
            log.setUserEmail(userEmail);
            log.setUserId(userId);

            portalPersistence.create(log);
            return Response.ok(200).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity(new StringBuilder("Error Creating Log")).build();
        }
    }

    @Override
    public Response checkBusinessManagerValidity(Long userid, Long businessId) {
        PortalUser pu = portalPersistence.find(PortalUser.class, userid);
        String response = "FALSE";
        if (pu.isActive()) {
            Business b = portalPersistence.find(Business.class, businessId);
            if (b.isActive() && b.isApproved()) {
                response = "TRUE";
            }
        }
        return Response.status(200).entity(new StringBuilder(response)).build();
    }

    @Override
    public Response checkStationManagerValidity(Long userid, Long stationId, Long roleId) {
        PortalUser pu = portalPersistence.find(PortalUser.class, userid);
        String response = "FALSE";
        if (pu.isActive()) {
            PortalUserRole pur = portalPersistence.find(PortalUserRole.class, roleId);
            if (pur.isActive()) {
                Station s = portalPersistence.find(Station.class, stationId);
                if (s.isActive()) {
                    if (s.getBusiness().isActive() && s.getBusiness().isApproved()) {
                        response = "TRUE";
                    }
                }
            }
        }
        return Response.status(200).entity(new StringBuilder(response)).build();
    }

    @Override
    public Response checkAttendantValidity(Long userid, Long dispAttendantId, Long roleId) {
        PortalUser pu = portalPersistence.find(PortalUser.class, userid);
        String response = "FALSE";
        if (pu.isActive()) {
            PortalUserRole pur = portalPersistence.find(PortalUserRole.class, roleId);
            if (pur.isActive()) {
                DispensingPointAttendant dpa = portalPersistence.find(DispensingPointAttendant.class, dispAttendantId);
                if (dpa.isActive()) {
                    if (dpa.getDispensingPoint().isActive()) {
                        if (dpa.getDispensingPoint().getStation().isActive()) {
                            if (dpa.getDispensingPoint().getStation().getBusiness().isActive() && dpa.getDispensingPoint().getStation().getBusiness().isApproved()) {
                                response = "TRUE";
                            }
                        }
                    }
                }
            }
        }
        return Response.status(200).entity(new StringBuilder(response)).build();
    }

    @Override
    public Response validateEmail(String emailAddress) {
        if (emailAddress.isEmpty()) {
            return Response.status(404).entity(new StringBuilder("Email Address is Empty")).build();
        }

        PortalUser p = portalPersistence.getUserByEmailAddress(emailAddress);

        if (p == null) {
            return Response.status(404).entity("Email Address is not Valid").build();
        }

        String nPassword = Util.generateToken();
        p.setPassword(Util.hashPassword(nPassword.substring(0, 5)));
        p.setModifiedDate(new Date());
        portalPersistence.update(p);

        System.out.println("new password" + nPassword.substring(0, 5));

        PasswordChangeLog log = new PasswordChangeLog();
        log.setCompletedTime(new Date());
        log.setPortalUser(p);
        portalPersistence.create(log);

        String message = "Your password reset request was successful. Your new password is " + nPassword + "\n"
                + "You are adviced to change your password once you log in. \n"
                + "Thank you for using Gapso.";
        String receipient = p.getEmailAddress();
        String subject = "Password Reset";
        emailService.prepareEmail(message, subject, receipient);

        return Response.ok(p).build();
    }

}
