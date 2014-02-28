package com.surevine.neon.badges.service.impl;

import com.surevine.neon.badges.dao.RevocationListDAO;
import com.surevine.neon.badges.dao.impl.RedisJSONRevocationListDAOImpl;
import com.surevine.neon.badges.model.RevocationList;
import com.surevine.neon.badges.service.RevocationListService;

public class RevocationListServiceImpl implements RevocationListService {

	private RevocationListDAO dao = new RedisJSONRevocationListDAOImpl();
	
	public void setDao(RevocationListDAO dao) {
		this.dao = dao;
	}

	@Override
	public String getJSONString(String namespace) {
		return dao.retrieve(namespace).toJSON().toString();
	}

	@Override
	public void createRevocationListFromJSON(String json, String namespace) {
		dao.persist(new RevocationList(json, namespace), namespace);
	}

	@Override
	public void revokeBage(String namespace, String uid, String reason) {
		RevocationList existing = dao.retrieve(namespace);
		existing.addToCRL(uid, reason);
		dao.persist(existing, namespace);
	}

}
