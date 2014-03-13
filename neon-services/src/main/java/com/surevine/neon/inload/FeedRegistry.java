package com.surevine.neon.inload;

import com.surevine.neon.model.ProfileBean;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Spring configured singleton registry containing a list of data feeds that can contribute to a profile
 */
public class FeedRegistry {
    /**
     * Singleton instance
     */
    private static FeedRegistry instance;

    /**
     * logger
     */
    private Logger logger = Logger.getLogger(FeedRegistry.class);

    /**
     * Registry of data feeds
     */
    private Set<DataFeed> registry = new HashSet<DataFeed>();

    /**
     * Private constructor to support singleton pattern
     */
    private FeedRegistry() {
        // no-op for singleton pattern
    }

    /**
     * Spring injected registry of data feeds
     * @param registry the registry
     */
    public void setRegistry(Set<DataFeed> registry) {
        this.registry = registry;
    }

    /**
     * Runs data feeds to augment a ProfileBean. The bean must have the userID set
     * @param profileBean the profile bean
     */
    public void augmentProfileWithFeeds(final ProfileBean profileBean) {
        logger.info("Augmenting profile data for " + profileBean.getUserID() + " using " + registry.size() + " live data feed(s)");
        for (DataFeed feed:registry) {
            if (feed.isEnabled()) {
                feed.contributeToProfile(profileBean);
            }
        }
    }

    /**
     * Gets the singleton instance
     * @return the singleton instance
     */
    public static FeedRegistry getInstance() {
        if (instance == null) {
            instance = new FeedRegistry();
        }
        return instance;
    }
}
