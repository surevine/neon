package com.surevine.neon.badges.service.rest;

import com.surevine.neon.badges.service.RevocationListService;
import com.surevine.neon.util.SpringApplicationContext;

import javax.ws.rs.*;

@Path("/badges/revocationlist/")
@Produces("application/json")
@Consumes("application/json")
public class RestRevocationListService implements RevocationListService {
	private RevocationListService implementation;

	@GET
    @Path("{namespace}")
	@Override	
	public String getJSONString(@PathParam("namespace") String namespace) {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		return implementation.getJSONString(namespace);
	}

	@POST
    @Path("{namespace}")
	@Override	
	public void createRevocationListFromJSON(String json, @PathParam("namespace") String namespace) {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		implementation.createRevocationListFromJSON(json, namespace);

	}

	@PUT
	@Path("{namespace}/{uid}")
	@Override
	public void revokeBage(@PathParam("namespace") String namespace, @PathParam("uid")String uid, @QueryParam("reason")String reason) {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		implementation.revokeBage(namespace, uid, reason);
	}

    private void loadServiceFromSpringContext() {
        this.implementation = (RevocationListService) SpringApplicationContext.getBean("revocationListService");
    }
}
