package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.util.Properties;
import org.apache.log4j.Logger;

import java.util.Map;

public class ImporterConfigurationDAOImpl implements ImporterConfigurationDAO {
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
    }

    @Override
    public Map<String, String> getConfigurationForImporter(String importerName) {
        return jedis.hgetAll(Properties.getProperties().getSystemNamespace() + ":" + NS_IMPORTER_PREFIX + ":" + importerName);
    }

    @Override
    public String getStringConfigurationOption(String importerName, String configurationKey) {
        return jedis.hget(Properties.getProperties().getSystemNamespace() + ":" + NS_IMPORTER_PREFIX + ":" + importerName, configurationKey);
    }

    @Override
    public boolean getBooleanConfigurationOption(String importerName, String configurationKey) {
        return Boolean.valueOf(jedis.hget(Properties.getProperties().getSystemNamespace() + ":" + NS_IMPORTER_PREFIX + ":" + importerName, configurationKey));
    }

    public void setJedis(IPooledJedis jedis) {
        this.jedis = jedis;
    }
}
