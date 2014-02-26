package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;
import com.surevine.neon.model.SkillBean;
import com.surevine.neon.util.Properties;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Handles persistence of NS_PROJECT_DETAILS fields
 */
public class ProjectPersistenceHandler extends AbstractNamespaceHandler implements NamespaceHandler {
    public static final String FIELD_PROJECT_ACTIVITY = "PROJECT_ACTIVITY";
    
    @Override
    public String getNamespace() {
        return ProfileDAO.NS_PROJECT_DETAILS;
    }

    @Override
    public void persist(ProfileBean profile, DataImporter importer) {
        String hsetKey = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + getNamespace();
        Set<ProjectActivityBean> activities = profile.getProjectActivity();
        if (activities.size() == 0) {
            setMultipleField(hsetKey, FIELD_PROJECT_ACTIVITY, importer.getImporterName(), activities);
        }
    }

    @Override
    public void load(ProfileBean profile) {
        if (profile.getUserID() != null) {
            String baseNamespace = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + ProfileDAO.NS_PROJECT_DETAILS;
            Map<String,String> profileData = jedis.hgetAll(baseNamespace);

            Collection<String> actStrings = getMultipleField(profileData,FIELD_PROJECT_ACTIVITY,profile);
            for (String actString:actStrings) {
                ProjectActivityBean bean = new ProjectActivityBean();
                bean.populateFromString(actString);
                profile.getProjectActivity().add(bean);
            }
        }
    }
}
