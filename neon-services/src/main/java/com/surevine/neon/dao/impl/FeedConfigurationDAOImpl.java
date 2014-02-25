package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.FeedConfigurationDAO;
import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.util.Properties;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class FeedConfigurationDAOImpl implements FeedConfigurationDAO {
    private IPooledJedis jedis;
    private Logger logger = Logger.getLogger(FeedConfigurationDAOImpl.class);

    @Override
    public void addFeedConfiguration(String feedName, Map<String, String> config) {
        logger.debug("Adding " + config.size() + " configuration options for feed " + feedName);
        for (Map.Entry<String,String> configOption:config.entrySet()) {
            addFeedConfigurationOption(feedName, configOption.getKey(), configOption.getValue());
        }
    }

    @Override
    public void addFeedConfigurationOption(String feedName, String key, String value) {
        logger.debug("Adding feed configuration option " + key + " with value " + value + " for feed " + feedName);
        jedis.hset(Properties.getProperties().getSystemNamespace() + ":" + FeedConfigurationDAO.NS_FEED_PREFIX + ":" + feedName, key, value);
    }

    @Override
    public Map<String, String> getConfigurationForFeed(String feedName) {
        return jedis.hgetAll(Properties.getProperties().getSystemNamespace() + ":" + FeedConfigurationDAO.NS_FEED_PREFIX + ":" + feedName);
    }

    @Override
    public String getStringConfigurationOption(String feedName, String configurationKey) {
        return jedis.hget(Properties.getProperties().getSystemNamespace() + ":" + FeedConfigurationDAO.NS_FEED_PREFIX + ":" + feedName, configurationKey);
    }

    @Override
    public boolean getBooleanConfigurationOption(String feedName, String configurationKey) {
        return Boolean.valueOf(jedis.hget(Properties.getProperties().getSystemNamespace() + ":" + FeedConfigurationDAO.NS_FEED_PREFIX + ":" + feedName, configurationKey));
    }

    /**
     * Clears feed configuration - run when spring initialises this DAO.
     */
    public void clearFeedConfiguration() {
        logger.debug("Clearing feed configuration");
        Set<String> existingConfigurations = jedis.keys(Properties.getProperties().getSystemNamespace() + ":" + FeedConfigurationDAO.NS_FEED_PREFIX + ":*");
        if (existingConfigurations != null) {
            for (String feedConfigurationHashKey:existingConfigurations) {
                jedis.del(feedConfigurationHashKey);
            }
        }
    }

    public void setJedis(IPooledJedis jedis) {
        this.jedis = jedis;
    }
}
