package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;

/**
 * Handles persistence of any additional properties
 */
public class AdditionalPropertiesPersistenceHandler extends AbstractNamespaceHandler implements NamespaceHandler {
    @Override
    public String getNamespace() {
        return ProfileDAOImpl.NS_ADDITIONAL_PROPERTIES;
    }

    @Override
    public void persist(ProfileBean profile, DataImporter importer) {

    }

    @Override
    public void load(ProfileBean bean) {

    }
}
