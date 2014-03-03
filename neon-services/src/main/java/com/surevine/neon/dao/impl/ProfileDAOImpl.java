package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.inload.FeedRegistry;
import com.surevine.neon.model.*;
import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.util.Properties;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.*;

public class ProfileDAOImpl implements ProfileDAO {
    private Logger logger = Logger.getLogger(ProfileDAOImpl.class);
    private Map<String,NamespaceHandler> handlerMapping = new HashMap<>();
    private IPooledJedis jedis;
        
    @Override
    public ProfileBean getProfileForUser(String userID) {
        // MOCKED FOR NOW UNTIL IMPORT IS RICHER. Toggle the mock with dev.use_mock_profile application property
        if (Properties.getProperties().isUseMockProfile()) {
            return getMockBean(userID);
        } else {
            ProfileBean bean = new ProfileBean();
            bean.setUserID(userID);
            // run handlers to load persistent information held about the user
            for (NamespaceHandler handler:handlerMapping.values()) {
                handler.load(bean);
            }
            
            // augment profile data with any live feeds
            FeedRegistry.getInstance().augmentProfileWithFeeds(bean);
            return bean;
        }
    }
    
    @Override
    public Set<String> getUserIDList() {
        logger.debug("Getting list of current users");
        Set<String> userIDs = jedis.smembers(Properties.getProperties().getSystemNamespace() + ":" + NS_USER_LIST_KEY);
        return userIDs;
    }

    @Override
    public void addUserIDToProfileList(String userID) {
        logger.debug("Adding user " + userID + " to the list of current users");
        if (!jedis.sismember(Properties.getProperties().getSystemNamespace() + ":" + NS_USER_LIST_KEY, userID)) {
            jedis.sadd(Properties.getProperties().getSystemNamespace() + ":" + NS_USER_LIST_KEY, userID);
        }
    }

    @Override
    public void removeUserIDFromProfileList(String userID) {
        logger.debug("Removing user " + userID + " from the list of current users");
        if (jedis.sismember(Properties.getProperties().getSystemNamespace() + ":" + NS_USER_LIST_KEY, userID)) {
            jedis.srem(Properties.getProperties().getSystemNamespace() + ":" + NS_USER_LIST_KEY, userID);
        }

        logger.debug("Removing user profile data for user " + userID);
        String baseNamespace = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + userID + ":*";
        Set<String> userKeys = jedis.keys(baseNamespace);
        if (userKeys != null) {
            for (String userKey:userKeys) {
                jedis.del(userKey);
            }
        }
    }

    /**
     * Basic structure is:
     * 
     * HSET NEON:PROFILE:{USER_ID}:{IMPORTER_NAMESPACE} then key value pairs of "{FIELD_NAME}:{IMPORTER_NAME} => {FIELD_VALUE}".
     * The importer name is included as we accept multiple sources for a single data field, each source has a priority 
     * so we can reconstruct the profile containing the highest priority value for each provided field.
     * 
     * @param profile the partially populated profile bean
     * @param importer the importer that contributed to the profile bean
     */
    @Override
    public void persistProfile(ProfileBean profile, DataImporter importer) {
    	logger.trace("Persisting for "+importer.getImporterName());
        // very complicated persistence to account for metadata, priorities and namespaces
        String userID = profile.getUserID();
        if (importer.getImporterName() == null) {
            logger.error("Could not persist profile information as the importer name was not provided.");
        } else if (importer.getSupportedNamespaces() == null || importer.getSupportedNamespaces().length==0) {
            logger.error("Could not persist profile information as the target namespace was not provided.");
        } else if (userID == null) {
            logger.error("Could not persist profile information provided by " + importer.getImporterName() + " as the userID was not provided.");
        } else {
        	String[] namespaces = importer.getSupportedNamespaces();
        	for (int i=0; i < namespaces.length; i++) {
        		logger.debug("Persisting "+namespaces[i]+" for "+importer.getImporterName());
	            NamespaceHandler handler = handlerMapping.get(namespaces[i]);
	            if (handler != null) {
	                handler.persist(profile, importer);
	            } else {
	                logger.error("Could not persist profile information provided by " + importer.getImporterName() + " as there is no handler configured for namespace " + namespaces[i]);
	            }
        	}
        }        
    }

    /**
     * Sets up handlers to persist each namespace
     * @param handlers list of handlers - if two handlers handle the same namespace the first one in the list is 
     *                 registered
     */
    public void setHandlers(List<NamespaceHandler> handlers) {
        for (NamespaceHandler handler:handlers) {
            if (!handlerMapping.containsKey(handler.getNamespace())) {
                logger.debug("Registering namespace handler " + handler.getClass().getName() + " for namespace " + handler.getNamespace());
                handlerMapping.put(handler.getNamespace(), handler);
            }
        }
    }

    public void setJedis(IPooledJedis jedis) {
        this.jedis = jedis;
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
        a1.setSourceSystem("gitlab");
        bean.getActivityStream().add(a1);

        ActivityBean a2 = new ActivityBean();
        a2.setActivityType("GIT Commit");
        a2.setActivityDescription("Committed branch: BUG-2345");
        a2.setActivityTime(new Date());
        a2.setSourceSystem("gitlab");
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
}
