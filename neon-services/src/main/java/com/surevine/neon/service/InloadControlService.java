package com.surevine.neon.service;

import com.surevine.neon.service.bean.ImporterConfigurationServiceBean;

import java.util.Collection;
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
     * @return configuration bean with settings for the named importer
     */
    public ImporterConfigurationServiceBean getConfigForImporter(String importer);

    /**
     * Gets the configuration for all importers
     * @return the configuration for all importers
     */
    public Collection<ImporterConfigurationServiceBean> getAllImporterConfigurations();

    /**
     * Updates or sets configuration options for an importer. Does not remove any existing config - can only add or 
     * override.
     * @param configurationBean the configuration
     */
    public void setConfiguration(ImporterConfigurationServiceBean configurationBean);
}
