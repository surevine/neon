package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ConnectionBean;
import com.surevine.neon.model.ProfileBean;
import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

public class SocialButterflyCriteriaCheckerTest {
    private SocialButterflyCriteriaChecker underTest;
    private BadgeClassDAO mockBadgeDAO;
    private BadgeAssertionDAO mockBadgeAssertionDAO;

    @Test
    public void checkAssertionLogic() {
        Collection<BadgeAssertion> existingBadges = new HashSet<BadgeAssertion>();
        ProfileBean profileBean = new ProfileBean();
        profileBean.setUserID("user1");

        for (int i = 0; i < 20; i++) {
            ConnectionBean c = new ConnectionBean();
            c.setUserID("u" + i);
            c.setAnnotation("u" + i + " connection");
            profileBean.getConnections().add(c);
        }

        expect(mockBadgeDAO.badgeClassExists("user1_sb")).andReturn(true);

        Capture<BadgeAssertion> badgeAssertionCapture = new Capture<BadgeAssertion>();
        mockBadgeAssertionDAO.persist(capture(badgeAssertionCapture));

        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        underTest.checkCriteriaInternal(profileBean, existingBadges);

        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);

        assertTrue(badgeAssertionCapture.getValue().getNamespace().equals("user1_sb"));
    }

    @Test
    public void checkFail() {
        Collection<BadgeAssertion> existingBadges = new HashSet<BadgeAssertion>();
        ProfileBean profileBean = new ProfileBean();
        profileBean.setUserID("user1");

        for (int i = 0; i < 19; i++) {
            ConnectionBean c = new ConnectionBean();
            c.setUserID("u" + i);
            c.setAnnotation("u" + i + " connection");
            profileBean.getConnections().add(c);
        }

        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        underTest.checkCriteriaInternal(profileBean, existingBadges);

        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
    }

    @Test
    public void checkFailOnDuplicate() {
        Collection<BadgeAssertion> existingBadges = new HashSet<BadgeAssertion>();
        ProfileBean profileBean = new ProfileBean();
        profileBean.setUserID("user1");

        for (int i = 0; i < 20; i++) {
            ConnectionBean c = new ConnectionBean();
            c.setUserID("u");
            c.setAnnotation("u" + i + " connection");
            profileBean.getConnections().add(c);
        }

        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        underTest.checkCriteriaInternal(profileBean, existingBadges);

        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
    }

    @Before
    public void setup() {
        mockBadgeDAO = createMock(BadgeClassDAO.class);
        mockBadgeAssertionDAO = createMock(BadgeAssertionDAO.class);
        underTest = new SocialButterflyCriteriaChecker();
        underTest.setBadgeAssertionDAO(mockBadgeAssertionDAO);
        underTest.setBadgeClassDAO(mockBadgeDAO);
    }

    @After
    public void teardown() {
        mockBadgeDAO = null;
        mockBadgeAssertionDAO = null;
        underTest = null;
    }
}
