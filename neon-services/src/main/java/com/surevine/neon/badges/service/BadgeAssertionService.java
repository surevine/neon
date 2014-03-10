package com.surevine.neon.badges.service;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.List;

import com.surevine.neon.badges.model.BadgeAssertion;
import java.net.URL;

public interface BadgeAssertionService {

	public String getJSONString(String namespace);
	public void createBadgeAssertionFromJSON(String jsonString, String namespace) throws MalformedURLException;
	public Collection<BadgeAssertion> getBadgeAssertions(String username, boolean validate, List<URL> trustedIssuers);
	public String getBadgeMarkup(String username, List<URL> trustedIssuers);
}
