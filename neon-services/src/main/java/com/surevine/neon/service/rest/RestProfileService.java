package com.surevine.neon.service.rest;

import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.service.ProfileService;
import com.surevine.neon.util.SpringApplicationContext;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * Provided interface exposed as a REST service serving JSON.
 */
@Path("/profile/")
@Produces("application/json")
public class RestProfileService implements ProfileService {
    private Logger log = Logger.getLogger(RestProfileService.class);
    private ProfileService serviceImplementation;
    
    @GET
    @Path("{userID}")
    public ProfileBean getProfile(@PathParam("userID") String userID) {
        if (serviceImplementation == null) {
            loadServiceFromSpringContext();
        }
        
        log.debug("Running getProfile for user [" + userID + "]");
        return serviceImplementation.getProfile(userID);
    }
    
    private void loadServiceFromSpringContext() {
        log.debug("Loading profileService from Spring context");
        this.serviceImplementation = (ProfileService) SpringApplicationContext.getBean("profileService");
    }
}
