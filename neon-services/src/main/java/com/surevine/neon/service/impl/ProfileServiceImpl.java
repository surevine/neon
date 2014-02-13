package com.surevine.neon.service.impl;

import com.surevine.neon.service.ProfileService;
import com.surevine.neon.service.bean.ProfileBean;

/**
 * Profile service implementation
 */
public class ProfileServiceImpl implements ProfileService {
    @Override
    public ProfileBean getProfile(String userID) {
        ProfileBean bean = new ProfileBean();
        bean.setUserID(userID);
        return bean;
    }
            
}
