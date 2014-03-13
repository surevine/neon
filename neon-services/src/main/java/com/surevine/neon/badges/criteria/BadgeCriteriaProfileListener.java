package com.surevine.neon.badges.criteria;

import com.surevine.neon.dao.ProfileUpdatedListener;
import com.surevine.neon.model.ProfileBean;

import java.util.HashSet;
import java.util.Set;

public class BadgeCriteriaProfileListener implements ProfileUpdatedListener {
    private Set<BadgeCriteriaChecker> criteriaCheckers = new HashSet<BadgeCriteriaChecker>();
    
    @Override
    public void profileUpdated(ProfileBean profileBean) {
        if (criteriaCheckers.size() > 0) {
            if (profileBean != null) {
                for (BadgeCriteriaChecker checker:criteriaCheckers) {
                    checker.checkCriteria(profileBean);
                }
            }
        }
    }

    public void setCriteriaCheckers(Set<BadgeCriteriaChecker> criteriaCheckers) {
        this.criteriaCheckers = criteriaCheckers;
    }
}
