package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;
import org.apache.log4j.Logger;

import java.util.Collection;

public abstract class BadgeCriteriaChecker {
    private Logger logger = Logger.getLogger(this.getClass());
    protected BadgeAssertionDAO badgeAssertionDAO;
    
    void checkCriteria(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        if (profileBean.getVcard().getEmail() != null) {
            checkCriteriaInternal(profileBean, existingBadges);
        } else {
            logger.info("Failed to award badge to user as they do not have an email set (this is required for identity in the assertion). Once an email is set this badge will be awarded on next profile update.");
        }
    }

    abstract void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges);

    public void setBadgeAssertionDAO(BadgeAssertionDAO badgeAssertionDAO) {
        this.badgeAssertionDAO = badgeAssertionDAO;
    }
}
