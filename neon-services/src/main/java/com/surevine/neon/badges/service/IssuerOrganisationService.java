package com.surevine.neon.badges.service;

import java.net.MalformedURLException;

public interface IssuerOrganisationService {

	public String getJSONString(String namespace);
	public void createIssuerOrganisationFromJSON(String jsonString, String namespace) throws MalformedURLException;
	
}
