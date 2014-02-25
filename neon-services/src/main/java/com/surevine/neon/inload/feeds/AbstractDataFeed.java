package com.surevine.neon.inload.feeds;

import com.surevine.neon.dao.FeedConfigurationDAO;
import com.surevine.neon.inload.DataFeed;

import java.util.Map;

/**
 * Abstract data feed. Most feeds should use this as a superclass.
 */
public abstract class AbstractDataFeed implements DataFeed {
    private FeedConfigurationDAO configurationDAO;

    /**
     * Sets the configuration DAO instance
     * @param configurationDAO the configuration DAO
     */
    public void setConfigurationDAO(FeedConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }

    @Override
    public void setAdditionalConfiguration(Map<String, String> configuration) {
        configurationDAO.addFeedConfiguration(getFeedName(), configuration);
    }

    @Override
    public int getSourcePriority() {
        return Integer.parseInt(configurationDAO.getStringConfigurationOption(getFeedName(), FeedConfigurationDAO.NS_PRIORITY));
    }

    @Override
    public boolean isEnabled() {
        return configurationDAO.getBooleanConfigurationOption(getFeedName(), FeedConfigurationDAO.NS_ENABLED);
    }

    @Override
    public void setSourcePriority(int priority) {
        configurationDAO.addFeedConfigurationOption(getFeedName(), FeedConfigurationDAO.NS_PRIORITY, priority + "");
    }

    @Override
    public void setEnabled(boolean enabled) {
        configurationDAO.addFeedConfigurationOption(getFeedName(), FeedConfigurationDAO.NS_ENABLED, Boolean.toString(enabled));
    }
}
