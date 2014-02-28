package com.surevine.neon.badges.service;

import java.net.MalformedURLException;

public interface BadgeAssertionService {

	public String getJSONString(String namespace);
	public void createBadgeAssertionFromJSON(String jsonString, String namespace) throws MalformedURLException;
}
