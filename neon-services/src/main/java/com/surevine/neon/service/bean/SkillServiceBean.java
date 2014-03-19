package com.surevine.neon.service.bean;

/**
 * Contains the details required to add a skill to a profile.
 */
public class SkillServiceBean {
    private String userID;
    private int rating;
    private String skillName;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}
