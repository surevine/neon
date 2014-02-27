package com.surevine.neon.badges.service;

import java.net.MalformedURLException;

public interface BadgeClassService {

	public String getJSONString(String namespace);
	public void createBadgeClassFromJSON(String jsonString, String namespace) throws MalformedURLException;
}
