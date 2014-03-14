package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.model.ProfileBean;

public abstract class BadgeCriteriaChecker {
    protected BadgeAssertionDAO badgeAssertionDAO;
    
    abstract void checkCriteria(ProfileBean profileBean);

    public void setBadgeAssertionDAO(BadgeAssertionDAO badgeAssertionDAO) {
        this.badgeAssertionDAO = badgeAssertionDAO;
    }
}
