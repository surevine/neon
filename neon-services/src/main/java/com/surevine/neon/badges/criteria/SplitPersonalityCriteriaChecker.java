package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class SplitPersonalityCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        boolean multipleProjects = false;
        
        Iterator projectIDs = profileBean.getIDsOfActiveProjects();
        if (projectIDs.hasNext()) {
            projectIDs.next();
            multipleProjects = projectIDs.hasNext();
        }
        
        if (multipleProjects && !alreadyAwarded(profileBean.getUserID() + "_mp",existingBadges)) {
            assertBadge(profileBean.getUserID(), profileBean.getVcard().getEmail(), "mp", "gitlab-multi-project.png");
        }
    }
}
