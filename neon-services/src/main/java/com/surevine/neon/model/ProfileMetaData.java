package com.surevine.neon.model;

import java.util.Date;

/**
 * Stores metadata about the various sources that contributed to a profile.
 * Experimental - depends a lot on the data structure in redis.
 */
public class ProfileMetaData {
    private int highestPriorityNamespaceProvider;
    private String namespace;
    private String sourceName;
    private Date dataAge;

    public int getHighestPriorityNamespaceProvider() {
        return highestPriorityNamespaceProvider;
    }

    public void setHighestPriorityNamespaceProvider(int highestPriorityNamespaceProvider) {
        this.highestPriorityNamespaceProvider = highestPriorityNamespaceProvider;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Date getDataAge() {
        return dataAge;
    }

    public void setDataAge(Date dataAge) {
        this.dataAge = dataAge;
    }
}
