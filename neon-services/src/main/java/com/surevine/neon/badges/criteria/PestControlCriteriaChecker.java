package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;

import java.util.Collection;

public class PestControlCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        // not in the gitlab importer yet - implementation TODO
    }
}
