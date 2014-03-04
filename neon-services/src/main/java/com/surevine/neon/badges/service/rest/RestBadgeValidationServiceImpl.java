package com.surevine.neon.badges.service.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.model.EnrichedBadgeAssertion;
import com.surevine.neon.badges.service.BadgeValidationService;
import com.surevine.neon.badges.service.impl.BadgeValidationServiceImpl;

@Path("/badges/validate")
@Produces("application/json")
@Consumes("application/json")
public class RestBadgeValidationServiceImpl implements BadgeValidationService {

	private BadgeValidationService implementation = new BadgeValidationServiceImpl();
	

	@GET
	@Path("enrich")
	@Override
	public EnrichedBadgeAssertion validateAndEnrich(URL badge, @QueryParam("trustedIssuer") List<URL> trustedIssuers) throws MalformedURLException {
		return implementation.validateAndEnrich(badge, trustedIssuers);
	}

	@GET
	@Path("enrich/{userName}")
	@Override
	public EnrichedBadgeAssertion validateAndEnrich(@QueryParam("badge") URL badge, @QueryParam("trustedIssuer") List<URL> trustedIssuers, @PathParam("userName") String expectedRecipient) throws MalformedURLException {
		return implementation.validateAndEnrich(badge, trustedIssuers, expectedRecipient);
	}

	@GET
	@Path("validate")
	@Override
	public void validate(@QueryParam("badge")URL badge, @QueryParam("trustedIssuer") List<URL> trustedIssuers) {
		implementation.validate(badge, trustedIssuers);
	}

	@GET
	@Path("validate/{userName}")
	@Override
	public void validate(@QueryParam ("badge")URL badge, @QueryParam("trustedIssuer")List<URL> trustedIssuers, @PathParam("userName")String expectedRecipient) {
		implementation.validate(badge, trustedIssuers, expectedRecipient);
	}


}
