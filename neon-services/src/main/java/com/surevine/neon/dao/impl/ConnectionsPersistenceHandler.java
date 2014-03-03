package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ConnectionBean;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.util.Properties;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Handles persistence of NS_CONNECTIONS fields
 */
public class ConnectionsPersistenceHandler extends AbstractNamespaceHandler implements NamespaceHandler {
    public static final String FIELD_CONNECTION = "CONNECTION";
    
    private Logger logger = Logger.getLogger(ConnectionsPersistenceHandler.class);
    
    @Override
    public String getNamespace() {
        return ProfileDAO.NS_CONNECTIONS;
    }

    @Override
    public void persist(ProfileBean profile, DataImporter importer) {
    	logger.debug("Persisting connections of "+profile.getUserID()+" from "+importer.getImporterName());

        String hsetKey = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + getNamespace();
        Set<ConnectionBean> conns = profile.getConnections();
        logger.debug("Found "+conns.size()+" connections to persist");
        if (conns.size() > 0) {
            setMultipleField(hsetKey, FIELD_CONNECTION, importer.getImporterName(), conns);
        }
    }

    @Override
    public void load(ProfileBean profile) {
        if (profile.getUserID() != null) {
            String baseNamespace = Properties.getProperties().getSystemNamespace() + ":" + ProfileDAO.NS_PROFILE_PREFIX + ":" + profile.getUserID() + ":" + ProfileDAO.NS_CONNECTIONS;
            Map<String,String> profileData = jedis.hgetAll(baseNamespace);

            Collection<String> connStrings = getMultipleField(profileData,FIELD_CONNECTION,profile);
            for (String conn:connStrings) {
                ConnectionBean bean = new ConnectionBean();
                bean.populateFromString(conn);
                profile.getConnections().add(bean);
            }
        }
    }
}
