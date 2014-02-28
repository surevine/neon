package com.surevine.neon.badges.service.impl;

import java.net.MalformedURLException;

import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.dao.impl.RedisJSONBadgeClassDAOImpl;
import com.surevine.neon.badges.model.BadgeClass;
import com.surevine.neon.badges.service.BadgeClassService;

public class BadgeClassServiceImpl implements BadgeClassService {

	private BadgeClassDAO dao = new RedisJSONBadgeClassDAOImpl();
	
	public void setDao(BadgeClassDAO dao) {
		this.dao = dao;
	}

	@Override
	public String getJSONString(String namespace) {
		return dao.retrieve(namespace).toString();
	}

	@Override
	public void createBadgeClassFromJSON(String jsonString, String namespace) throws MalformedURLException {
		BadgeClass bc = new BadgeClass(jsonString, namespace);
		dao.persist(bc);
	}

}
