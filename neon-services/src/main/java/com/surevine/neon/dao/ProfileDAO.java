package com.surevine.neon.dao;

import com.surevine.neon.model.ProfileBean;

import java.util.Set;

public interface ProfileDAO {
    public ProfileBean getProfileForUser(String userID);
    public ProfileBean getPartialProfileForUser(String userID, Set<String> namespaces);
}
