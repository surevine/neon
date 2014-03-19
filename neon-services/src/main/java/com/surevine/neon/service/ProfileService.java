package com.surevine.neon.service;

import com.surevine.neon.model.ProfileBean;

import java.util.Collection;

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

    /**
     * Adds a user to the profile service
     * @param userID the userID
     */
    public void addUser(String userID);

    /**
     * Removes a user from the profile service
     * @param userID the userID
     */
    public void removeUser(String userID);

    /**
     * Gets all userIDs in the system
     * @return all userIDs in the system
     */
    public Collection<String> getAllUserIDs();
}
