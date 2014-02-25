package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;

/**
 * Handles persistence of NS_ACTIVITY fields
 */
public class ActivityPersistenceHandler extends AbstractNamespaceHandler implements NamespaceHandler {
    @Override
    public String getNamespace() {
        return ProfileDAO.NS_ACTIVITY;
    }

    @Override
    public void persist(ProfileBean profile, DataImporter importer) {

    }

    @Override
    public void load(ProfileBean bean) {

    }
}
