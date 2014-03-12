package com.surevine.neon.badges.service.rest;

import com.surevine.neon.badges.model.EnrichedBadgeAssertion;
import com.surevine.neon.badges.service.BadgeValidationService;
import com.surevine.neon.util.SpringApplicationContext;

import javax.ws.rs.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Path("/badges/validate")
@Produces("application/json")
@Consumes("application/json")
public class RestBadgeValidationServiceImpl implements BadgeValidationService {
	private BadgeValidationService implementation;

	@GET
	@Path("enrich")
	@Override
	public EnrichedBadgeAssertion validateAndEnrich(URL badge, @QueryParam("trustedIssuer") List<URL> trustedIssuers) throws MalformedURLException {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		return implementation.validateAndEnrich(badge, trustedIssuers);
	}

	@GET
	@Path("enrich/{userName}")
	@Override
	public EnrichedBadgeAssertion validateAndEnrich(@QueryParam("badge") URL badge, @QueryParam("trustedIssuer") List<URL> trustedIssuers, @PathParam("userName") String expectedRecipient) throws MalformedURLException {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		return implementation.validateAndEnrich(badge, trustedIssuers, expectedRecipient);
	}

	@GET
	@Path("validate")
	@Override
	public void validate(@QueryParam("badge")URL badge, @QueryParam("trustedIssuer") List<URL> trustedIssuers) {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		implementation.validate(badge, trustedIssuers);
	}

	@GET
	@Path("validate/{userName}")
	@Override
	public void validate(@QueryParam ("badge")URL badge, @QueryParam("trustedIssuer")List<URL> trustedIssuers, @PathParam("userName")String expectedRecipient) {
        if (implementation == null) {
            loadServiceFromSpringContext();
        }
		implementation.validate(badge, trustedIssuers, expectedRecipient);
	}

    private void loadServiceFromSpringContext() {
        this.implementation = (BadgeValidationService) SpringApplicationContext.getBean("badgeValidationService");
    }
}
