package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.SkillBean;
import com.surevine.neon.util.DateUtil;
import com.surevine.neon.util.Properties;

import java.util.Map;
import java.util.Set;

/**
 * Handles persistence of NS_STATUS fields
 */
public class StatusPersistenceHandler extends AbstractNamespaceHandler implements NamespaceHandler {
    public static final String FIELD_LOCATION = "LOCATION";
    public static final String FIELD_PRESENCE = "PRESENCE";
    public static final String FIELD_LAST_UPDATED = "LAST_UPDATED";
    
    @Override
    public String getNamespace() {
        return ProfileDAO.NS_STATUS;
    }

    @Override
    public void persist(ProfileBean profile, DataImporter importer) {
        String hsetKey = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + getNamespace();
        setSingleField(hsetKey,FIELD_LOCATION,importer.getImporterName(),profile.getStatus().getLocation());
        setSingleField(hsetKey,FIELD_PRESENCE,importer.getImporterName(),profile.getStatus().getPresence());
        setSingleField(hsetKey,FIELD_LAST_UPDATED,importer.getImporterName(), DateUtil.dateToString(profile.getStatus().getPresenceLastUpdated()));
    }

    @Override
    public void load(ProfileBean profile) {
        String hsetKey = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + getNamespace();
        Map<String,String> profileData = jedis.hgetAll(hsetKey);
        
        profile.getStatus().setLocation(getSingleField(profileData, FIELD_LOCATION, profile));
        profile.getStatus().setPresence(getSingleField(profileData, FIELD_PRESENCE, profile));
        String statusLastUpdatedString = getSingleField(profileData, FIELD_LAST_UPDATED, profile);
        if (statusLastUpdatedString != null && !statusLastUpdatedString.isEmpty()) {
            profile.getStatus().setPresenceLastUpdated(DateUtil.stringToDate(statusLastUpdatedString));
        }
    }
}
