package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.SkillBean;
import com.surevine.neon.util.Properties;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Handles persistence of NS_SKILLS fields
 */
public class SkillsPersistenceHandler extends AbstractNamespaceHandler implements NamespaceHandler {
    public static final String FIELD_SKILLS = "SKILL";
    
    @Override
    public String getNamespace() {
        return ProfileDAO.NS_SKILLS;
    }

    @Override
    public void persist(ProfileBean profile, DataImporter importer) {
        String hsetKey = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + getNamespace();
        Set<SkillBean> skills = profile.getSkills();
        if (skills.size() == 0) {
            setMultipleField(hsetKey, FIELD_SKILLS, importer.getImporterName(), skills);
        }
    }

    @Override
    public void load(ProfileBean profile) {
        if (profile.getUserID() != null) {
            String baseNamespace = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + ProfileDAO.NS_SKILLS;
            Map<String,String> profileData = jedis.hgetAll(baseNamespace);
            
            Collection<String> skillStrings = getMultipleField(profileData,FIELD_SKILLS,profile);
            for (String skillString:skillStrings) {
                SkillBean bean = new SkillBean();
                bean.populateFromString(skillString);
                profile.getSkills().add(bean);
            }
        }
    }
}
