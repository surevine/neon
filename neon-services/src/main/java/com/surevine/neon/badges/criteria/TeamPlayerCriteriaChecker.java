package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;

import java.util.Collection;
import java.util.Set;

public class TeamPlayerCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        String userID = profileBean.getUserID();
        Set<ProjectActivityBean> pabs = profileBean.getProjectActivity();

        for (ProjectActivityBean pab:pabs) {
            // assume any activity on a project means the user is a member
            String namespace = userID + "_" + pab.getProjectID() + "_gjp";
            if (!alreadyAwarded(namespace,existingBadges)) {
                assertProjectBadge(userID, profileBean.getVcard().getEmail(), pab.getProjectID(), "gjp", "gitlab-join-project.png");
            }
        }
    }
}
