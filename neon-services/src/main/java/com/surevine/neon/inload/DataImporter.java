package com.surevine.neon.inload;

import java.util.Date;
import java.util.Map;

/**
 * Data providers provide some profile information.
 */
public interface DataImporter {
    /**
     * An importer MUST have a unique name to store its configuration without conflicts
     * @return a unique name
     */
    public String getImporterName();

    /**
     * The namespace this contributes to
     * @return the namespace
     */
    public String getNamespace();
    
    /**
     * Sets the configuration for this importer. It's held in the DB so can be edited at runtime but a restart will 
     * replace the configuration with the one configured in the spring configuration
     * @param configuration the configuration - usually loaded by Spring at application start
     */
    public void setAdditionalConfiguration(Map<String,String> configuration);
    
    /**
     * Sets the cache timeout
     * @param timeout the timeout in seconds
     */
    public void setCacheTimeout(int timeout);

    /**
     * Sets whether it's enabled or not
     * @param enabled whether it's enabled or not
     */
    public void setEnabled(boolean enabled);

    /**
     * Sets the priority of this importer's data where there is more than one source that contributed to the same data
     * @param priority the priority
     */
    public void setSourcePriority(int priority);
    
    /**
     * Tells the importer to run its data import for all users already in the system 
     */
    public void runImport();

    /**
     * Tells the importer to run its data import for the specified userID - mostly used for new users
     * @param userID the userID
     */
    public void runImport(String userID);

    /**
     * Is this enabled?
     * @return true if this is configured as enabled, false if not - this is controlled by the DB config which can 
     * be changed at runtime.
     */
    public boolean isEnabled();

    /**
     * Gets the time this was last run
     * @return the time this was last run
     */
    public Date getLastRun();

    /**
     * Return true if the cache is out of date
     * @return true if the cache is out of date
     */
    public boolean cacheLapsed();
}
