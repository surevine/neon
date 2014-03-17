package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.model.IdentityObject;
import com.surevine.neon.badges.model.VerificationObject;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.util.Properties;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.UUID;

public class BigCheeseCriteriaChecker extends BadgeCriteriaChecker {
    private Logger logger = Logger.getLogger(BigCheeseCriteriaChecker.class);

    @Override
    void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        // check whether the profile meets the criteria to be awarded this badge
        // if it does create the relevant badge assertion (BadgeAssertionDAO in super class)
        boolean alreadyAwarded = false;
        skip: for (BadgeAssertion existingAssertion:existingBadges) {
            if (existingAssertion.getNamespace().equals(profileBean.getUserID() + "_op")) {
                // already has this badge
                alreadyAwarded = true;
                logger.debug("Skipping badge assertion as user already has badge " + profileBean.getUserID() + "_op");
                break skip;
            }
        }

        if (!alreadyAwarded) {
            try {
                String userID = profileBean.getUserID();
                BadgeAssertion ba = new BadgeAssertion();
                ba.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/op"));
                ba.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/gitlab-own-project.png"));
                ba.setNamespace(userID + "_op");
                VerificationObject voba4 = new VerificationObject();
                voba4.setType("hosted");
                voba4.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/op/" + userID));
                ba.setVerify(voba4);
                IdentityObject ioba4 = new IdentityObject();
                ioba4.setType("email");
                ioba4.setHashed(false);
                ioba4.setIdentity(profileBean.getVcard().getEmail());
                ba.setRecipient(ioba4);
                ba.setUid(UUID.randomUUID().toString());
                badgeAssertionDAO.persist(ba);
            } catch (MalformedURLException mue) {
                // set by us so will be a code or config issue if we get here - no op
            }
        }
    }
}
