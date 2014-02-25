package com.surevine.neon.dao;

import java.util.Map;

public interface FeedConfigurationDAO {
    public static final String NS_FEED_PREFIX = "FEED_CONFIG";
    public static final String NS_ENABLED = "ENABLED";
    public static final String NS_PRIORITY = "PRIORITY";

    /**
     * Stores feed configuration
     * @param feedName the name of the feed
     * @param config the configuration for the feed
     */
    public void addFeedConfiguration(String feedName, Map<String,String> config);

    /**
     * Stores a single configuration item
     * @param feedName the name of the feed
     * @param key the key of the configuration item
     * @param value the value of the configuration item
     */
    public void addFeedConfigurationOption(String feedName, String key, String value);

    /**
     * Gets the configuration for a specific feed
     * @param feedName the feed name
     * @return the feed config
     */
    public Map<String,String> getConfigurationForFeed(String feedName);

    /**
     * Gets a single string configuration option
     * @param feedName the feed name
     * @param configurationKey  the key of the configuration option
     * @return the value of the option
     */
    public String getStringConfigurationOption(String feedName, String configurationKey);

    /**
     * Gets a single configuration option cast to a boolean
     * @param feedName the feed name
     * @param configurationKey  the key of the configuration option
     * @return true if the string value is "true" false otherwise
     */
    public boolean getBooleanConfigurationOption(String feedName, String configurationKey);
}
