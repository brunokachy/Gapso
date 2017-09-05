/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.interfaces;

import com.gapso.persistence.entity.activity.DailySales;
import com.gapso.persistence.entity.portal.Business;
import com.gapso.persistence.entity.portal.DispensingPointAttendant;
import com.gapso.persistence.entity.portal.PaymentOption;
import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.PortalUserRole;
import com.gapso.persistence.entity.portal.Product;
import com.gapso.persistence.entity.portal.ProductDispensingPoint;
import com.gapso.persistence.entity.portal.Station;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Onyedika Okafor
 */
@Path("create")
@Produces(MediaType.APPLICATION_JSON)
public interface CreateInterface {

    @POST
    @Path("createPortalUser")
    public Response createPortalUser(PortalUser pu);

    @POST
    @Path("createBusiness")
    public Response createBusiness(Business b);

    @POST
    @Path("createPortalUserRole")
    public Response createPortalUserRole(PortalUserRole pur);

    @POST
    @Path("createPaymentOption")
    public Response createPaymentOption(PaymentOption po);

    @POST
    @Path("createProduct")
    public Response createProduct(Product p);

    @POST
    @Path("createStation")
    public Response createStation(Station s);

    @POST
    @Path("createStationProduct")
    public Response createStationProduct(List<Long> sps);

    @POST
    @Path("recordSales")
    public Response recordSales(DailySales ds);

    @POST
    @Path("createStationPaymentOption")
    public Response createStationPaymentOption(List<Long> sps);

    @POST
    @Path("createProductDispensingPoint")
    public Response createProductDispensingPoint(ProductDispensingPoint pdp);

    @GET
    @Path("createDispensingPointAttendant")
    public Response createDispensingPointAttendant(DispensingPointAttendant dpa);
    
    

}
