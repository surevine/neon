package com.surevine.neon.service.bean;

import java.util.Map;

/**
 * Bean used for importer confirmation JSON in and out of the system
 */
public class ImporterConfigurationServiceBean {
    private String importerName;
    private Map<String,String> configuration;

    public String getImporterName() {
        return importerName;
    }

    public void setImporterName(String importerName) {
        this.importerName = importerName;
    }

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }
}
