package com.surevine.neon.service;

import java.util.Collection;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.SkillBean;
import com.surevine.neon.service.bean.SkillServiceBean;

public interface SkillService {

	public Collection<ProfileBean> getUsersForSkill(String skillName, int minLevel);

    /**
     * Adds a skill for a user
     * @param skillBean the skill
     */
    public void addSkill(SkillServiceBean skillBean);
}
