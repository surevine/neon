package com.surevine.neon.model;

import org.json.JSONObject;

/**
 * Bean for holding details of a user's expertise
 */
public class SkillBean implements NeonSerializableObject {
    public static final int SKILL_MENTOR = 5;
    public static final int SKILL_EXPERT = 4;
    public static final int SKILL_ARTISAN = 3;
    public static final int SKILL_JOURNEYMAN = 2;
    public static final int SKILL_BEGINNER = 1;
    
    private static final String JSONKEY_SKILLNAME = "skillName";
    private static final String JSONKEY_RATING = "rating";
    private static final String JSONKEY_INFERRED = "inferred";
    private static final String JSONKEY_DISAVOWED = "disavowed";
    
    private String skillName;
    private int rating;
    private boolean inferred;
    private boolean disavowed;

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
    	if (skillName==null) {
    		this.skillName=null;
    	}
    	else {
    		this.skillName = skillName.toLowerCase().trim();
    	}
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isInferred() {
        return inferred;
    }

    public void setInferred(boolean inferred) {
        this.inferred = inferred;
    }

    public boolean isDisavowed() {
        return disavowed;
    }

    public void setDisavowed(boolean disavowed) {
        this.disavowed = disavowed;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (!(o instanceof SkillBean)) {
    		return false;
    	}
    	return ((SkillBean)o).getSkillName().equals(skillName);
    }
    
    @Override
    public int hashCode() {
    	return skillName.hashCode();
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSONKEY_DISAVOWED, isDisavowed());
        jsonObject.put(JSONKEY_INFERRED, isInferred());
        jsonObject.put(JSONKEY_RATING, getRating());
        if (getSkillName() != null && !getSkillName().isEmpty()) {
            jsonObject.put(JSONKEY_SKILLNAME, getSkillName());
        }
        return jsonObject.toString();
    }

    @Override
    public void populateFromString(String serialisedData) {
        JSONObject jsonObject = new JSONObject(serialisedData);
        if (!jsonObject.isNull(JSONKEY_SKILLNAME)) {
            setSkillName(jsonObject.getString(JSONKEY_SKILLNAME));
        }
        if (!jsonObject.isNull(JSONKEY_RATING)) {
            setRating(jsonObject.getInt(JSONKEY_RATING));
        }
        if (!jsonObject.isNull(JSONKEY_INFERRED)) {
            setInferred(jsonObject.getBoolean(JSONKEY_INFERRED));
        }
        if (!jsonObject.isNull(JSONKEY_DISAVOWED)) {
            setDisavowed(jsonObject.getBoolean(JSONKEY_DISAVOWED));
        }
    }
}
