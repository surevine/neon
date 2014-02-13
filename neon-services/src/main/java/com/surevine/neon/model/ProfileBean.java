package com.surevine.neon.model;

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
}
