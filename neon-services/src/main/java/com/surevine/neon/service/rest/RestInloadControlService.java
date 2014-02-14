package com.surevine.neon.service.rest;

import com.surevine.neon.service.InloadControlService;
import com.surevine.neon.util.SpringApplicationContext;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import java.util.List;
import java.util.Map;

/**
 * Provided interface exposed as a REST service serving JSON.
 */
@Path("/inload/")
@Produces("application/json")
public class RestInloadControlService implements InloadControlService {
    private Logger log = Logger.getLogger(RestInloadControlService.class);
    
    private InloadControlService serviceImplementation;

    @GET
    public void inload(@QueryParam("userID") String userID, @QueryParam("namespaces") List<String> namespaces) {
        if (serviceImplementation == null) {
            loadServiceFromSpringContext();
        }

        log.debug("Inloading data for userID [" + userID + "] against namespaces [" + namespaces + "]");
        serviceImplementation.inload(userID, namespaces);
    }

    @GET
    public void inload(@QueryParam("userID") String userID) {
        if (serviceImplementation == null) {
            loadServiceFromSpringContext();
        }

        log.debug("Inloading data for userID [" + userID + "]");
        serviceImplementation.inload(userID);
    }
    
    @GET
    @Path("importerconfig/{importerName}")
    public Map<String,String> getConfigForImporter(@PathParam("importerName") String importer) {
        if (serviceImplementation == null) {
            loadServiceFromSpringContext();
        }
        
        return serviceImplementation.getConfigForImporter(importer);
    }

    private void loadServiceFromSpringContext() {
        log.debug("Loading inloadControlService from Spring context");
        this.serviceImplementation = (InloadControlService) SpringApplicationContext.getBean("inloadControlService");
    }
}
