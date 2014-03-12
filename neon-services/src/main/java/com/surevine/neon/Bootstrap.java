package com.surevine.neon;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.dao.IssuerOrganisationDAO;
import com.surevine.neon.badges.model.BadgeClass;
import com.surevine.neon.badges.model.IssuerOrganisation;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.importers.MockImporter;
import com.surevine.neon.model.*;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class Bootstrap {
    private Logger logger = Logger.getLogger(Bootstrap.class);
    private IssuerOrganisationDAO issuerOrganisationDAO;
    private BadgeClassDAO badgeClassDAO;
    private ProfileDAO profileDAO;
    private BadgeAssertionDAO badgeAssertionDAO;

    public void bootstrap() {
        InputStream bootstrapIS = Bootstrap.class.getClassLoader().getResourceAsStream("bootstrap.json");
        if (bootstrapIS != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(bootstrapIS));
            String line;
            StringBuffer buffer = new StringBuffer();
            try {
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
            } catch (IOException e) {
                logger.error("Could not read bootstrap.json",e);
            }
            
            if (buffer.length() > 0) {
                processBootstrapJSON(new JSONObject(buffer.toString()));
            }
        } else {
            logger.warn("Could not bootstrap as bootstrap.json was not found on the classpath.");
        }
        persistMockProfile();
    }
    
    public void bootstrap(String bootstrapJSON) {
        processBootstrapJSON(new JSONObject(bootstrapJSON));
        persistMockProfile();
    }

    public void bootstrap(JSONObject bootstrapJSON) {
        processBootstrapJSON(bootstrapJSON);
        persistMockProfile();
    }

    public void setIssuerOrganisationDAO(IssuerOrganisationDAO issuerOrganisationDAO) {
        this.issuerOrganisationDAO = issuerOrganisationDAO;
    }

    public void setBadgeClassDAO(BadgeClassDAO badgeClassDAO) {
        this.badgeClassDAO = badgeClassDAO;
    }

    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    public void setBadgeAssertionDAO(BadgeAssertionDAO badgeAssertionDAO) {
        this.badgeAssertionDAO = badgeAssertionDAO;
    }

    private void processBootstrapJSON(JSONObject bootstrapJSON) {
        logger.info("Bootstrapping");
        parseIssuers(bootstrapJSON);
        parseBadges(bootstrapJSON);
    }

    private void parseIssuers(JSONObject bootstrapJSON) {
        if (bootstrapJSON.has("issuerOrganisations")) {
            JSONArray issuerOrganisations = bootstrapJSON.getJSONArray("issuerOrganisations");
            for (int i = 0; i < issuerOrganisations.length(); i++) {
                JSONObject issuerOrganisationJSO = issuerOrganisations.getJSONObject(i);
                if (issuerOrganisationJSO != null) {
                    try {
                        IssuerOrganisation issuerOrganisation = new IssuerOrganisation(issuerOrganisationJSO,issuerOrganisationJSO.getString("namespace"));
                        issuerOrganisationDAO.persist(issuerOrganisation);
                    } catch (MalformedURLException e) {
                        logger.error("Could not parse issuer organisation during bootstrap", e);
                    }
                }
            }
        }
    }

    private void parseBadges(JSONObject bootstrapJSON) {
        if (bootstrapJSON.has("badgeClasses")) {
            JSONArray badges = bootstrapJSON.getJSONArray("badgeClasses");
            for (int i = 0; i < badges.length(); i++) {
                JSONObject badgeJSO = badges.getJSONObject(i);
                if (badgeJSO != null) {
                    try {
                        BadgeClass badgeClass = new BadgeClass(badgeJSO,badgeJSO.getString("namespace"));
                        badgeClassDAO.persist(badgeClass);
                    } catch (MalformedURLException e) {
                        logger.error("Could not parse badge class during bootstrap", e);
                    }
                }
            }
        }
    }

    /**
     * Wall of code persists a mock profile for use for demos or for client dev work
     */
    private void persistMockProfile() {
        String userID = ProfileDAO.MOCK_USER_ID;
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
        bean.getAdditionalProperties().put("Favourite colour", "Red");
        bean.getAdditionalProperties().put("First car", "Ferrari");
        bean.getAdditionalProperties().put("Browser of choice", "Firefox");

        bean.getVcard().setOrg("HR");
        bean.getVcard().setFn("Michael Mock");
        bean.getVcard().setEmail("mmock@localhost");
        bean.getVcard().setTitle("Java Developer");
        try {
            bean.getVcard().getPhoto().setMimeType("image/jpg");
            bean.getVcard().getPhoto().setPhotoURL(new URL("http://4.bp.blogspot.com/_mtjEKB2K9F0/S9asQvp9q_I/AAAAAAAAAGc/2FaJZW5xhOw/s1600/Homer-original.jpg"));
        } catch (Exception e) {
            // do nothing
        }

        bean.getVcard().getTelephoneNumbers().add(new VCardTelBean("External", "01234 567890"));
        bean.getVcard().getTelephoneNumbers().add(new VCardTelBean("Internal", "12345"));
        bean.getVcard().getTelephoneNumbers().add(new VCardTelBean("Mobile", "07777 777777"));

        StatusBean statusBean = new StatusBean();
        statusBean.setLocation("Building B");
        statusBean.setPresence("Away");
        statusBean.setPresenceLastUpdated(new Date());
        bean.setStatus(statusBean);

        bean.setBio("In 1972 sent to prison by a military court for a crime they didn't commit. This man promptly escaped from a maximum-security stockade to the Los Angeles underground. Today, still wanted by the government, he survives as a soldier of fortune.");

        ActivityBean a1 = new ActivityBean();
        a1.setActivityType("Raised issue");
        a1.setActivityDescription("Raised issue 65442");
        a1.setActivityTime(new Date());
        a1.setSourceSystem("Jira");
        bean.getActivityStream().add(a1);

        ActivityBean a2 = new ActivityBean();
        a2.setActivityType("Profile update");
        a2.setActivityDescription("Updated personal project in Jive");
        a2.setActivityTime(new Date());
        a2.setSourceSystem("Jive");
        bean.getActivityStream().add(a2);

        ActivityBean a3 = new ActivityBean();
        a3.setActivityType("Resolve issue");
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

        ActivityBean a5 = new ActivityBean();
        a5.setActivityType("Blog");
        a5.setActivityDescription("Created a blog post");
        a5.setActivityTime(new Date());
        a5.setSourceSystem("Jive");
        bean.getActivityStream().add(a5);

        ProjectActivityBean pab1 = new ProjectActivityBean();
        pab1.setProjectName("Project Orange");
        pab1.setProjectID("ORA1");
        pab1.setUrl("http://localhost/project/orange");
        pab1.setWhen(new Date());
        pab1.setActivityDescription("Created project");
        bean.getProjectActivity().add(pab1);

        ProjectActivityBean pab2 = new ProjectActivityBean();
        pab2.setProjectName("Project Orange");
        pab2.setProjectID("ORA1");
        pab2.setUrl("http://localhost/project/orange");
        pab2.setWhen(new Date());
        pab2.setActivityDescription("Created a merge request");
        bean.getProjectActivity().add(pab2);

        ProjectActivityBean pab3 = new ProjectActivityBean();
        pab3.setProjectName("Project Orange");
        pab3.setProjectID("ORA1");
        pab3.setUrl("http://localhost/project/orange");
        pab3.setWhen(new Date());
        pab3.setActivityDescription("Committed some code");
        bean.getProjectActivity().add(pab3);

        ProjectActivityBean pab4 = new ProjectActivityBean();
        pab4.setProjectName("Project Pink");
        pab4.setProjectID("PIN1");
        pab4.setUrl("http://localhost/project/pink");
        pab4.setWhen(new Date());
        pab4.setActivityDescription("Joined project as a member");
        bean.getProjectActivity().add(pab4);

        ProjectActivityBean pab5 = new ProjectActivityBean();
        pab5.setProjectName("Project Pink");
        pab5.setProjectID("PIN1");
        pab5.setUrl("http://localhost/project/pink");
        pab5.setWhen(new Date());
        pab5.setActivityDescription("Closed a merge request");
        bean.getProjectActivity().add(pab5);
        
        ConnectionBean con1 = new ConnectionBean();
        con1.setUserID("smithj");
        con1.setAnnotation("Member of same project");
        bean.getConnections().add(con1);

        ConnectionBean con2 = new ConnectionBean();
        con2.setUserID("jonesa");
        con2.setAnnotation("Member of same project");
        bean.getConnections().add(con2);

        ConnectionBean con3 = new ConnectionBean();
        con3.setUserID("davisd");
        con3.setAnnotation("Manager");
        bean.getConnections().add(con3);

        ConnectionBean con4 = new ConnectionBean();
        con4.setUserID("potterp");
        con4.setAnnotation("Manages");
        bean.getConnections().add(con4);
        
        profileDAO.persistProfile(bean, new MockImporter());
    }
}
