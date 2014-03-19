package com.surevine.neon.service.impl;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.ImportRegistry;
import com.surevine.neon.service.ProfileService;
import com.surevine.neon.model.ProfileBean;

import java.util.Collection;

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
    public Collection<String> getAllUserIDs() {
        return dao.getUserIDList();
    }

    public void setDao(ProfileDAO dao) {
        this.dao = dao;
    }

    public void setImportRegistry(ImportRegistry importRegistry) {
        this.importRegistry = importRegistry;
    }
}
