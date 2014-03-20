package com.surevine.neon.service.rest;

import com.surevine.neon.service.SystemHealthService;
import com.surevine.neon.service.bean.SystemHealthServiceBean;
import com.surevine.neon.util.SpringApplicationContext;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * REST exposure of SystemHealthService.
 */
@Path("/system")
@Produces("application/json")
@Consumes("application/json")
public class RestSystemHealthService implements SystemHealthService {
    private SystemHealthService implementation;
    
    @GET
    @Override
    public SystemHealthServiceBean getSystemStatus() {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
        return implementation.getSystemStatus();
    }

    private void loadServiceFromSpringContext() {
        implementation = (SystemHealthService) SpringApplicationContext.getBean("systemHealthService");
    }
}
