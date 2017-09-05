/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.interfaces;

import com.gapso.persistence.entity.portal.Business;
import com.gapso.persistence.entity.portal.PortalUser;
import com.gapso.persistence.entity.portal.PortalUserRole;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Onyedika Okafor
 */
@Path("/delete")
@Produces(MediaType.APPLICATION_JSON)
public interface DeleteInterface {
    
    @POST
    @Path("deletePortalUser")
    public Response deletePortalUser(PortalUser pu);

    @POST
    @Path("deleteBusiness")
    public Response deleteBusiness(Business b);

    @POST
    @Path("deletePortalUserRole")
    public Response deletePortalUserRole(PortalUserRole pur);
    
}
