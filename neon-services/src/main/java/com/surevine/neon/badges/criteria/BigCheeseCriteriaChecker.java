package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.model.IdentityObject;
import com.surevine.neon.badges.model.VerificationObject;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;
import com.surevine.neon.util.Properties;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class BigCheeseCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        Set<ProjectActivityBean> pabs = profileBean.getProjectActivity();

        outer: for (ProjectActivityBean pab:pabs) {
            if (ProjectActivityBean.ProjectActivityType.PROJECT_OWN.equals(pab.getType())) {
                // not project specific so if any project is owned awward the badge and then stop checking
                if (!alreadyAwarded(profileBean.getUserID() + "_op",existingBadges)) {
                    assertBadge(profileBean.getUserID(), profileBean.getVcard().getEmail(), "op", "gitlab-own-project.png");
                }
                break outer;
            }
        }
    }
}
