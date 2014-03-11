package com.surevine.neon.dao;

import java.net.URL;
import java.util.Collection;

import com.surevine.neon.model.ProfileBean;


public interface SearchDAO {
	
	public Collection<ProfileBean> getPeopleBySkill(String skillName, int minLevel);
	
	public Collection<ProfileBean> getPeopleByBadge(String namespace);

}
