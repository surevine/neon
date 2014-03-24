package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;

import java.util.Collection;

/**
 * Checks the criteria for the "Merge Master" badge (created a merge request)
 */
public class MergeMasterCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        // not in the gitlab importer yet - implementation TODO
    }
}
