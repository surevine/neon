package com.surevine.neon.model;

import java.util.*;

/**
 * Bean holding the details of a profile.
 */
public class ProfileBean {
    private String userID;
    private VCardBean vcard = new VCardBean();
    private Set<SkillBean> skills = new HashSet<SkillBean>();
    private List<ActivityBean> activityStream = new ArrayList<>();
    private Map<String,String> additionalProperties = new HashMap<String,String>();
    private StatusBean status = new StatusBean();
    private String bio; // needs a priority for multiple importers

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Set<SkillBean> getSkills() {
        return skills;
    }

    protected void setSkills(Set<SkillBean> skills) {
        this.skills = skills;
    }
    
    public void addOrUpdateSkill(SkillBean skill) {
    	if (hasSkill(skill)) {
    		skills.remove(skill);
    	}
    	skills.add(skill);
    }
    
    public boolean hasSkill(SkillBean skill) {
    	return skills.contains(skill);
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
    
    public void setAdditionalProperty(String propertyName, String propertyValue) {
    	if (propertyValue==null || propertyValue.trim().equals("")) { //If we only have a blank or null value, don't record anything
    		return;
    	}
    	this.additionalProperties.put(propertyName, propertyValue);
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public VCardBean getVcard() {
        return vcard;
    }

    public void setVcard(VCardBean vcard) {
        this.vcard = vcard;
    }

    public List<ActivityBean> getActivityStream() {
        return activityStream;
    }

    public void setActivityStream(List<ActivityBean> activityStream) {
        this.activityStream = activityStream;
    }
}
