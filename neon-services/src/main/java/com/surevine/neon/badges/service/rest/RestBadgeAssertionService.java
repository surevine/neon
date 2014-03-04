package com.surevine.neon.badges.service.rest;

import java.net.MalformedURLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.surevine.neon.badges.service.BadgeAssertionService;
import com.surevine.neon.badges.service.impl.BadgeAssertionServiceImpl;

@Path("/badges/assertion/")
@Produces("application/json")
@Consumes("application/json")
public class RestBadgeAssertionService implements BadgeAssertionService {

	private BadgeAssertionService implementation = new BadgeAssertionServiceImpl();
	
	public void setImplementation(BadgeAssertionService implementation) {
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
	public void createBadgeAssertionFromJSON(String jsonString, @PathParam("namespace") String namespace) throws MalformedURLException {
		try {
			implementation.createBadgeAssertionFromJSON(jsonString, namespace);
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Found an invalid url in the JSON: "+jsonString, e);
		}
	}

}
