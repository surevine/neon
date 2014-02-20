package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.VCardTelBean;
import com.surevine.neon.util.Properties;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Handles persistence of basic profile details
 */
public class BasicDetailsPersistenceHandler extends AbstractNamespaceHandler {
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
        String hsetKey = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + ProfileDAO.NS_BASIC_DETAILS;
        setSingleField(hsetKey, FIELD_FN, importer.getImporterName(), profile.getVcard().getFn());
        setSingleField(hsetKey, FIELD_EMAIL, importer.getImporterName(), profile.getVcard().getEmail());
        setSingleField(hsetKey, FIELD_ORG, importer.getImporterName(), profile.getVcard().getOrg());
        setSingleField(hsetKey, FIELD_TITLE, importer.getImporterName(), profile.getVcard().getTitle());
        setSingleField(hsetKey, FIELD_PHOTO_MIME, importer.getImporterName(), profile.getVcard().getPhoto().getMimeType());
        setSingleField(hsetKey, FIELD_PHOTO_URL, importer.getImporterName(), profile.getVcard().getPhoto().getPhotoURL().toString());
        setSingleField(hsetKey, FIELD_BIO, importer.getImporterName(), profile.getBio());
        
        if (profile.getVcard().getTelephoneNumbers() != null && profile.getVcard().getTelephoneNumbers().size() > 0) {
            setMultipleField(hsetKey, FIELD_TEL, importer.getImporterName(), profile.getVcard().getTelephoneNumbers());
        }
    }

    @Override
    public void load(ProfileBean profile) {
        if (profile.getUserID() != null) {
            String baseNamespace = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + ProfileDAO.NS_BASIC_DETAILS;
            Map<String,String> profileData = jedis.hgetAll(baseNamespace);
            
            profile.getVcard().setFn(getSingleField(profileData, FIELD_FN, profile));
            profile.getVcard().setEmail(getSingleField(profileData, FIELD_EMAIL, profile));
            profile.getVcard().setOrg(getSingleField(profileData, FIELD_ORG, profile));
            profile.getVcard().setTitle(getSingleField(profileData, FIELD_TITLE, profile));
            profile.getVcard().getPhoto().setMimeType(getSingleField(profileData, FIELD_PHOTO_MIME, profile));
            
            try {
                profile.getVcard().getPhoto().setPhotoURL(new URL(getSingleField(profileData, FIELD_PHOTO_URL, profile)));
            } catch (MalformedURLException mue) {
                // drop it - just don't set the URL
            }
            
            Iterable<String> telephoneNumbers = getMultipleField(profileData, FIELD_TEL, profile);
            for (String telString:telephoneNumbers) {
                VCardTelBean telBean = new VCardTelBean();
                String type = telString.substring(0, telString.indexOf(";"));
                String number = telString.substring(telString.indexOf(";") + 1);
                telBean.setType(type);
                telBean.setNumber(number);
                profile.getVcard().getTelephoneNumbers().add(telBean);
            }
            
        } else {
            // for now log as a bug as we should never be calling this without a userID - worth guarding in case we do. 
            // this whole check can probably be removed once the codebase is stable and tested.
            logger.error("Failed to correctly respond to a request to load a profile as the userID wasn't present in the ProfileBean. This indicates a bug as the contract requires the userID to be set.");
        }
    }
}
