package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.util.Properties;

import java.util.Map;

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
        String hsetKey = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + getNamespace();
        for (Map.Entry<String,String> entry:profile.getAdditionalProperties().entrySet()) {
            setSingleField(hsetKey,entry.getKey(),importer.getImporterName(), entry.getValue());
        }
    }

    @Override
    public void load(ProfileBean profile) {
        String hsetKey = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + getNamespace();
        Map<String,String> additionalProperties = jedis.hgetAll(hsetKey);
        
        for (Map.Entry<String,String> entry:additionalProperties.entrySet()) {
            String field = entry.getKey().split(":")[0];
            profile.getAdditionalProperties().put(field, entry.getValue());
        }
    }
}
