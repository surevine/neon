package com.surevine.neon.badges.service.rest;

import java.net.MalformedURLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.surevine.neon.badges.service.IssuerOrganisationService;
import com.surevine.neon.badges.service.impl.IssuerOrganisationServiceImpl;

@Path("/badges/issuer/")
@Produces("application/json")
@Consumes("application/json")
public class RestIssuerOrganisationService implements IssuerOrganisationService {

	private IssuerOrganisationService implementation = new IssuerOrganisationServiceImpl();
	
	public void setImplementation(IssuerOrganisationService implementation) {
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
	public void createIssuerOrganisationFromJSON(String jsonString, @PathParam("namespace") String namespace) {
		try {
			implementation.createIssuerOrganisationFromJSON(jsonString, namespace);
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Could not create an issuer organisation due to a malformed URL in: "+jsonString, e);
		}
	}

}
