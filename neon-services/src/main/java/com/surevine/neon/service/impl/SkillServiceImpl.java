package com.surevine.neon.service.impl;

import java.util.Collection;


import com.surevine.neon.dao.SearchDAO;
import com.surevine.neon.dao.impl.SimpleIteratingSearchDAOIml;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.service.SkillService;

public class SkillServiceImpl implements SkillService {

	private SearchDAO searchDAO = new SimpleIteratingSearchDAOIml();
	
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}

	@Override
	public Collection<ProfileBean> getUsersForSkill(String skillName, int minLevel) {
		return searchDAO.getPeopleBySkill(skillName, minLevel);
	}

}
