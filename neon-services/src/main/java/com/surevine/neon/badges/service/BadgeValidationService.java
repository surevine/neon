package com.surevine.neon.badges.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.surevine.neon.badges.model.EnrichedBadgeAssertion;

public interface BadgeValidationService {

	public EnrichedBadgeAssertion validateAndEnrich(URL badge, List<URL> trustedIssuers) throws MalformedURLException;
	
	public EnrichedBadgeAssertion validateAndEnrich(URL badge, List<URL> trustedIssuers, String expectedRecipient) throws MalformedURLException;
	
	public void validate(URL badge, List<URL> trustedIssuers);
	
	public void validate(URL badge, List<URL> trustedIssuers, String expectedRecipient);

}