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

import java.util.HashSet;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertTrue;

public class SplitPersonalityCriteriaCheckerTest {
    private SplitPersonalityCriteriaChecker underTest;
    private BadgeClassDAO mockBadgeDAO;
    private BadgeAssertionDAO mockBadgeAssertionDAO;

    @Test
    public void testCriteria() {
        expect(mockBadgeDAO.badgeClassExists("user_mp")).andReturn(true);
        Capture<BadgeAssertion> badgeAssertionCapture1 = new Capture<BadgeAssertion>();
        mockBadgeAssertionDAO.persist(capture(badgeAssertionCapture1));

        replay(mockBadgeDAO);
        replay(mockBadgeAssertionDAO);

        
        ProfileBean profileBean = new ProfileBean();
        profileBean.setUserID("user");
        ProjectActivityBean pab = new ProjectActivityBean();
        pab.setProjectID("a");
        pab.setType(ProjectActivityBean.ProjectActivityType.PROJECT_OWN);
        profileBean.getProjectActivity().add(pab);
        ProjectActivityBean pab2 = new ProjectActivityBean();
        pab2.setProjectID("b");
        pab2.setType(ProjectActivityBean.ProjectActivityType.PROJECT_COMMIT);
        profileBean.getProjectActivity().add(pab2);

        underTest.checkCriteriaInternal(profileBean, new HashSet<BadgeAssertion>());
        assertTrue(badgeAssertionCapture1.getValue().getNamespace().equals("user_mp"));
        verify(mockBadgeDAO);
        verify(mockBadgeAssertionDAO);
    }

    @Before
    public void setup() {
        mockBadgeDAO = createMock(BadgeClassDAO.class);
        mockBadgeAssertionDAO = createMock(BadgeAssertionDAO.class);
        underTest = new SplitPersonalityCriteriaChecker();
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
