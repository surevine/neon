package com.surevine.neon.service.impl;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.service.ProfileService;
import com.surevine.neon.model.ProfileBean;

/**
 * Profile service implementation
 */
public class ProfileServiceImpl implements ProfileService {
    private ProfileDAO dao;
    
    @Override
    public ProfileBean getProfile(String userID) {
        return dao.getProfileForUser(userID);
    }
    
    public void setDao(ProfileDAO dao) {
        this.dao = dao;
    }
}
