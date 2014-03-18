package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.model.IdentityObject;
import com.surevine.neon.badges.model.VerificationObject;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;
import com.surevine.neon.util.Properties;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * Checks the criteria for the "Pushy" badge (pushed some code)
 */
public class PushyCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        String userID = profileBean.getUserID();
        Set<ProjectActivityBean> pabs = profileBean.getProjectActivity();

        for (ProjectActivityBean pab:pabs) {
            if (ProjectActivityBean.ProjectActivityType.PROJECT_COMMIT.equals(pab.getType())) {
                String namespace = userID + "_" + pab.getProjectID() + "_gc";
                if (!alreadyAwarded(namespace,existingBadges)) {
                    assertProjectBadge(userID, profileBean.getVcard().getEmail(), pab.getProjectID(), "gc", "gitlab-commit.png");
                }
            }
        }
    }

    
}
