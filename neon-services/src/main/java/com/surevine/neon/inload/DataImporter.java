package com.surevine.neon.inload;

import java.util.Map;
import java.util.Set;

/**
 * Data providers provide some profile information as name:value pairs. They 
 * support one or more namespaces.
 */
public interface DataImporter {
    /**
     * An importer MUST have a unique name to store its configuration without conflicts
     * @return a unique name
     */
    public String getImporterName();

    /**
     * Whether or not this importer can get data related to the provided namespace
     * @param namespace the namespace
     * @return true if the importer can import data for the specified namespace, false if not
     */
    public boolean providesForNamespace(String namespace);

    /**
     * Sets the configuration for this importer. It's held in the DB so can be edited at runtime but a restart will 
     * replace the configuration with the one configured in the spring configuration
     * @param configuration the configuration - usually loaded by Spring at application start
     */
    public void setConfiguration(Map<String,String> configuration);

    /**
     * Tells the importer to run its data import for the specific user
     * @param userID the user to import
     */
    public void inload(String userID);

    /**
     * Tells the importer to run its data import for the specific users
     * @param userIDs the users
     */
    public void inload(Set<String> userIDs);

    /**
     * Is this enabled?
     * @return true if this is configured as enabled, false if not - this is controlled by the DB config which can 
     * be changed at runtime.
     */
    public boolean isEnabled();
}