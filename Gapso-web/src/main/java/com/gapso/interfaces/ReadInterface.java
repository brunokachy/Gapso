/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gapso.interfaces;

import com.gapso.persistence.entity.portal.PortalUserRole;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Onyedika Okafor
 */
@Path("/read")
@Produces(MediaType.APPLICATION_JSON)
public interface ReadInterface {
    
    @GET
    @Path("getUserRole")
    public PortalUserRole getUserRole(@QueryParam("userId") Long userId);
}
