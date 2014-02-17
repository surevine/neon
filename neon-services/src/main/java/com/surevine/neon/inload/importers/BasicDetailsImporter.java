package com.surevine.neon.inload.importers;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;

import java.util.Map;
import java.util.Set;

/**
 * Mock importer that acts as an example and proof of concept for the importer framework
 */
public class BasicDetailsImporter extends AbstractDataImporter implements DataImporter {
    private static final String IMPORTER_NAME = "VCARD_BASIC_DETAILS";
    private ImporterConfigurationDAO configurationDAO;
    private ProfileDAO profileDAO;
    
    @Override
    public String getImporterName() {
        return IMPORTER_NAME;
    }

    @Override
    public String getNamespace() {
        return ProfileDAO.NS_BASIC_DETAILS;
    }

    @Override
    protected void runImportImplementation(String userID) throws DataImportException {
        // no-op
    }
}
