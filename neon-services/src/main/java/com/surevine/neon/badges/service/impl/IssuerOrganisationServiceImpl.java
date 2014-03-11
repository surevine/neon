package com.surevine.neon.badges.service.impl;

import com.surevine.neon.badges.dao.IssuerOrganisationDAO;
import com.surevine.neon.badges.model.IssuerOrganisation;
import com.surevine.neon.badges.service.IssuerOrganisationService;

import java.net.MalformedURLException;

public class IssuerOrganisationServiceImpl implements IssuerOrganisationService {

	private IssuerOrganisationDAO dao;
	
	@Override
	public String getJSONString(String namespace) {
		return dao.retrieve(namespace).toJSON().toString();
	}

	public void setDao(IssuerOrganisationDAO dao) {
		this.dao = dao;
	}

	@Override
	public void createIssuerOrganisationFromJSON(String jsonString, String namespace) throws MalformedURLException {
		dao.persist(new IssuerOrganisation(jsonString, namespace));

	}

}
