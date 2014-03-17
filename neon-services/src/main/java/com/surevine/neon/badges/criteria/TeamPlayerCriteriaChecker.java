package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;

import java.util.Collection;

public class TeamPlayerCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        // check whether the profile meets the criteria to be awarded this badge
        // if it does create the relevant badge assertion (BadgeAssertionDAO in super class)
    }
}
