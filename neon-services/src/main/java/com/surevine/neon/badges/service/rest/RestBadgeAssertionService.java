package com.surevine.neon.badges.service.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.service.BadgeAssertionService;
import com.surevine.neon.badges.service.impl.BadgeAssertionServiceImpl;

@Path("/badges/assertion/")
@Produces("application/json")
@Consumes("application/json")
public class RestBadgeAssertionService implements BadgeAssertionService {

	private Logger logger = Logger.getLogger(RestBadgeAssertionService.class);
	
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

	@GET
	@Path("/list/{username}")
	@Override
	public Collection<BadgeAssertion> getBadgeAssertions(@PathParam("username") String username, @QueryParam("validate") @DefaultValue("false") boolean validate, @QueryParam("trustedIssuer")List<URL> trustedIssuers) {
		return implementation.getBadgeAssertions(username, validate, trustedIssuers);
	}

	@GET
	@Path("/list/html/{username}")
	@Produces("text/html")
	@Override
	public String getBadgeMarkup(@PathParam("username") String username, @QueryParam("trustedIssuer")List<URL> trustedIssuers) {
		logger.debug("Getting badge fragment for "+username);
		return implementation.getBadgeMarkup(username, trustedIssuers);
	}
	

}
