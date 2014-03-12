package com.surevine.neon.inload.importers;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.VCardTelBean;
import org.apache.log4j.Logger;
import java.net.URL;

/**
 * Mock importer that acts as an example and proof of concept for the importer framework
 */
public class MockImporter extends AbstractDataImporter implements DataImporter {
    Logger logger = Logger.getLogger(MockImporter.class);
    
    private static final String IMPORTER_NAME = "MOCK_IMPORTER";
    protected String[] supportedNamespaces= { ProfileDAO.NS_BASIC_DETAILS, ProfileDAO.NS_CONNECTIONS, ProfileDAO.NS_PROJECT_DETAILS, ProfileDAO.NS_ACTIVITY, ProfileDAO.NS_SKILLS, ProfileDAO.NS_ADDITIONAL_PROPERTIES, ProfileDAO.NS_STATUS };
    
    @Override
    public String getImporterName() {
        return IMPORTER_NAME;
    }

    @Override
    public String[] getSupportedNamespaces() {
        return supportedNamespaces;
    }

    @Override
    protected void runImportImplementation(String userID) throws DataImportException {
        // no-op
    }
}
