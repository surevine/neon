package com.surevine.neon.model;

/**
 * Bean for holding details of a user's expertise
 */
public class SkillBean {
    public static final int SKILL_MENTOR = 5;
    public static final int SKILL_EXPERT = 4;
    public static final int SKILL_ARTISAN = 3;
    public static final int SKILL_JOURNEYMAN = 2;
    public static final int SKILL_BEGINNER = 1;
    
    private String skillName;
    private int rating;
    private boolean inferred;
    private boolean disavowed;

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
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
}
