package com.surevine.neon.model;

/**
 * Stores metadata about the various sources that contributed to a profile.
 */
public class ImporterMetaData {
    private String sourceName;
    private String lastImport;
    private int sourcePriority;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getLastImport() {
        return lastImport;
    }

    public void setLastImport(String lastImport) {
        this.lastImport = lastImport;
    }

    public int getSourcePriority() {
        return sourcePriority;
    }

    public void setSourcePriority(int sourcePriority) {
        this.sourcePriority = sourcePriority;
    }
}
