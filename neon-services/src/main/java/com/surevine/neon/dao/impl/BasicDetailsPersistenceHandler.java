package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.VCardTelBean;
import com.surevine.neon.redis.PooledJedis;
import com.surevine.neon.util.Properties;

/**
 * Handles persistence of basic profile details
 */
public class BasicDetailsPersistenceHandler implements NamespaceHandler {
    public static final String FIELD_FN = "FN";
    public static final String FIELD_EMAIL = "EMAIL";
    public static final String FIELD_ORG = "ORG";
    public static final String FIELD_TITLE = "TITLE";
    public static final String FIELD_PHOTO_MIME = "PHOTO_MIME";
    public static final String FIELD_PHOTO_URL = "PHOTO_URL";
    public static final String FIELD_TEL = "TEL";
    public static final String FIELD_BIO = "BIO";

    @Override
    public String getNamespace() {
        return ProfileDAO.NS_BASIC_DETAILS;
    }

    @Override
    public void persist(ProfileBean profile, DataImporter importer) {
        String baseNamespace = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + ProfileDAO.NS_BASIC_DETAILS;
        
        if (profile.getVcard().getFn() != null) {
            PooledJedis.get().hset(baseNamespace, FIELD_FN + ":" + importer.getImporterName(), profile.getVcard().getFn());
        }
        if (profile.getVcard().getEmail() != null) {
            PooledJedis.get().hset(baseNamespace, FIELD_EMAIL + ":" + importer.getImporterName(), profile.getVcard().getEmail());
        }
        if (profile.getVcard().getOrg() != null) {
            PooledJedis.get().hset(baseNamespace, FIELD_ORG + ":" + importer.getImporterName(), profile.getVcard().getOrg());
        }
        if (profile.getVcard().getTitle() != null) {
            PooledJedis.get().hset(baseNamespace, FIELD_TITLE + ":" + importer.getImporterName(), profile.getVcard().getTitle());
        }
        if (profile.getVcard().getPhoto() != null && profile.getVcard().getPhoto().getMimeType() != null && profile.getVcard().getPhoto().getPhotoURL() != null) {
            PooledJedis.get().hset(baseNamespace, FIELD_PHOTO_MIME + ":" + importer.getImporterName(), profile.getVcard().getPhoto().getMimeType());
            PooledJedis.get().hset(baseNamespace, FIELD_PHOTO_URL + ":" + importer.getImporterName(), profile.getVcard().getPhoto().getPhotoURL().toString());
        }
        if (profile.getVcard().getTelephoneNumbers() != null && profile.getVcard().getTelephoneNumbers().size() > 0) {
            for (VCardTelBean tel:profile.getVcard().getTelephoneNumbers()) {
                PooledJedis.get().hset(baseNamespace, FIELD_TEL + ":" + importer.getImporterName() + ":" + tel.getType(), tel.getNumber());
            }
        }
        if (profile.getBio() != null) {
            PooledJedis.get().hset(baseNamespace, FIELD_BIO + ":" + importer.getImporterName(), profile.getBio());
        }
    }
    
    @Override
    public void load(ProfileBean profile) {
        String baseNamespace = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + ProfileDAO.NS_BASIC_DETAILS;
        // TODO: Load the hash, go through the entries populating the profile with the highest priority field values we have previously stored, attach source metadata to bean.
    }
}
