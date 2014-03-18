package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.model.IdentityObject;
import com.surevine.neon.badges.model.VerificationObject;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.util.Properties;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

public abstract class BadgeCriteriaChecker {
    private Logger logger = Logger.getLogger(this.getClass());
    protected BadgeAssertionDAO badgeAssertionDAO;
    protected BadgeClassDAO badgeClassDAO;

    void checkCriteria(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        if (profileBean.getVcard().getEmail() != null) {
            checkCriteriaInternal(profileBean, existingBadges);
        } else {
            logger.info("Failed to award badge to user as they do not have an email set (this is required for identity in the assertion). Once an email is set this badge will be awarded on next profile update.");
        }
    }

    abstract void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges);

    public void setBadgeAssertionDAO(BadgeAssertionDAO badgeAssertionDAO) {
        this.badgeAssertionDAO = badgeAssertionDAO;
    }

    public void setBadgeClassDAO(BadgeClassDAO badgeClassDAO) {
        this.badgeClassDAO = badgeClassDAO;
    }

    protected boolean alreadyAwarded(String namespace, Collection<BadgeAssertion> existingBadges) {
        for (BadgeAssertion existingAssertion:existingBadges) {
            if (existingAssertion.getNamespace().equals(namespace)) {
                return true;
            }
        }
        return false;
    }

    protected void assertProjectBadge(String userID, String email, String projectID, String namespacePostfix, String image) {
        String namespace = userID + "_" + projectID + "_" + namespacePostfix;
        if (badgeClassExists(projectID + "_" + namespacePostfix)) {
            try {
                BadgeAssertion ba = new BadgeAssertion();
                ba.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/" + projectID + "_" + namespacePostfix));
                ba.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/" + image));
                ba.setNamespace(namespace);
                VerificationObject voba = new VerificationObject();
                voba.setType("hosted");
                voba.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/" + userID + "_" + projectID + "_" + namespacePostfix));
                ba.setVerify(voba);
                IdentityObject ioba = new IdentityObject();
                ioba.setType("email");
                ioba.setHashed(false);
                ioba.setIdentity(email);
                ba.setRecipient(ioba);
                ba.setUid(UUID.randomUUID().toString());
                ba.setIssuedOn(new Date());
                badgeAssertionDAO.persist(ba);
            } catch (MalformedURLException mue) {
                // set by us so will be a code or config issue if we get here - noop
            }
        } else {
            logger.trace("Did not create badge assertion for " + userID + " as no badge class exists for namespace " + namespace);
        }
    }

    protected void assertBadge(String userID, String email, String namespacePostfix, String image) {
        String namespace = userID + "_" + namespacePostfix;
        if (badgeClassExists(namespace)) {
            try {
                BadgeAssertion ba = new BadgeAssertion();
                ba.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/" + namespacePostfix));
                ba.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/" + image));
                ba.setNamespace(namespace);
                VerificationObject voba = new VerificationObject();
                voba.setType("hosted");
                voba.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/" + userID + "_" + namespacePostfix));
                ba.setVerify(voba);
                IdentityObject ioba = new IdentityObject();
                ioba.setType("email");
                ioba.setHashed(false);
                ioba.setIdentity(email);
                ba.setRecipient(ioba);
                ba.setUid(UUID.randomUUID().toString());
                ba.setIssuedOn(new Date());
                badgeAssertionDAO.persist(ba);
            } catch (MalformedURLException mue) {
                // set by us so will be a code or config issue if we get here - noop
            }
        } else {
            logger.trace("Did not create badge assertion for " + userID + " as no badge class exists for namespace " + namespace);
        }
    }
    
    protected boolean badgeClassExists(final String badgeClassNamespace) {
        return badgeClassDAO.badgeClassExists(badgeClassNamespace);
    }
}
