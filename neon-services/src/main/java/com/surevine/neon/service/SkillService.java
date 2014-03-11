package com.surevine.neon.service;

import java.util.Collection;
import com.surevine.neon.model.ProfileBean;

public interface SkillService {

	public Collection<ProfileBean> getUsersForSkill(String skillName, int minLevel);
}
