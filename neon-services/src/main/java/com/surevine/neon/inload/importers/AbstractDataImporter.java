package com.surevine.neon.inload.importers;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.util.DateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Abstract data importer contains all the basic functionality required for importer configuration and state management.
 * Most importers should extend this unless they have good reason not to.
 */
public abstract class AbstractDataImporter implements DataImporter {
	private ImporterConfigurationDAO configurationDAO;
    private Logger log = Logger.getLogger(AbstractDataImporter.class);
    protected ProfileDAO profileDAO;

    /**
     * Delegated import implementation
     * @param userID the user ID to run the import for
     */
    protected abstract void runImportImplementation(String userID);

    /**
     * Sets the implementation of the profile DAO
     * @param pd the profile DAO
     */
    public void setProfileDAO(ProfileDAO pd) {
        profileDAO=pd;
    }

    /**
     * Sets the configuration DAO instance
     * @param configurationDAO the configuration DAO
     */
    public void setConfigurationDAO(ImporterConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }

    @Override
    public void setAdditionalConfiguration(Map<String, String> configuration) {
        configurationDAO.addImporterConfiguration(getImporterName(), configuration);
    }

    @Override
    public int getSourcePriority() {
        return Integer.parseInt(configurationDAO.getStringConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_PRIORITY));
    }

    @Override
    public void runImport() {
        Set<String> userIDs = profileDAO.getUserIDList();
		log.info("Retrieving data for " + userIDs.size() + " users");
		Iterator<String> userID = userIDs.iterator();
		while (userID.hasNext()) {
			runImport(userID.next());
		}
	}

    @Override
    public void runImport(String userID) {
        try {
            runImportImplementation(userID);
            configurationDAO.addImporterConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_LAST_IMPORT, DateUtil.dateToString(new Date()));
        } catch (DataImportException die) {
            log.warn("Importer " + getImporterName() + " failed to import data for user " + userID + "[" + die.getMessage() + "]");
        }
    }

    @Override
    public Date getLastRun() {
        String lastRunString = configurationDAO.getStringConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_LAST_IMPORT);
        Date lastRun = null;
        
        if (lastRunString != null) {
            lastRun = DateUtil.stringToDate(lastRunString);
        }
        
        return lastRun;
    }

    @Override
    public boolean cacheLapsed() {
        Date lastRun = getLastRun();
        if (lastRun == null) {
            return true;
        }
        
        int cacheTimeout = Integer.parseInt(configurationDAO.getStringConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_IMPORTER_TIMEOUT));
        Long diffInMillis = new Date().getTime() - lastRun.getTime();
        
        return diffInMillis > (cacheTimeout * 1000);
    }

    @Override
    public boolean isEnabled() {
        return configurationDAO.getBooleanConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_ENABLED);
    }

    @Override
    public void setCacheTimeout(int timeout) {
        configurationDAO.addImporterConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_IMPORTER_TIMEOUT, timeout + "");
    }

    @Override
    public void setSourcePriority(int priority) {
        configurationDAO.addImporterConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_PRIORITY, priority + "");
    }

    @Override
    public void setEnabled(boolean enabled) {
        configurationDAO.addImporterConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_ENABLED, Boolean.toString(enabled));
    }

    protected String getRawWebData(String userID, String urlBase) {
		log.info("Getting raw web content for "+userID);
		String rV=null;
		InputStream webIn=null;
		URL targetURL=null;
		try {
			targetURL=new URL(urlBase.replaceAll("\\{username\\}", userID));
			log.trace("Target URL for import: "+targetURL.toString());
			webIn = targetURL.openStream();
			rV=IOUtils.toString(webIn);
		}
		catch (MalformedURLException e) {
			log.error(e);
			throw new DataImportException(userID, this, "Could not generate a profile URL from "+urlBase, e);
		}
		catch (IOException ioe) {
			log.error(ioe);
			throw new DataImportException(userID, this, "Could not retrieve profile page "+targetURL, ioe);
		}
		finally {
			IOUtils.closeQuietly(webIn);
		}
		if (rV==null || rV.trim().equals("")) {
			throw new DataImportException(userID, this, "No data could be found for the user profile at "+targetURL);
		}
		if(log.isDebugEnabled()) {
			log.debug("Retrieved "+rV.length()+" charecters of profile data");
		}
		if (log.isTraceEnabled()) {
			log.trace("Raw profile data:  |"+rV+"|");
		}
		return rV;
	}
	
	protected String getSafeJsonString(JSONObject o, String key) {
		try {
			return o.getString(key);
		}
		catch (JSONException e) {
			log.warn("Could not retrieve the key "+key+" from "+o, e);
			return null;
		}
	}
}