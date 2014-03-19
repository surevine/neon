package com.surevine.neon.service.impl;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.dao.SearchDAO;
import com.surevine.neon.dao.impl.SimpleIteratingSearchDAOIml;
import com.surevine.neon.inload.ImportRegistry;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.SkillBean;
import com.surevine.neon.service.SkillService;
import com.surevine.neon.service.bean.SkillServiceBean;

import java.util.Collection;

public class SkillServiceImpl implements SkillService {

	private SearchDAO searchDAO = new SimpleIteratingSearchDAOIml();
    private ProfileDAO profileDAO;
	
	public void setSearchDAO(SearchDAO searchDAO) {
		this.searchDAO = searchDAO;
	}

    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    @Override
	public Collection<ProfileBean> getUsersForSkill(String skillName, int minLevel) {
		return searchDAO.getPeopleBySkill(skillName, minLevel);
	}

    @Override
    public void addSkill(SkillServiceBean skillServiceBean) {
        if (skillServiceBean.getUserID() != null && skillServiceBean.getUserID().length() > 0 
                && skillServiceBean.getSkillName() != null && skillServiceBean.getSkillName().length() > 0
                && skillServiceBean.getRating()  > 0) {
            ProfileBean profileBean = new ProfileBean();
            profileBean.setUserID(skillServiceBean.getUserID());
            SkillBean skillBean = new SkillBean();
            skillBean.setRating(skillServiceBean.getRating());
            skillBean.setSkillName(skillServiceBean.getSkillName());
            profileBean.getSkills().add(skillBean);
            
            profileDAO.persistProfile(profileBean, ImportRegistry.getInstance().getInternalDataImporter());
        }
    }
}
