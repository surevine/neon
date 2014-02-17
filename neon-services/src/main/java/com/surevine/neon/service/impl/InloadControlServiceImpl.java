package com.surevine.neon.service.impl;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.inload.ImportRegistry;
import com.surevine.neon.service.InloadControlService;

import java.util.Map;

/**
 * Service implementation for control data importers via the registry.
 */
public class InloadControlServiceImpl implements InloadControlService {
    private ImportRegistry registry;
    private ImporterConfigurationDAO importerConfigurationDAO;
    
    @Override
    public void runImport(String userID) {
        registry.runImport(userID);
    }

    @Override
    public Map<String,String> getConfigForImporter(String importer) {
        return importerConfigurationDAO.getConfigurationForImporter(importer);
    }

    public void setRegistry(ImportRegistry registry) {
        this.registry = registry;
    }

    public void setImporterConfigurationDAO(ImporterConfigurationDAO importerConfigurationDAO) {
        this.importerConfigurationDAO = importerConfigurationDAO;
    }
}
