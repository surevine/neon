package com.surevine.neon.badges.service.rest;

import com.surevine.neon.badges.service.BadgeBakeryService;
import com.surevine.neon.util.SpringApplicationContext;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.URL;

@Path("/badges/bake/")
@Produces("image/png")
public class RestBadgeBakeryService implements BadgeBakeryService {

	private BadgeBakeryService implementation;

	@GET
    @Path("{namespace}")
	@Override
	public byte[] bake(@QueryParam("image") URL source,  @PathParam("namespace")String namespace) throws IOException {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		return implementation.bake(source, namespace);
	}
    
    private void loadServiceFromSpringContext() {
        this.implementation = (BadgeBakeryService) SpringApplicationContext.getBean("badgeBakeryService");
    }

}
