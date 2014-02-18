package com.surevine.neon.inload;

import com.surevine.neon.model.ProfileBean;

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
     * Registry of data feeds
     */
    private Set<DataFeed> registry = new HashSet<>();

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
