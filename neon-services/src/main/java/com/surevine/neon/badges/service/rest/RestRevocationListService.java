package com.surevine.neon.badges.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.surevine.neon.badges.service.RevocationListService;
import com.surevine.neon.badges.service.impl.RevocationListServiceImpl;

@Path("/badges/revocationlist/")
@Produces("application/json")
@Consumes("application/json")
public class RestRevocationListService implements RevocationListService {

	private RevocationListService implementation = new RevocationListServiceImpl();
	
	public void setImplementation(RevocationListService implementation) {
		this.implementation = implementation;
	}

	@GET
    @Path("{namespace}")
	@Override	
	public String getJSONString(@PathParam("namespace") String namespace) {
		return implementation.getJSONString(namespace);
	}

	@POST
    @Path("{namespace}")
	@Override	
	public void createRevocationListFromJSON(String json, @PathParam("namespace") String namespace) {
		implementation.createRevocationListFromJSON(json, namespace);

	}

	@PUT
	@Path("{namespace}/{uid}")
	@Override
	public void revokeBage(@PathParam("namespace") String namespace, @PathParam("uid")String uid, @QueryParam("reason")String reason) {
		implementation.revokeBage(namespace, uid, reason);
	}

}
