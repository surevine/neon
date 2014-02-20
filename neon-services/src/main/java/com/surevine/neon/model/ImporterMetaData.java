package com.surevine.neon.model;

import java.util.Date;

/**
 * Stores metadata about the various sources that contributed to a profile.
 */
public class ImporterMetaData {
    private String sourceName;
    private Date lastImport;
    private int sourcePriority;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Date getLastImport() {
        return lastImport;
    }

    public void setLastImport(Date lastImport) {
        this.lastImport = lastImport;
    }

    public int getSourcePriority() {
        return sourcePriority;
    }

    public void setSourcePriority(int sourcePriority) {
        this.sourcePriority = sourcePriority;
    }
}
