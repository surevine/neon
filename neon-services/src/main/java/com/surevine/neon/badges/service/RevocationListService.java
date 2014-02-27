package com.surevine.neon.badges.service;

public interface RevocationListService {

	public String getJSONString(String namespace);
	public void createRevocationListFromJSON(String json, String namespace);
	public void revokeBage(String namespace, String uid, String reason);
}