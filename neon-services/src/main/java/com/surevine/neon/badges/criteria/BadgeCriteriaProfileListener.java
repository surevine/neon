package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.dao.ProfileUpdatedListener;
import com.surevine.neon.model.ProfileBean;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Profile listener that responds to a profile being updated by running badge criteria checkers against the updated 
 * profile.
 */
public class BadgeCriteriaProfileListener implements ProfileUpdatedListener {
    private Set<BadgeCriteriaChecker> criteriaCheckers = new HashSet<BadgeCriteriaChecker>();
    protected BadgeAssertionDAO badgeAssertionDAO;
    
    @Override
    public void profileUpdated(ProfileBean profileBean) {
        Collection<BadgeAssertion> existingBadges = badgeAssertionDAO.getAllBadgesForUser(profileBean.getUserID());
        if (criteriaCheckers.size() > 0) {
            if (profileBean != null) {
                for (BadgeCriteriaChecker checker:criteriaCheckers) {
                    checker.checkCriteria(profileBean, existingBadges);
                }
            }
        }
    }

    public void setCriteriaCheckers(Set<BadgeCriteriaChecker> criteriaCheckers) {
        this.criteriaCheckers = criteriaCheckers;
    }

    public void setBadgeAssertionDAO(BadgeAssertionDAO badgeAssertionDAO) {
        this.badgeAssertionDAO = badgeAssertionDAO;
    }
}
