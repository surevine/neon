package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.model.*;

import java.net.URL;
import java.util.Date;
import java.util.Set;

public class ProfileDAOImpl implements ProfileDAO {
    @Override
    public ProfileBean getProfileForUser(String userID) {
        // doesn't do anything yet - mocked until importers have something in the DB to build a profile bean from
        return getMockBean(userID);
    }

    // mocking for now
    private ProfileBean getMockBean(String userID) {
        ProfileBean bean = new ProfileBean();
        bean.setUserID(userID);
        SkillBean bean1 = new SkillBean();
        bean1.setSkillName("Java");
        bean1.setRating(SkillBean.SKILL_ARTISAN);
        bean.getSkills().add(bean1);

        SkillBean bean2 = new SkillBean();
        bean2.setSkillName("JavaScript");
        bean2.setRating(SkillBean.SKILL_BEGINNER);
        bean.getSkills().add(bean2);

        SkillBean bean3 = new SkillBean();
        bean3.setSkillName("Java");
        bean3.setRating(SkillBean.SKILL_JOURNEYMAN);
        bean3.setInferred(true);
        bean3.setDisavowed(true);
        bean.getSkills().add(bean3);

        // add some additional props
        bean.getAdditionalProperties().put("add1", "randomvalue1");
        bean.getAdditionalProperties().put("add2", "randomvalue2");
        bean.getAdditionalProperties().put("add3", "randomvalue3");

        bean.getVcard().setOrg("HR");
        bean.getVcard().setFn("Dave Smith");
        bean.getVcard().setEmail("dsmith@localhost");
        bean.getVcard().setTitle("Java Developer");
        try {
            bean.getVcard().getPhoto().setMimeType("image/gif");
            bean.getVcard().getPhoto().setPhotoURL(new URL("http://someurl/image.gif"));
        } catch (Exception e) {
            // do nothing
        }

        bean.getVcard().getTelephoneNumbers().add(new VCardTelBean("n", "11111"));
        bean.getVcard().getTelephoneNumbers().add(new VCardTelBean("s", "22222"));

        StatusBean statusBean = new StatusBean();
        statusBean.setLocation("Building B");
        statusBean.setPresence("Away");
        statusBean.setPresenceLastUpdated(new Date());
        bean.setStatus(statusBean);

        bean.setBio("In 1972 sent to prison by a military court for a crime they didn't commit. This man promptly escaped from a maximum-security stockade to the Los Angeles underground. Today, still wanted by the government, he survives as a soldier of fortune.");

        ActivityBean a1 = new ActivityBean();
        a1.setActivityType("GIT Commit");
        a1.setActivityDescription("Committed branch: BUG-1234");
        a1.setActivityTime(new Date());
        a1.setSourceSystem("Gitlab");
        bean.getActivityStream().add(a1);

        ActivityBean a2 = new ActivityBean();
        a2.setActivityType("GIT Commit");
        a2.setActivityDescription("Committed branch: BUG-2345");
        a2.setActivityTime(new Date());
        a2.setSourceSystem("Gitlab");
        bean.getActivityStream().add(a2);

        ActivityBean a3 = new ActivityBean();
        a3.setActivityType("Issue resolved");
        a3.setActivityDescription("Marked issue 12345 as resolved, fixed.");
        a3.setActivityTime(new Date());
        a3.setSourceSystem("Jira");
        bean.getActivityStream().add(a3);

        ActivityBean a4 = new ActivityBean();
        a4.setActivityType("Login");
        a4.setActivityDescription("Logged in to Pidgin");
        a4.setActivityTime(new Date());
        a4.setSourceSystem("Pidgin");
        bean.getActivityStream().add(a4);
        return bean;
    }
    
	@Override
	public void persistProfile(ProfileBean profile) {
    	//Intentionally blank for now
	}
}
