package com.surevine.neon.badges.service;

import java.net.MalformedURLException;
import java.util.Collection;

import com.surevine.neon.badges.model.BadgeAssertion;

public interface BadgeAssertionService {

	public String getJSONString(String namespace);
	public void createBadgeAssertionFromJSON(String jsonString, String namespace) throws MalformedURLException;
	public Collection<BadgeAssertion> getBadgeAssertions(String username);
}
