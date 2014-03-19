package com.surevine.neon.inload;

import com.surevine.neon.dao.ProfileDAO;

import java.util.Date;
import java.util.Map;

final class InternalImporter implements DataImporter {
    protected String[] supportedNamespaces= { ProfileDAO.NS_BASIC_DETAILS, ProfileDAO.NS_CONNECTIONS, ProfileDAO.NS_PROJECT_DETAILS, ProfileDAO.NS_ACTIVITY, ProfileDAO.NS_SKILLS, ProfileDAO.NS_ADDITIONAL_PROPERTIES, ProfileDAO.NS_STATUS };

    InternalImporter() {
        // default access to prevent external instantiation
    }
    
    @Override
    public String getImporterName() {
        return "NEON Internal";
    }

    @Override
    public String[] getSupportedNamespaces() {
        return supportedNamespaces;
    }

    @Override
    public void setAdditionalConfiguration(Map<String, String> configuration) {
        throw new UnsupportedOperationException("The internal importer cannot be configured");
    }

    @Override
    public void setCacheTimeout(int timeout) {
        throw new UnsupportedOperationException("The internal importer cannot be configured");
    }

    @Override
    public void setEnabled(boolean enabled) {
        throw new UnsupportedOperationException("The internal importer cannot be configured");
    }

    @Override
    public void setSourcePriority(int priority) {
        throw new UnsupportedOperationException("The internal importer cannot be configured");
    }

    @Override
    public void runImport() {
        throw new UnsupportedOperationException("The internal importer doesn't import any data");
    }

    @Override
    public void runImport(String userID) {
        throw new UnsupportedOperationException("The internal importer doesn't import any data");
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Date getLastRun() {
        throw new UnsupportedOperationException("The internal importer doesn't import any data");
    }

    @Override
    public boolean cacheLapsed() {
        throw new UnsupportedOperationException("The internal importer doesn't import any data");
    }

    @Override
    public int getSourcePriority() {
        return 0;
    }

    @Override
    public void updateConfiguration() {
        throw new UnsupportedOperationException("The internal importer cannot be configured");
    }
}
