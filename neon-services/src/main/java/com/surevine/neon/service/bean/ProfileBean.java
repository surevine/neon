package com.surevine.neon.service.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Bean holding a profile.
 */
public class ProfileBean {
    private String userID;
    private Set<SkillBean> skills = new HashSet<SkillBean>();
    private Map<String,String> additionalProperties = new HashMap<String,String>();
    
    public ProfileBean() {
        populateMock();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Set<SkillBean> getSkills() {
        return skills;
    }

    public void setSkills(Set<SkillBean> skills) {
        this.skills = skills;
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
    
    // captain placeholder
    private void populateMock() {
        // mocking up the bean for now
        SkillBean bean1 = new SkillBean();
        bean1.setSkillName("Java");
        bean1.setRating(SkillBean.SKILL_ARTISAN);
        skills.add(bean1);

        SkillBean bean2 = new SkillBean();
        bean2.setSkillName("JavaScript");
        bean2.setRating(SkillBean.SKILL_BEGINNER);
        skills.add(bean2);

        SkillBean bean3 = new SkillBean();
        bean3.setSkillName("Java");
        bean3.setRating(SkillBean.SKILL_JOURNEYMAN);
        bean3.setInferred(true);
        bean3.setDisavowed(true);
        skills.add(bean3);

        // add some additional props
        additionalProperties.put("randomproperty1", "randomvalue1");
        additionalProperties.put("randomproperty2", "randomvalue2");
        additionalProperties.put("randomproperty3", "randomvalue3");
    }
}
