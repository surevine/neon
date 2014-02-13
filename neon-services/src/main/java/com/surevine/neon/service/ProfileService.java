package com.surevine.neon.service;

import com.surevine.neon.service.bean.ProfileBean;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Profile service interface configured for REST
 */
@Path("/profile/")
@Produces("application/json")
public interface ProfileService {
    @GET
    @Path("{userID}")
    public ProfileBean getProfile(@PathParam("userID") String userID);
}
