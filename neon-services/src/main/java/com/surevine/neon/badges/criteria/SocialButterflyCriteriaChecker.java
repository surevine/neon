package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;

import java.util.Collection;

/**
 * Checks the criteria for the "Social Butterfly" badge (>=20 connections)
 */
public class SocialButterflyCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        if (profileBean.getUniqueConnectionUserIDs().size() >= 20 && !alreadyAwarded(profileBean.getUserID() + "_sb",existingBadges)) {
            assertBadge(profileBean.getUserID(), profileBean.getVcard().getEmail(), "sb", "gitlab-social-butterfly.png");
        }
    }
}
