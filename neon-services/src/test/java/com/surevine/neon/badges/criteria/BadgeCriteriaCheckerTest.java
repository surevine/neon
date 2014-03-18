package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.model.BadgeAssertion;
import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests BadgeCriteriaChecker
 */
public class BadgeCriteriaCheckerTest {
    private BadgeClassDAO mockBadgeDAO;
    private BadgeAssertionDAO mockBadgeAssertionDAO;
    private BadgeCriteriaChecker underTest;

    @Test
    public void testAlreadyAwarded() {
        Collection<BadgeAssertion> existingBadges = new HashSet<BadgeAssertion>();
        BadgeAssertion ba1 = new BadgeAssertion();
        ba1.setNamespace("namespace_1");
        existingBadges.add(ba1);
        BadgeAssertion ba2 = new BadgeAssertion();
        ba2.setNamespace("namespace_2");
        existingBadges.add(ba2);

        assertFalse(underTest.alreadyAwarded("namespace_3", existingBadges));

        BadgeAssertion ba3 = new BadgeAssertion();
        ba3.setNamespace("namespace_3");
        existingBadges.add(ba3);

        assertTrue(underTest.alreadyAwarded("namespace_3", existingBadges));
    }

    @Test
    public void testAssertBadge() {
        expect(mockBadgeDAO.badgeClassExists("u1_b1")).andReturn(true);
        Capture<BadgeAssertion> badgeAssertionCapture1 = new Capture<BadgeAssertion>();
        mockBadgeAssertionDAO.persist(capture(badgeAssertionCapture1));

        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        underTest.assertBadge("u1","","b1","");
        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
        
        assertTrue(badgeAssertionCapture1.getValue().getNamespace().equals("u1_b1"));
    }

    @Test
    public void testAssertBadgeFail() {
        expect(mockBadgeDAO.badgeClassExists("u1_b1")).andReturn(false);

        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        underTest.assertBadge("u1", "", "b1", "");
        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
    }

    @Test
    public void testAssertProjectBadgeFail() {
        expect(mockBadgeDAO.badgeClassExists("u1_p1_b1")).andReturn(false);

        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        underTest.assertProjectBadge("u1", "", "p1", "b1", "");
        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
    }

    @Test
    public void testAssertProjectBadge() {
        expect(mockBadgeDAO.badgeClassExists("u1_p1_b1")).andReturn(true);
        Capture<BadgeAssertion> badgeAssertionCapture1 = new Capture<BadgeAssertion>();
        mockBadgeAssertionDAO.persist(capture(badgeAssertionCapture1));

        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        underTest.assertProjectBadge("u1", "", "p1", "b1", "");
        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
        
        assertTrue(badgeAssertionCapture1.getValue().getNamespace().equals("u1_p1_b1"));
    }

    @Before
    public void setup() {
        mockBadgeDAO = createMock(BadgeClassDAO.class);
        mockBadgeAssertionDAO = createMock(BadgeAssertionDAO.class);
        underTest = new MockBadgeCriteriaChecker();
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
