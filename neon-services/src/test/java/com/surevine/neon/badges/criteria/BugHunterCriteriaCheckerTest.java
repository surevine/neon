package com.surevine.neon.badges.criteria;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;
import org.easymock.Capture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

public class BugHunterCriteriaCheckerTest {
    private BugHunterCriteriaChecker underTest;
    private BadgeClassDAO mockBadgeDAO;
    private BadgeAssertionDAO mockBadgeAssertionDAO;
    
    @Test
    public void checkAssertionLogic() {
        Collection<BadgeAssertion> existingBadges = new HashSet<BadgeAssertion>();
        ProfileBean profileBean = new ProfileBean();
        profileBean.setUserID("user1");

        ProjectActivityBean pab = new ProjectActivityBean();
        pab.setProjectID("p1");
        pab.setType(ProjectActivityBean.ProjectActivityType.PROJECT_JOIN);
        profileBean.getProjectActivity().add(pab);
        ProjectActivityBean pab2 = new ProjectActivityBean();
        pab2.setProjectID("p1");
        pab2.setType(ProjectActivityBean.ProjectActivityType.ISSUE_AUTHOR);
        profileBean.getProjectActivity().add(pab2);
        ProjectActivityBean pab3 = new ProjectActivityBean();
        pab3.setProjectID("p2");
        pab3.setType(ProjectActivityBean.ProjectActivityType.PROJECT_OWN);
        profileBean.getProjectActivity().add(pab3);
        ProjectActivityBean pab4 = new ProjectActivityBean();
        pab4.setProjectID("p3");
        pab4.setType(ProjectActivityBean.ProjectActivityType.ISSUE_AUTHOR);
        profileBean.getProjectActivity().add(pab4);
        
        // the class existence will be checked for the two matching assertions
        expect(mockBadgeDAO.badgeClassExists("p1_rai")).andReturn(true);
        expect(mockBadgeDAO.badgeClassExists("p3_rai")).andReturn(true);

        Capture<BadgeAssertion> badgeAssertionCapture1 = new Capture<BadgeAssertion>();
        mockBadgeAssertionDAO.persist(capture(badgeAssertionCapture1));
        Capture<BadgeAssertion> badgeAssertionCapture2 = new Capture<BadgeAssertion>();
        mockBadgeAssertionDAO.persist(capture(badgeAssertionCapture2));
        
        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);
        
        underTest.checkCriteriaInternal(profileBean, existingBadges);
        
        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
        
        assertTrue(badgeAssertionCapture1.getValue().getNamespace().equals("user1_p1_rai") || badgeAssertionCapture2.getValue().getNamespace().equals("user1_p1_rai"));
        assertTrue(badgeAssertionCapture1.getValue().getNamespace().equals("user1_p3_rai") || badgeAssertionCapture2.getValue().getNamespace().equals("user1_p3_rai"));
        
    }

    @Before
    public void setup() {
        mockBadgeDAO = createMock(BadgeClassDAO.class);
        mockBadgeAssertionDAO = createMock(BadgeAssertionDAO.class);
        underTest = new BugHunterCriteriaChecker();
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
