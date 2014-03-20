package com.surevine.neon.dao;

import java.util.Collection;
import java.util.Map;

public interface ImporterConfigurationDAO {
    public static final String NS_IMPORTER_PREFIX = "IMPORTER_CONFIG";
    public static final String NS_IMPORTER_TIMEOUT = "CACHE_TIMEOUT_SECONDS";
    public static final String NS_SUPPORTER_NAMESPACE = "DATA_NAMESPACE";
    public static final String NS_ENABLED = "ENABLED";
    public static final String NS_PRIORITY = "PRIORITY";
    public static final String NS_LAST_IMPORT = "LAST_IMPORT";
    
    /**
     * Stores importer configuration
     * @param importerName the name of the importer
     * @param config the configuration for the importer
     */
    public void addImporterConfiguration(String importerName, Map<String,String> config);

    /**
     * Stores a single configuration item
     * @param importerName the name of the importer
     * @param key the key of the configuration item
     * @param value the value of the configuration item
     */
    public void addImporterConfigurationOption(String importerName, String key, String value);

    /**
     * Gets the configuration for a specific importer
     * @param importerName the importer name
     * @return the importer config
     */
    public Map<String,String> getConfigurationForImporter(String importerName);

    /**
     * Gets the configuration for all importers
     * @return the importer config for all importers as Map<"importer name", Map<"config option","config value">>
     */
    public Map<String, Map<String,String>> getConfigurationForImporters();
    
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
     * Clears an importer configuration from the system
     * @param importerName the name of the importer
     */
    public void clearImporterConfiguration(String importerName);
}
