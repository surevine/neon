package com.surevine.neon.badges.service.rest;

import com.surevine.neon.badges.service.BadgeClassService;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.util.SpringApplicationContext;

import javax.ws.rs.*;
import java.net.MalformedURLException;
import java.util.Collection;

@Path("/badges/class/")
@Produces("application/json")
@Consumes("application/json")
public class RestBadgeClassService implements BadgeClassService {
	
	private BadgeClassService implementation;

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
	public void createBadgeClassFromJSON(String json, @PathParam("namespace") String namespace) {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		try {
			implementation.createBadgeClassFromJSON(json, namespace);
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Found an invalid url in the JSON: "+json, e);
		}
	}

	@GET
	@Path("{namespace}/people")
	@Override
	public Collection<ProfileBean> getUsersForBadge(@PathParam("namespace") String namespace) {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		return implementation.getUsersForBadge(namespace);
	}

    private void loadServiceFromSpringContext() {
        this.implementation = (BadgeClassService) SpringApplicationContext.getBean("badgeClassService");
    }
}