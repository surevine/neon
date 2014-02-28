package com.surevine.neon.badges.service.impl;

import java.net.MalformedURLException;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.dao.impl.RedisJSONBadgeAssertionDAOImpl;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.service.BadgeAssertionService;

public class BadgeAssertionServiceImpl implements BadgeAssertionService {

	private BadgeAssertionDAO dao = new RedisJSONBadgeAssertionDAOImpl();
	
	public void setDao(BadgeAssertionDAO dao) {
		this.dao = dao;
	}

	@Override
	public String getJSONString(String namespace) {
		return dao.retrieve(namespace).toString();
	}

	@Override
	public void createBadgeAssertionFromJSON(String jsonString, String namespace) throws MalformedURLException {
		dao.persist(new BadgeAssertion(jsonString, namespace));

	}

}
