package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;

import java.util.Collection;
import java.util.Set;

/**
 * Checks the criteria for the "Bug Hunter" badge (raised an issue)
 */
public class BugHunterCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        String userID = profileBean.getUserID();
        Set<ProjectActivityBean> pabs = profileBean.getProjectActivity();
        
        for (ProjectActivityBean pab:pabs) {
            if (ProjectActivityBean.ProjectActivityType.ISSUE_AUTHOR.equals(pab.getType())) {
                String namespace = userID + "_" + pab.getProjectID() + "_rai";
                if (!alreadyAwarded(namespace,existingBadges)) {
                    assertProjectBadge(userID, profileBean.getVcard().getEmail(), pab.getProjectID(), "rai", "gitlab-author-issue.png");
                }
            }
        }
    }
}
