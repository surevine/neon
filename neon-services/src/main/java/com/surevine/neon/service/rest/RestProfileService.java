package com.surevine.neon.service.rest;

import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.service.ProfileService;
import com.surevine.neon.util.SpringApplicationContext;
import org.apache.log4j.Logger;

import javax.ws.rs.*;

/**
 * Provided interface exposed as a REST service serving JSON.
 */
@Path("/profile/")
@Produces("application/json")
@Consumes("application/json")
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

    @POST
    public void addUser(@QueryParam("userID") String userID) {
        if (serviceImplementation == null) {
            loadServiceFromSpringContext();
        }
        
        log.info("Adding user " + userID + " to profile service.");
        serviceImplementation.addUser(userID);
    }

    @DELETE
    public void removeUser(@QueryParam("userID") String userID) {
        if (serviceImplementation == null) {
            loadServiceFromSpringContext();
        }

        log.info("Removing user " + userID + " from profile service.");
        // TODO: This needs a guard of some kind
        serviceImplementation.removeUser(userID);
    }


    private void loadServiceFromSpringContext() {
        log.debug("Loading profileService from Spring context");
        this.serviceImplementation = (ProfileService) SpringApplicationContext.getBean("profileService");
    }
}
