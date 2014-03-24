package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Checks the criteria for the "Fully Committed" badge (10 or more commits on a project)
 */
public class FullyCommittedCriteriaChecker extends BadgeCriteriaChecker {
    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        String userID = profileBean.getUserID();
        Set<ProjectActivityBean> pabs = profileBean.getProjectActivity();

        Map<String, Integer> projectCommits = new HashMap<String, Integer>();

        for (ProjectActivityBean pab:pabs) {
            if (ProjectActivityBean.ProjectActivityType.PROJECT_COMMIT.equals(pab.getType())) {
                if (!projectCommits.containsKey(pab.getProjectID())) {
                    projectCommits.put(pab.getProjectID(), 0);
                }
                
                Integer currentCommitCount = projectCommits.get(pab.getProjectID());
                projectCommits.put(pab.getProjectID(), currentCommitCount + 1);
            }
        }

        for (Map.Entry<String, Integer> entry:projectCommits.entrySet()) {
            String projectID = entry.getKey();
            Integer commitCount = entry.getValue();
            String namespace = userID + "_" + projectID + "_gc10";
            
            if (commitCount >= 10 && !alreadyAwarded(namespace,existingBadges)) {
                assertProjectBadge(userID, profileBean.getVcard().getEmail(), projectID, "gc10", "gitlab-10-commit.png");
            }
        }
    }
}
