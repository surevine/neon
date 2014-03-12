package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ActivityBean;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.SkillBean;
import com.surevine.neon.util.Properties;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Handles persistence of NS_ACTIVITY fields
 */
public class ActivityPersistenceHandler extends AbstractNamespaceHandler implements NamespaceHandler {
    public static final String FIELD_ACTIVITIES = "ACTIVITY";
    @Override
    public String getNamespace() {
        return ProfileDAO.NS_ACTIVITY;
    }

    @Override
    public void persist(ProfileBean profile, DataImporter importer) {
        String hsetKey = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + getNamespace();
        List<ActivityBean> activities = profile.getActivityStream();
        if (activities.size() > 0) {
            setMultipleField(hsetKey, FIELD_ACTIVITIES, importer.getImporterName(), activities);
        }
    }

    @Override
    public void load(ProfileBean profile) {
        if (profile.getUserID() != null) {
            String baseNamespace = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + ProfileDAO.NS_ACTIVITY;
            Map<String,String> profileData = jedis.hgetAll(baseNamespace);

            Collection<String> activityStrings = getMultipleField(profileData,FIELD_ACTIVITIES,profile);
            for (String activityString:activityStrings) {
                ActivityBean bean = new ActivityBean();
                bean.populateFromString(activityString);
                profile.getActivityStream().add(bean);
            }
        }
    }
}
