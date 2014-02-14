package com.surevine.neon.service;

import com.surevine.neon.model.ProfileBean;

/**
 * Profile service interface
 */
public interface ProfileService {
    /**
     * Gets a full profile for the specific userID
     * @param userID the userID of the person you want the profile of
     * @return a ProfileBean instance containing the profile information
     */
    public ProfileBean getProfile(String userID);
}
