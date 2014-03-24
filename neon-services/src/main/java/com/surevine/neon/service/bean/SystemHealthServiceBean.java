package com.surevine.neon.service.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Holds information about the status of the system.
 */
public class SystemHealthServiceBean {
    private Collection<ImporterConfigurationServiceBean> importers = new HashSet<ImporterConfigurationServiceBean>();
    private Map<String,String> systemProperties = new HashMap<String, String>();

    public Collection<ImporterConfigurationServiceBean> getImporters() {
        return importers;
    }

    public void setImporters(Collection<ImporterConfigurationServiceBean> importers) {
        this.importers = importers;
    }

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Map<String, String> systemProperties) {
        this.systemProperties = systemProperties;
    }
}
