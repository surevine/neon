package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class BigCheeseCriteriaCheckerTest {
    private BigCheeseCriteriaChecker underTest;
    private BadgeClassDAO mockBadgeDAO;
    private BadgeAssertionDAO mockBadgeAssertionDAO;

    @Test
    public void testCriteria() {
        expect(mockBadgeDAO.badgeClassExists("user_op")).andReturn(true);
        mockBadgeAssertionDAO.persist(anyObject(BadgeAssertion.class));

        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        Collection<BadgeAssertion> existingBadges = new HashSet<BadgeAssertion>();
        BadgeAssertion ba1 = new BadgeAssertion();
        ba1.setNamespace("user_differentbadge");
        existingBadges.add(ba1);
        BadgeAssertion ba2 = new BadgeAssertion();
        ba2.setNamespace("user2_op");
        existingBadges.add(ba2);
        ProfileBean profileBean = new ProfileBean();
        profileBean.setUserID("user");
        ProjectActivityBean pab = new ProjectActivityBean();
        pab.setType(ProjectActivityBean.ProjectActivityType.PROJECT_OWN);
        profileBean.getProjectActivity().add(pab);

        underTest.checkCriteriaInternal(profileBean, existingBadges);
        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
    }

    @Test
    public void testCriteriaMultiplePab() {
        expect(mockBadgeDAO.badgeClassExists("user_op")).andReturn(true);
        mockBadgeAssertionDAO.persist(anyObject(BadgeAssertion.class));

        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        Collection<BadgeAssertion> existingBadges = new HashSet<BadgeAssertion>();
        BadgeAssertion ba1 = new BadgeAssertion();
        ba1.setNamespace("user_differentbadge");
        existingBadges.add(ba1);
        BadgeAssertion ba2 = new BadgeAssertion();
        ba2.setNamespace("user2_op");
        existingBadges.add(ba2);
        ProfileBean profileBean = new ProfileBean();
        profileBean.setUserID("user");
        ProjectActivityBean pab = new ProjectActivityBean();
        pab.setType(ProjectActivityBean.ProjectActivityType.PROJECT_OWN);
        profileBean.getProjectActivity().add(pab);
        ProjectActivityBean pab2 = new ProjectActivityBean();
        pab2.setType(ProjectActivityBean.ProjectActivityType.PROJECT_OWN);
        profileBean.getProjectActivity().add(pab2);

        underTest.checkCriteriaInternal(profileBean, existingBadges);
        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
    }

    @Test
    public void testCriteriaWithExistingBadge() {
        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        Collection<BadgeAssertion> existingBadges = new HashSet<BadgeAssertion>();
        BadgeAssertion ba1 = new BadgeAssertion();
        ba1.setNamespace("user_differentbadge");
        existingBadges.add(ba1);
        BadgeAssertion ba2 = new BadgeAssertion();
        ba2.setNamespace("user_op");
        existingBadges.add(ba2);
        ProfileBean profileBean = new ProfileBean();
        profileBean.setUserID("user");
        ProjectActivityBean pab = new ProjectActivityBean();
        pab.setType(ProjectActivityBean.ProjectActivityType.PROJECT_OWN);
        profileBean.getProjectActivity().add(pab);

        underTest.checkCriteriaInternal(profileBean, existingBadges);
        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
    }

    @Test
    public void testCriteriaWithWrongPabs() {
        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        Collection<BadgeAssertion> existingBadges = new HashSet<BadgeAssertion>();
        BadgeAssertion ba1 = new BadgeAssertion();
        ba1.setNamespace("user_differentbadge");
        existingBadges.add(ba1);
        BadgeAssertion ba2 = new BadgeAssertion();
        ba2.setNamespace("user_op");
        existingBadges.add(ba2);
        ProfileBean profileBean = new ProfileBean();
        profileBean.setUserID("user");
        ProjectActivityBean pab = new ProjectActivityBean();
        pab.setType(ProjectActivityBean.ProjectActivityType.PROJECT_JOIN);
        profileBean.getProjectActivity().add(pab);
        ProjectActivityBean pab2 = new ProjectActivityBean();
        pab2.setType(ProjectActivityBean.ProjectActivityType.ISSUE_AUTHOR);
        profileBean.getProjectActivity().add(pab2);

        underTest.checkCriteriaInternal(profileBean, existingBadges);
        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
    }

    @Before
    public void setup() {
        mockBadgeDAO = createMock(BadgeClassDAO.class);
        mockBadgeAssertionDAO = createMock(BadgeAssertionDAO.class);
        underTest = new BigCheeseCriteriaChecker();
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
