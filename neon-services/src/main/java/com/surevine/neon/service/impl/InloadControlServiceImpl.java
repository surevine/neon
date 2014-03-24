package com.surevine.neon.service.impl;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.inload.ImportRegistry;
import com.surevine.neon.service.InloadControlService;
import com.surevine.neon.service.bean.ImporterConfigurationServiceBean;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    @Override
    public Collection<ImporterConfigurationServiceBean> getAllImporterConfigurations() {
        Set<ImporterConfigurationServiceBean> configBeans = new HashSet<ImporterConfigurationServiceBean>();
        Map<String, Map<String,String>> configMaps = importerConfigurationDAO.getConfigurationForImporters();
        for (Map.Entry<String, Map<String,String>> entry:configMaps.entrySet()) {
            ImporterConfigurationServiceBean bean = new ImporterConfigurationServiceBean();
            bean.setImporterName(entry.getKey());
            bean.setConfiguration(entry.getValue());
            configBeans.add(bean);
        }
        return configBeans;
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
