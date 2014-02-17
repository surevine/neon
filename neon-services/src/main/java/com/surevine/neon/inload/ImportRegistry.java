package com.surevine.neon.inload;

import java.util.HashSet;
import java.util.Set;

/**
 * Spring configured singleton registry containing a list of data provides that contribute to a profile
 */
public class ImportRegistry {
    /**
     * Singleton instance
     */
    private static ImportRegistry instance;

    /**
     * Registry of importers
     */
    private Set<DataImporter> registry = new HashSet<>();

    /**
     * Private constructor to support singleton pattern
     */
    private ImportRegistry() {
        // no-op for singleton pattern
    }

    /**
     * Spring injected registry of importers
     * @param registry the registry
     */
    public void setRegistry(Set<DataImporter> registry) {
        this.registry = registry;
    }

    /**
     * Run data importers
     */              
    public void runImport() {
        for (DataImporter prov: this.registry) {
            prov.runImport();
        }
    }

    /**
     * Run data importers for a specified user
     * @param userID the userID
     */
    public void runImport(String userID) {
        for (DataImporter prov: this.registry) {
            prov.runImport(userID);
        }
    }
    
    /**
     * Gets the singleton instance
     * @return the singleton instance
     */
    public static ImportRegistry getInstance() {
        if (instance == null) {
            instance = new ImportRegistry();
        }
        return instance;
    }
}
