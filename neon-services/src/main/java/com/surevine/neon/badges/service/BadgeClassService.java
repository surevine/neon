package com.surevine.neon.badges.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import com.surevine.neon.model.ProfileBean;

public interface BadgeClassService {

	public String getJSONString(String namespace);
	public void createBadgeClassFromJSON(String jsonString, String namespace) throws MalformedURLException;
	public Collection<ProfileBean> getUsersForBadge(String namespace);

}