package com.surevine.neon.badges.service.rest;

import java.io.IOException;
import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.surevine.neon.badges.service.BadgeBakeryService;
import com.surevine.neon.badges.service.impl.BadgeBakeryServiceImpl;

@Path("/badges/bake/")
@Produces("image/png")
public class RestBadgeBakeryService implements BadgeBakeryService {

	private BadgeBakeryService implementation = new BadgeBakeryServiceImpl();
	
	public void setImplementation(BadgeBakeryService implementation) {
		this.implementation = implementation;
	}

	@GET
    @Path("{namespace}")
	@Override
	public byte[] bake(@QueryParam("image") URL source,  @PathParam("namespace")String namespace) throws IOException {
		return implementation.bake(source, namespace);
	}

}
