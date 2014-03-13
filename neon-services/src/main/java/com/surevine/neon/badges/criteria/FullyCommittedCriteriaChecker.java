package com.surevine.neon.badges.criteria;

import com.surevine.neon.model.ProfileBean;

public class FullyCommittedCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteria(ProfileBean profileBean) {
        // check whether the profile meets the criteria to be awarded this badge
        // if it does create the relevant badge assertion (BadgeAssertionDAO in super class)
    }
}
