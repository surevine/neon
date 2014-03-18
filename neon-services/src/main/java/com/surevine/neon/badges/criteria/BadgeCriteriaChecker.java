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

/**
 * Base class for classes that check whether a profile meets the criteria to be awarded a badge.
 */
public abstract class BadgeCriteriaChecker {
    private Logger logger = Logger.getLogger(this.getClass());
    protected BadgeAssertionDAO badgeAssertionDAO;
    protected BadgeClassDAO badgeClassDAO;

    /**
     * Entry method for checking whether a profile meets the criteria of the badge this class is checking.
     * @param profileBean the profile information to check
     * @param existingBadges any existing badges associated with the profile's userID
     */
    void checkCriteria(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges) {
        if (profileBean.getVcard().getEmail() != null) {
            checkCriteriaInternal(profileBean, existingBadges);
        } else {
            logger.info("Failed to award badge to user as they do not have an email set (this is required for identity in the assertion). Once an email is set this badge will be awarded on next profile update.");
        }
    }

    /**
     * Performs the criteria check against the argument profile bean 
     * @param profileBean the profile information to check
     * @param existingBadges any existing badges associated with the profile's userID
     */
    abstract void checkCriteriaInternal(ProfileBean profileBean, Collection<BadgeAssertion> existingBadges);

    public void setBadgeAssertionDAO(BadgeAssertionDAO badgeAssertionDAO) {
        this.badgeAssertionDAO = badgeAssertionDAO;
    }

    public void setBadgeClassDAO(BadgeClassDAO badgeClassDAO) {
        this.badgeClassDAO = badgeClassDAO;
    }

    /**
     * Determines if the argument collection of badge assertions contains one with the argument namespace
     * @param namespace the namespace to look for
     * @param existingBadges the collection of existing badges
     * @return true if the collection contains a badge assertion with the argument namespace
     */
    protected boolean alreadyAwarded(String namespace, Collection<BadgeAssertion> existingBadges) {
        for (BadgeAssertion existingAssertion:existingBadges) {
            if (existingAssertion.getNamespace().equals(namespace)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a project level badge assertion. The namespace for the assertion is set as userID_projectID_namespacePostfix
     * @param userID the user ID of the badge achiever
     * @param email the email address of the badge achiever
     * @param projectID the project ID the badge relates to
     * @param namespacePostfix namespace postfix (usually the badge class namespace)
     * @param image the name of the badge image
     */
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

    /**
     * Creates a badge assertion. The namespace for the assertion is set as userID_namespacePostfix
     * @param userID the user ID of the badge achiever
     * @param email the email address of the badge achiever
     * @param namespacePostfix namespace postfix (usually the badge class namespace)
     * @param image the name of the badge image
     */
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

    /**
     * Determines if a badge class exists
     * @param badgeClassNamespace the namespace of the badge class to look for
     * @return true if the namespace exists
     */
    protected boolean badgeClassExists(final String badgeClassNamespace) {
        return badgeClassDAO.badgeClassExists(badgeClassNamespace);
    }
}
