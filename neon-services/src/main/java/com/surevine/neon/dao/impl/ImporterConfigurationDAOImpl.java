package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.redis.PooledJedis;

import java.util.Map;

public class ImporterConfigurationDAOImpl implements ImporterConfigurationDAO {
    @Override
    public void configureImporter(String importerName, Map<String, String> config) {
        for (Map.Entry<String,String> configOption:config.entrySet()) {
            PooledJedis.get().hset(NS_IMPORTER_PREFIX + ":" + importerName, configOption.getKey(), configOption.getValue());
        }
    }

    @Override
    public Map<String, String> getConfigurationForImporter(String importerName) {
        return PooledJedis.get().hgetAll(NS_IMPORTER_PREFIX + ":" + importerName);
    }

    @Override
    public String getStringConfigurationOption(String importerName, String configurationKey) {
        return PooledJedis.get().hget(NS_IMPORTER_PREFIX + ":" + importerName, configurationKey);
    }

    @Override
    public boolean getBooleanConfigurationOption(String importerName, String configurationKey) {
        return Boolean.valueOf(PooledJedis.get().hget(NS_IMPORTER_PREFIX + ":" + importerName, configurationKey));
    }

    @Override
    public void setConfigurationOption(String importerName, String configurationKey, String value) {
        PooledJedis.get().hset(NS_IMPORTER_PREFIX + ":" + importerName, configurationKey, value);
    }
}
