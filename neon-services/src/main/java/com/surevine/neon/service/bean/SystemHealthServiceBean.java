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
    private Collection<UserSummaryServiceBean> users = new HashSet<UserSummaryServiceBean>();
    private Map<String,String> systemProperties = new HashMap<String, String>();

    public Collection<ImporterConfigurationServiceBean> getImporters() {
        return importers;
    }

    public void setImporters(Collection<ImporterConfigurationServiceBean> importers) {
        this.importers = importers;
    }

    public Collection<UserSummaryServiceBean> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserSummaryServiceBean> users) {
        this.users = users;
    }

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Map<String, String> systemProperties) {
        this.systemProperties = systemProperties;
    }
}
