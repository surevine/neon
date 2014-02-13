package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.SkillBean;

import java.util.Set;

public class ProfileDAOImpl implements ProfileDAO {
    @Override
    public ProfileBean getProfileForUser(String userID) {
        return getMockBean();
    }

    @Override
    public ProfileBean getPartialProfileForUser(String userID, Set<String> namespaces) {
        return getMockBean();
    }
    
    // mocking for now
    private ProfileBean getMockBean() {
        ProfileBean bean = new ProfileBean();
        SkillBean bean1 = new SkillBean();
        bean1.setSkillName("Java");
        bean1.setRating(SkillBean.SKILL_ARTISAN);
        bean.getSkills().add(bean1);

        SkillBean bean2 = new SkillBean();
        bean2.setSkillName("JavaScript");
        bean2.setRating(SkillBean.SKILL_BEGINNER);
        bean.getSkills().add(bean2);

        SkillBean bean3 = new SkillBean();
        bean3.setSkillName("Java");
        bean3.setRating(SkillBean.SKILL_JOURNEYMAN);
        bean3.setInferred(true);
        bean3.setDisavowed(true);
        bean.getSkills().add(bean3);

        // add some additional props
        bean.getAdditionalProperties().put("add1", "randomvalue1");
        bean.getAdditionalProperties().put("add2", "randomvalue2");
        bean.getAdditionalProperties().put("add3", "randomvalue3");
        return bean;
    }
}
