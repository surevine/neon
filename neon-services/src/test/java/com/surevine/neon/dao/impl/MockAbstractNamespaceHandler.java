package com.surevine.neon.dao.impl;

import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;

/**
 * Stub / mock namespace handler for us in unit tests
 */
public class MockAbstractNamespaceHandler extends AbstractNamespaceHandler {
    @Override
    public String getNamespace() {
        return "MOCK";
    }

    @Override
    public void persist(ProfileBean profile, DataImporter importer) {
        
    }

    @Override
    public void load(ProfileBean bean) {

    }
}
