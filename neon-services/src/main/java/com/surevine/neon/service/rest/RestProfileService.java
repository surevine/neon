package com.surevine.neon.service.rest;

import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.service.ProfileService;
import com.surevine.neon.util.SpringApplicationContext;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/profile/")
@Produces("application/json")
public class RestProfileService implements ProfileService {
    private ProfileService serviceImplementation;
    
    @GET
    @Path("{userID}")
    public ProfileBean getProfile(@PathParam("userID") String userID) {
        if (serviceImplementation == null) {
            loadServiceFromSpringContext();
        }
        return serviceImplementation.getProfile(userID);
    }
    
    private void loadServiceFromSpringContext() {
        this.serviceImplementation = (ProfileService) SpringApplicationContext.getBean("profileService");
    }
}
