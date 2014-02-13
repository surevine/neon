package com.surevine.neon.service;

import com.surevine.neon.model.ProfileBean;

/**
 * Profile service interface
 */
public interface ProfileService {
    public ProfileBean getProfile(String userID);
}
