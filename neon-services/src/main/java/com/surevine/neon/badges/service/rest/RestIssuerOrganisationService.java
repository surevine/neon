package com.surevine.neon.badges.service.rest;

import com.surevine.neon.badges.service.IssuerOrganisationService;
import com.surevine.neon.util.SpringApplicationContext;

import javax.ws.rs.*;
import java.net.MalformedURLException;

@Path("/badges/issuer/")
@Produces("application/json")
@Consumes("application/json")
public class RestIssuerOrganisationService implements IssuerOrganisationService {
	private IssuerOrganisationService implementation;

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
	public void createIssuerOrganisationFromJSON(String jsonString, @PathParam("namespace") String namespace) {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		try {
			implementation.createIssuerOrganisationFromJSON(jsonString, namespace);
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Could not create an issuer organisation due to a malformed URL in: "+jsonString, e);
		}
	}

    private void loadServiceFromSpringContext() {
        this.implementation = (IssuerOrganisationService) SpringApplicationContext.getBean("issuerOrganisationService");
    }
}
