package com.surevine.neon.badges.service.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.surevine.neon.badges.service.BadgeClassService;
import com.surevine.neon.badges.service.impl.BadgeClassServiceImpl;
import com.surevine.neon.model.ProfileBean;

@Path("/badges/class/")
@Produces("application/json")
@Consumes("application/json")
public class RestBadgeClassService implements BadgeClassService {
	
	private BadgeClassService implementation = new BadgeClassServiceImpl();

	public void setImplementation(BadgeClassService implementation) {
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
	public void createBadgeClassFromJSON(String json, @PathParam("namespace") String namespace) {
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
		return implementation.getUsersForBadge(namespace);
	}
}