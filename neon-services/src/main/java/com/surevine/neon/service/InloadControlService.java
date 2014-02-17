package com.surevine.neon.service;

import java.util.List;
import java.util.Map;

/**
 * Inload control interface
 */
public interface InloadControlService {
    /**
     * Runs importers to import data about the specified userID
     * @param userID the userID of the user you want to import data for
     */
    public void runImport(String userID);

    /**
     * Gets the configuration for a specific named importer
     * @param importer
     * @return
     */
    public Map<String,String> getConfigForImporter(String importer);
}
