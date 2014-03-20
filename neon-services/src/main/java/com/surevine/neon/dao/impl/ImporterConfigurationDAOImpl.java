package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.util.DateUtil;
import com.surevine.neon.util.Properties;
import org.apache.log4j.Logger;

import java.util.*;

public class ImporterConfigurationDAOImpl implements ImporterConfigurationDAO {
    private static final String LAST_CONFIG_CHANGE = "LAST_CONFIG_CHANGE";
    private IPooledJedis jedis;
    private Logger logger = Logger.getLogger(ImporterConfigurationDAO.class);
    
    @Override
    public void addImporterConfiguration(String importerName, Map<String, String> config) {
        logger.debug("Adding " + config.size() + " configuration options for importer " + importerName);
        for (Map.Entry<String,String> configOption:config.entrySet()) {
            addImporterConfigurationOption(importerName, configOption.getKey(), configOption.getValue());
        }
    }

    @Override
    public void addImporterConfigurationOption(String importerName, String key, String value) {
        logger.debug("Adding importer configuration option " + key + " with value " + value + " for importer " + importerName);
        jedis.hset(Properties.getProperties().getSystemNamespace() + ":" + NS_IMPORTER_PREFIX + ":" + importerName, key, value);
        jedis.hset(Properties.getProperties().getSystemNamespace() + ":" + NS_IMPORTER_PREFIX + ":" + importerName, LAST_CONFIG_CHANGE, DateUtil.dateToString(new Date()));
    }

    @Override
    public Map<String, String> getConfigurationForImporter(String importerName) {
        return jedis.hgetAll(Properties.getProperties().getSystemNamespace() + ":" + NS_IMPORTER_PREFIX + ":" + importerName);
    }

    @Override
    public Map<String, Map<String, String>> getConfigurationForImporters() {
        Set<String> impKeys = jedis.keys(Properties.getProperties().getSystemNamespace() + ":" + NS_IMPORTER_PREFIX + ":*");
        Map<String, Map<String,String>> configs = new HashMap<String, Map<String, String>>();
        for (String key:impKeys) {
            String importerName = key.substring(key.lastIndexOf(":") + 1);
            configs.put(importerName, jedis.hgetAll(key));
        }
        return configs;
    }

    @Override
    public String getStringConfigurationOption(String importerName, String configurationKey) {
        return jedis.hget(Properties.getProperties().getSystemNamespace() + ":" + NS_IMPORTER_PREFIX + ":" + importerName, configurationKey);
    }

    @Override
    public boolean getBooleanConfigurationOption(String importerName, String configurationKey) {
        return Boolean.valueOf(jedis.hget(Properties.getProperties().getSystemNamespace() + ":" + NS_IMPORTER_PREFIX + ":" + importerName, configurationKey));
    }

    @Override
    public void clearImporterConfiguration(String importerName) {
        logger.trace("Clearing old importer configuration for " + importerName);
        Set<String> existingConfigurations = jedis.keys(Properties.getProperties().getSystemNamespace() + ":" + ImporterConfigurationDAO.NS_IMPORTER_PREFIX + ":" + importerName);
        if (existingConfigurations != null) {
            for (String importerConfigurationHashKey:existingConfigurations) {
                jedis.del(importerConfigurationHashKey);
            }
        }
        jedis.hset(Properties.getProperties().getSystemNamespace() + ":" + NS_IMPORTER_PREFIX + ":" + importerName, LAST_CONFIG_CHANGE, DateUtil.dateToString(new Date()));
    }

    public void setJedis(IPooledJedis jedis) {
        this.jedis = jedis;
    }
}
