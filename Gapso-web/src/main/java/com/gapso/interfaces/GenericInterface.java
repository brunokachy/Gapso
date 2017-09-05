/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.interfaces;

import com.gapso.persistence.entity.portal.PortalUser;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Onyedika Okafor
 */
@Path("/generic")
@Produces(MediaType.APPLICATION_JSON)
public interface GenericInterface {

    @POST
    @Path("sendNewUserEmail")
    public Response sendNewUserEmail(PortalUser pu);

    @GET
    @Path("login")
    public Response login(@QueryParam("emailAddress") String emailAddress, @QueryParam("password") String password);

    @GET
    @Path("validateLoginEmail")
    public Response validateLoginEmail(@QueryParam("emailAddress") String emailAddress);
    
    @GET
    @Path("validateEmail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateEmail(@QueryParam("emailAddress") String emailAddress);

    @GET
    @Path("logUserSession")
    public Response logUserSession(@QueryParam("ipaddress") String ipaddress, @QueryParam("userEmail") String userEmail,
            @QueryParam("userId") Long userId);

    @GET
    @Path("checkBusinessManagerValidity")
    public Response checkBusinessManagerValidity(@QueryParam("userid") Long userid, @QueryParam("businessId") Long businessId);

    @GET
    @Path("checkStationManagerValidity")
    public Response checkStationManagerValidity(@QueryParam("userid") Long userid, @QueryParam("stationId") Long stationId, @QueryParam("roleId") Long roleId);

    @GET
    @Path("checkAttendantValidity")
    public Response checkAttendantValidity(@QueryParam("userid") Long userid, @QueryParam("dispAttendantId") Long dispAttendantId, @QueryParam("roleId") Long roleId);

}
