package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;

/**
 * Handles persistence of NS_PROJECT_DETAILS fields
 */
public class ProjectPersistenceHandler extends AbstractNamespaceHandler implements NamespaceHandler {
    @Override
    public String getNamespace() {
        return null;
    }

    @Override
    public void persist(ProfileBean profile, DataImporter importer) {

    }

    @Override
    public void load(ProfileBean bean) {

    }
}
