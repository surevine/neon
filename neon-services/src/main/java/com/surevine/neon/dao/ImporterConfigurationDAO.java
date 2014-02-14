package com.surevine.neon.dao;

import java.util.Map;

public interface ImporterConfigurationDAO {
    /**
     * Prefix namespace for name/value configuration options
     */
    public static final String NS_IMPORTER_PREFIX = "IMPORTER_CONFIG";

    /**
     * Stores importer configuration
     * @param importerName the name of the importer
     * @param config the configuration for the importer
     */
    public void configureImporter(String importerName, Map<String,String> config);

    /**
     * Gets the conifguration for a specific importer
     * @param importerName the importer name
     * @return the importer config
     */
    public Map<String,String> getConfigurationForImporter(String importerName);

    /**
     * Gets a single string configuration option
     * @param importerName the importer name
     * @param configurationKey  the key of the configuration option
     * @return the value of the option
     */
    public String getStringConfigurationOption(String importerName, String configurationKey);

    /**
     * Gets a single configuration option cast to a boolean
     * @param importerName the importer name
     * @param configurationKey  the key of the configuration option
     * @return true if the string value is "true" false otherwise
     */
    public boolean getBooleanConfigurationOption(String importerName, String configurationKey);

    /**
     * Sets or updates a config option for an importer
     * @param importerName the name of the importer
     * @param configurationKey the key we want to set or change
     * @param value the value for that configuration option
     */
    public void setConfigurationOption(String importerName, String configurationKey, String value);
}
