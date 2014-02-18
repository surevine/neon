package com.surevine.neon.service.impl;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.inload.ImportRegistry;
import com.surevine.neon.service.InloadControlService;
import com.surevine.neon.service.bean.ImporterConfigurationServiceBean;

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
    public ImporterConfigurationServiceBean getConfigForImporter(String importer) {
        Map<String,String> config = importerConfigurationDAO.getConfigurationForImporter(importer);
        ImporterConfigurationServiceBean bean = new ImporterConfigurationServiceBean();
        bean.setConfiguration(config);
        bean.setImporterName(importer);
        return bean;
    }

    public void setRegistry(ImportRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void setConfiguration(ImporterConfigurationServiceBean configurationBean) {
        importerConfigurationDAO.addImporterConfiguration(configurationBean.getImporterName(), configurationBean.getConfiguration());
    }

    public void setImporterConfigurationDAO(ImporterConfigurationDAO importerConfigurationDAO) {
        this.importerConfigurationDAO = importerConfigurationDAO;
    }
}
