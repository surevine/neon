package com.surevine.neon.inload;

import java.util.Date;
import java.util.Map;

public class MockDataImporter implements DataImporter {
    private String importerName;
    private String namespace;
    private Map<String, String> configuration;
    private int timeout;
    private boolean enabled;
    private int sourcePriority;
    private Date lastRun;
    private boolean cacheLapsed;
    private boolean hasRun;
    
    @Override
    public String getImporterName() {
        return importerName;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public void setAdditionalConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setCacheTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setSourcePriority(int priority) {
        this.sourcePriority = priority;
    }

    @Override
    public void runImport() {
        lastRun = new Date();
        hasRun = true;
    }

    @Override
    public void runImport(String userID) {
        lastRun = new Date();
        hasRun = true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Date getLastRun() {
        return lastRun;
    }

    @Override
    public boolean cacheLapsed() {
        return cacheLapsed;
    }

    public void setCacheLapsed(boolean cacheLapsed) {
        this.cacheLapsed = cacheLapsed;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }

    public void setImporterName(String importerName) {
        this.importerName = importerName;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    
    public boolean isHasRun() {
        return hasRun;
    }

    public int getSourcePriority() {
        return sourcePriority;
    }
}
