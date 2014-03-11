package com.surevine.neon.badges.service.impl;

import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.model.BadgeClass;
import com.surevine.neon.badges.service.BadgeClassService;
import com.surevine.neon.dao.SearchDAO;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.util.SpringApplicationContext;

import java.net.MalformedURLException;
import java.util.Collection;

public class BadgeClassServiceImpl implements BadgeClassService {

	private BadgeClassDAO dao;
	private SearchDAO search;
	
	public void setSearch(SearchDAO search) {
		this.search = search;
	}

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

	@Override
	public Collection<ProfileBean> getUsersForBadge(String namespace) {
		if (search==null) {
			loadSearchFromContext();
		}
		return search.getPeopleByBadge(namespace);
	}
	
	private void loadSearchFromContext() {
        search = (SearchDAO) SpringApplicationContext.getBean("searchDAO");

	}

}
