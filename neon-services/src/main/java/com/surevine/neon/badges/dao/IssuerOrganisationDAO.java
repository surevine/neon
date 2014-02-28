package com.surevine.neon.badges.dao;

import com.surevine.neon.badges.model.IssuerOrganisation;

public interface IssuerOrganisationDAO {

	public void persist(IssuerOrganisation toPersist);
	
	public IssuerOrganisation retrieve(String namespace);
	
}
