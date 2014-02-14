package com.surevine.neon.inload.importers;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;

import java.util.Map;
import java.util.Set;

/**
 * Mock importer that acts as an example and proof of concept for the importer framework
 */
public class BasicDetailsImporter implements DataImporter {
    private static final String IMPORTER_NAME = "VCARD_BASIC_DETAILS";
    private ImporterConfigurationDAO configurationDAO;
    private ProfileDAO profileDAO;
    
    @Override
    public String getImporterName() {
        return IMPORTER_NAME;
    }

    @Override
    public boolean providesForNamespace(String namespace) {
        return ProfileDAO.NS_BASIC_DETAILS.equals(namespace);
    }

    @Override
    public void setConfiguration(Map<String, String> configuration) {
        configurationDAO.configureImporter(IMPORTER_NAME, configuration);
    }

    @Override
    public void inload(String userID) {
        // here, for a real importer, we would head off to the service that supplies us with the data we need, retrieve
        // that data, parse it to produce name value pairs for storage, and pass the data to the profileDAO for 
        // persistence
    }

    @Override
    public void inload(Set<String> userIDs) {
        // here, for a real importer, we would head off to the service that supplies us with the data we need, retrieve
        // that data, parse it to produce name value pairs for storage, and pass the data to the profileDAO for 
        // persistence
    }

    @Override
    public boolean isEnabled() {
        return configurationDAO.getBooleanConfigurationOption(IMPORTER_NAME, "enabled");
    }

    public void setConfigurationDAO(ImporterConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }

    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }
}
