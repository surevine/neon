package com.surevine.neon.model;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Bean holding the details of a profile.
 */
public class ProfileBean {
    private String userID;
    private Set<SkillBean> skills = new HashSet<SkillBean>();
    private Map<String,String> additionalProperties = new HashMap<String,String>();
    private URL profileImage;
    private String name;
    private String teamName;
    
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
    	this.additionalProperties.put(propertyName, propertyValue);
    }

	public URL getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(URL profileImage) {
		this.profileImage = profileImage;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getTeamName() {
		return teamName;
	}


	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
    
}
