package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.inload.FeedRegistry;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.util.Properties;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProfileDAOImpl implements ProfileDAO {
    private Logger logger = Logger.getLogger(ProfileDAOImpl.class);
    private Map<String,NamespaceHandler> handlerMapping = new HashMap<String, NamespaceHandler>();
    private IPooledJedis jedis;
        
    @Override
    public ProfileBean getProfileForUser(String userID) {
        // If dev.use_mock_profile application property is true OR the argument userID = mockuser then the mock profile is returned
        ProfileBean bean = new ProfileBean();
        bean.setUserID(userID);
        
        if (Properties.getProperties().isUseMockProfile()) {
            bean.setUserID(MOCK_USER_ID);
        }
        
        // run handlers to load persistent information held about the user
        for (NamespaceHandler handler:handlerMapping.values()) {
            handler.load(bean);
        }
        
        // augment profile data with any live feeds
        FeedRegistry.getInstance().augmentProfileWithFeeds(bean);
        return bean;
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
}
