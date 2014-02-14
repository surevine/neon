package com.surevine.neon.dao;

import com.surevine.neon.model.ProfileBean;

import java.util.Set;

/**
 * Interface for the profile DAO
 */
public interface ProfileDAO {
    /**
     * The prefix for all profile related keys
     */
    public static final String NS_PROFILE_PREFIX = "PROFILE";

    /**
     * Namespace key for basic details
     */
    public static final String NS_BASIC_DETAILS = "BASIC";

    /**
     * Gets a profile for a specific user
     * @param userID the userID of the user whose profile you want
     * @return a populated ProfileBean for the user
     */
    public ProfileBean getProfileForUser(String userID);
}
