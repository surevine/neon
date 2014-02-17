package com.surevine.neon.inload;

import com.surevine.neon.dao.ProfileDAO;

import java.util.HashSet;
import java.util.Set;

/**
 * Spring configured singleton registry containing a list of data provides that contribute to a profile
 */
public class ImportRegistry {
    /**
     * The profile DAO
     */
    private ProfileDAO dao;
    
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
     * Run all importers that support the specific namespace for the given userID
     * @param userID the userID to import data for
     * @param namespace the namespace to import data for
     */
    public void runImport(String userID, String namespace) {
        for (DataImporter prov: this.registry) {
            if (prov.providesForNamespace(namespace)) {
                prov.inload(userID);
            }
        }
    }

    /**
     * Run all importers for the given userID
     * @param userID the userID to import data for
     */              
    public void runImport(String userID) {
        for (DataImporter prov: this.registry) {
            prov.inload(userID);
        }
    }
    
    public void runScheduledImportForAllUsers() {
        Set<String> users = 
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
