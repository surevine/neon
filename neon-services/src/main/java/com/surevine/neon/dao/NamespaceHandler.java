package com.surevine.neon.dao;

import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;

/**
 * Delegate for CRUD operations related to a specific profile namespace
 */
public interface NamespaceHandler {
    /**
     * Provides the namespace handled by this handler
     * @return the namespace
     */
    public String getNamespace();
    
    /**
     * Persists profile data
     * @param profile the profile bean
     * @param importer the source importer
     */
    public void persist(ProfileBean profile, DataImporter importer);

    /**
     * Populates the profile bean with any fields persisted in its namespace for the given user. Where multiple 
     * fields are found uses the highest priority version of that field.
     * @param bean the bean to populate - must have the userID set.
     */
    public void load(ProfileBean bean);
}
