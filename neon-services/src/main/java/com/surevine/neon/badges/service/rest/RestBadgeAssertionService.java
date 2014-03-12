package com.surevine.neon.badges.service.rest;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.service.BadgeAssertionService;
import com.surevine.neon.badges.service.impl.BadgeAssertionServiceImpl;
import com.surevine.neon.util.SpringApplicationContext;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

@Path("/badges/assertion/")
@Produces("application/json")
@Consumes("application/json")
public class RestBadgeAssertionService implements BadgeAssertionService {
	private Logger logger = Logger.getLogger(RestBadgeAssertionService.class);
	private BadgeAssertionService implementation;

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
	public void createBadgeAssertionFromJSON(String jsonString, @PathParam("namespace") String namespace) throws MalformedURLException {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
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
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		return implementation.getBadgeAssertions(username, validate, trustedIssuers);
	}

	@GET
	@Path("/list/html/{username}")
	@Produces("text/html")
	@Override
	public String getBadgeMarkup(@PathParam("username") String username, @QueryParam("trustedIssuer")List<URL> trustedIssuers) {
		logger.debug("Getting badge fragment for "+username);
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		return implementation.getBadgeMarkup(username, trustedIssuers);
	}

    private void loadServiceFromSpringContext() {
        this.implementation = (BadgeAssertionService) SpringApplicationContext.getBean("badgeAssertionService");
    }

}
