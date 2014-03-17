package com.surevine.neon;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.dao.IssuerOrganisationDAO;
import com.surevine.neon.badges.model.*;
import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.importers.MockImporter;
import com.surevine.neon.model.*;
import com.surevine.neon.util.DateUtil;
import com.surevine.neon.util.Properties;
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
import java.util.UUID;

public class Bootstrap {
    private Logger logger = Logger.getLogger(Bootstrap.class);
    private IssuerOrganisationDAO issuerOrganisationDAO;
    private BadgeClassDAO badgeClassDAO;
    private ProfileDAO profileDAO;
    private BadgeAssertionDAO badgeAssertionDAO;
    private ImporterConfigurationDAO importerConfigurationDAO;

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

    public void setImporterConfigurationDAO(ImporterConfigurationDAO importerConfigurationDAO) {
        this.importerConfigurationDAO = importerConfigurationDAO;
    }

    private void processBootstrapJSON(JSONObject bootstrapJSON) {
        logger.info("Bootstrapping");
        parseIssuers(bootstrapJSON);
        parseBadges(bootstrapJSON);
        parseBadgeTemplates(bootstrapJSON);
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
    
    private void parseBadgeTemplates(JSONObject bootstrapJSON) {
        if (bootstrapJSON.has("badgeEnabledProjects") && bootstrapJSON.has("projectBadgeClassTemplates")) {
            JSONArray badgeTemplates = bootstrapJSON.getJSONArray("projectBadgeClassTemplates");
            JSONArray projects = bootstrapJSON.getJSONArray("badgeEnabledProjects");
            
            for (int i = 0; i < badgeTemplates.length(); i++) {
                JSONObject badgeTemplateJSO = badgeTemplates.getJSONObject(i);
                if (badgeTemplateJSO != null) {
                    try {
                        for (int j = 0; j < projects.length(); j++) {
                            JSONObject projectJSO = projects.getJSONObject(j);
                            String projectName = projectJSO.getString("projectName");
                            String projectID = projectJSO.getString("projectID");
                            if (projectName != null && (!projectName.isEmpty()) && projectID != null && (!projectID.isEmpty())) {
                                BadgeClass badgeClass = new BadgeClass(String.format(badgeTemplateJSO.toString(), projectName, projectID), String.format(badgeTemplateJSO.getString("namespace"), projectID));
                                badgeClassDAO.persist(badgeClass);
                            }
                        }
                    } catch (MalformedURLException e) {
                        logger.error("Could not parse issuer organisation during bootstrap", e);
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
        pab1.setType(ProjectActivityBean.ProjectActivityType.UNKNOWN);
        bean.getProjectActivity().add(pab1);

        ProjectActivityBean pab2 = new ProjectActivityBean();
        pab2.setProjectName("Project Orange");
        pab2.setProjectID("ORA1");
        pab2.setUrl("http://localhost/project/orange");
        pab2.setWhen(new Date());
        pab2.setActivityDescription("Created a merge request");
        pab2.setType(ProjectActivityBean.ProjectActivityType.UNKNOWN);
        bean.getProjectActivity().add(pab2);

        ProjectActivityBean pab3 = new ProjectActivityBean();
        pab3.setProjectName("Project Orange");
        pab3.setProjectID("ORA1");
        pab3.setUrl("http://localhost/project/orange");
        pab3.setWhen(new Date());
        pab3.setActivityDescription("Committed some code");
        pab3.setType(ProjectActivityBean.ProjectActivityType.PROJECT_COMMIT);
        bean.getProjectActivity().add(pab3);

        ProjectActivityBean pab4 = new ProjectActivityBean();
        pab4.setProjectName("Project Pink");
        pab4.setProjectID("PIN1");
        pab4.setUrl("http://localhost/project/pink");
        pab4.setWhen(new Date());
        pab4.setActivityDescription("Joined project as a member");
        pab4.setType(ProjectActivityBean.ProjectActivityType.PROJECT_JOIN);
        bean.getProjectActivity().add(pab4);

        ProjectActivityBean pab5 = new ProjectActivityBean();
        pab5.setProjectName("Project Pink");
        pab5.setProjectID("PIN1");
        pab5.setUrl("http://localhost/project/pink");
        pab5.setWhen(new Date());
        pab5.setActivityDescription("Closed a merge request");
        pab5.setType(ProjectActivityBean.ProjectActivityType.UNKNOWN);
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
        
        MockImporter imp = new MockImporter();
        profileDAO.persistProfile(bean, imp);
        importerConfigurationDAO.addImporterConfigurationOption(imp.getImporterName(), ImporterConfigurationDAO.NS_LAST_IMPORT, DateUtil.dateToString(new Date()));
        

//        try {
//            BadgeAssertion ba1 = new BadgeAssertion();
//            ba1.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/red_gjp"));
//            ba1.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/gitlab-join-project.png"));
//            ba1.setNamespace("mockuser_red_gjp");
//            VerificationObject voba1 = new VerificationObject();
//            voba1.setType("hosted");
//            voba1.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/red_gjp/mockuser")); // TODO needs to return self? If so need a service call for this.
//            ba1.setVerify(voba1);
//            IdentityObject ioba1 = new IdentityObject();
//            ioba1.setType("email");
//            ioba1.setHashed(false);
//            ioba1.setIdentity("mmock@localhost");
//            ba1.setRecipient(ioba1);
//            ba1.setUid(UUID.randomUUID().toString());
//            badgeAssertionDAO.persist(ba1);
//
//            BadgeAssertion ba2 = new BadgeAssertion();
//            ba2.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/red_gc"));
//            ba2.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/gitlab-commit.png"));
//            ba2.setNamespace("mockuser_red_gc");
//            VerificationObject voba2 = new VerificationObject();
//            voba2.setType("hosted");
//            voba2.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/red_gc/mockuser"));
//            ba2.setVerify(voba2);
//            IdentityObject ioba2 = new IdentityObject();
//            ioba2.setType("email");
//            ioba2.setHashed(false);
//            ioba2.setIdentity("mmock@localhost");
//            ba2.setRecipient(ioba2);
//            ba2.setUid(UUID.randomUUID().toString());
//            badgeAssertionDAO.persist(ba2);
//
//            BadgeAssertion ba3 = new BadgeAssertion();
//            ba3.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/ora_gmr"));
//            ba3.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/gitlab-mr.png"));
//            ba3.setNamespace("mockuser_ora_gmr");
//            VerificationObject voba3 = new VerificationObject();
//            voba3.setType("hosted");
//            voba3.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/ora_gmr/mockuser"));
//            ba3.setVerify(voba3);
//            IdentityObject ioba3 = new IdentityObject();
//            ioba3.setType("email");
//            ioba3.setHashed(false);
//            ioba3.setIdentity("mmock@localhost");
//            ba3.setRecipient(ioba3);
//            ba3.setUid(UUID.randomUUID().toString());
//            badgeAssertionDAO.persist(ba3);
//
//            BadgeAssertion ba4 = new BadgeAssertion();
//            ba4.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/op"));
//            ba4.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/gitlab-own-project.png"));
//            ba4.setNamespace("mockuser_op");
//            VerificationObject voba4 = new VerificationObject();
//            voba4.setType("hosted");
//            voba4.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/op/mockuser"));
//            ba4.setVerify(voba4);
//            IdentityObject ioba4 = new IdentityObject();
//            ioba4.setType("email");
//            ioba4.setHashed(false);
//            ioba4.setIdentity("mmock@localhost");
//            ba4.setRecipient(ioba4);
//            ba4.setUid(UUID.randomUUID().toString());
//            badgeAssertionDAO.persist(ba4);
//
//            BadgeAssertion ba5 = new BadgeAssertion();
//            ba5.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/ora_gc10"));
//            ba5.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/gitlab-10-commit.png"));
//            ba5.setNamespace("mockuser_ora_gc10");
//            VerificationObject voba5 = new VerificationObject();
//            voba5.setType("hosted");
//            voba5.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/ora_gc10/mockuser"));
//            ba5.setVerify(voba5);
//            IdentityObject ioba5 = new IdentityObject();
//            ioba5.setType("email");
//            ioba5.setHashed(false);
//            ioba5.setIdentity("mmock@localhost");
//            ba5.setRecipient(ioba5);
//            ba5.setUid(UUID.randomUUID().toString());
//            badgeAssertionDAO.persist(ba5);
//
//            BadgeAssertion ba6 = new BadgeAssertion();
//            ba6.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/ora_rei"));
//            ba6.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/gitlab-resolve-issue.png"));
//            ba6.setNamespace("mockuser_ora_rei");
//            VerificationObject voba6 = new VerificationObject();
//            voba6.setType("hosted");
//            voba6.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/ora_rei/mockuser"));
//            ba6.setVerify(voba6);
//            IdentityObject ioba6 = new IdentityObject();
//            ioba6.setType("email");
//            ioba6.setHashed(false);
//            ioba6.setIdentity("mmock@localhost");
//            ba6.setRecipient(ioba6);
//            ba6.setUid(UUID.randomUUID().toString());
//            badgeAssertionDAO.persist(ba6);
//
//            BadgeAssertion ba7 = new BadgeAssertion();
//            ba7.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/ora_rai"));
//            ba7.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/gitlab-author-issue.png"));
//            ba7.setNamespace("mockuser_ora_rai");
//            VerificationObject voba7 = new VerificationObject();
//            voba7.setType("hosted");
//            voba7.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/ora_rai/mockuser"));
//            ba7.setVerify(voba7);
//            IdentityObject ioba7 = new IdentityObject();
//            ioba7.setType("email");
//            ioba7.setHashed(false);
//            ioba7.setIdentity("mmock@localhost");
//            ba7.setRecipient(ioba7);
//            ba7.setUid(UUID.randomUUID().toString());
//            badgeAssertionDAO.persist(ba7);
//
//            BadgeAssertion ba8 = new BadgeAssertion();
//            ba8.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/mp"));
//            ba8.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/gitlab-multi-project.png"));
//            ba8.setNamespace("mockuser_mp");
//            VerificationObject voba8 = new VerificationObject();
//            voba8.setType("hosted");
//            voba8.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/mp/mockuser"));
//            ba8.setVerify(voba8);
//            IdentityObject ioba8 = new IdentityObject();
//            ioba8.setType("email");
//            ioba8.setHashed(false);
//            ioba8.setIdentity("mmock@localhost");
//            ba8.setRecipient(ioba8);
//            ba8.setUid(UUID.randomUUID().toString());
//            badgeAssertionDAO.persist(ba8);
//
//            BadgeAssertion ba9 = new BadgeAssertion();
//            ba9.setBadge(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/class/sb"));
//            ba9.setImage(new URL(Properties.getProperties().getBaseURL() + "/badges/images/gitlab-social-butterfly.png"));
//            ba9.setNamespace("mockuser_sb");
//            VerificationObject voba9 = new VerificationObject();
//            voba9.setType("hosted");
//            voba9.setUrl(new URL(Properties.getProperties().getBaseURL() + "/rest/badges/assertion/sb/mockuser"));
//            ba9.setVerify(voba9);
//            IdentityObject ioba9 = new IdentityObject();
//            ioba9.setType("email");
//            ioba9.setHashed(false);
//            ioba9.setIdentity("mmock@localhost");
//            ba9.setRecipient(ioba9);
//            ba9.setUid(UUID.randomUUID().toString());
//            badgeAssertionDAO.persist(ba9);
//            
//        } catch (Exception e) {
//            // noop
//        }
    }
}
