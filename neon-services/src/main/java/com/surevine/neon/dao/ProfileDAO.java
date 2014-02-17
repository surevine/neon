package com.surevine.neon.dao;

import com.surevine.neon.inload.DataImporter;
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
     * Namespace key for basic details
     */
    public static final String NS_PROJECT_DETAILS = "PROJECT";


    /**
     * Gets a profile for a specific user
     * @param userID the userID of the user whose profile you want
     * @return a populated ProfileBean for the user
     */
    public ProfileBean getProfileForUser(String userID);

    /**
     * Gets the set of userIDs we want profiles for in the system
     * @return the userIDs
     */
    public Set<String> getUserIDList();

    /**
     * Persists a portion of a profile bean contributed to by the argument data importer. The userID must exist.
     * @param profile the partially populated profile bean
     * @param importer the importer that contributed to the profile bean
     */
    public void persistProfile(ProfileBean profile, DataImporter importer);
}
