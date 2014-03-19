package com.surevine.neon.service.impl;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.ImportRegistry;
import com.surevine.neon.model.VCardBean;
import com.surevine.neon.service.ProfileService;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.service.bean.UserSummaryServiceBean;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Profile service implementation
 */
public class ProfileServiceImpl implements ProfileService {
    private ProfileDAO dao;
    private ImportRegistry importRegistry;
    
    @Override
    public ProfileBean getProfile(String userID) {
        return dao.getProfileForUser(userID);
    }

    @Override
    public void addUser(String userID) {
        dao.addUserIDToProfileList(userID);
        importRegistry.runImport(userID);
    }

    @Override
    public void removeUser(String userID) {
        dao.removeUserIDFromProfileList(userID);
    }

    @Override
    public Collection<UserSummaryServiceBean> getAllUsers() {
        Map<String, VCardBean> vcards = dao.getAllUserVCards();
        Set<UserSummaryServiceBean> summaries = new HashSet<UserSummaryServiceBean>();
        for (Map.Entry<String, VCardBean> entry:vcards.entrySet()) {
            summaries.add(new UserSummaryServiceBean(entry.getKey(), entry.getValue()));
        }
        return summaries;
    }

    public void setDao(ProfileDAO dao) {
        this.dao = dao;
    }

    public void setImportRegistry(ImportRegistry importRegistry) {
        this.importRegistry = importRegistry;
    }
}
