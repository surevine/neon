package com.surevine.neon.inload;

import com.surevine.neon.model.ProfileBean;

import java.util.Map;

/**
 * Augments / supplements a profile with a live data feed
 */
public interface DataFeed {
    /**
     * A feed MUST have a unique name to store its configuration without conflicts
     * @return a unique name
     */
    public String getFeedName();

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
     * Sets whether it's enabled or not
     * @param enabled whether it's enabled or not
     */
    public void setEnabled(boolean enabled);

    /**
     * Is this enabled?
     * @return true if enabled, false if not
     */
    public boolean isEnabled();

    /**
     * Sets the priority of this importer's data where there is more than one source that contributed to the same data
     * @param priority the priority
     */
    public void setSourcePriority(int priority);

    /**
     * Gets the source priority
     * @return the source priority
     */
    public int getSourcePriority();
    
    /**
     * Augments or contributes to a profile bean
     * @param profileBean the profile bean
     */
    public void contributeToProfile(final ProfileBean profileBean);
    
}
