package com.surevine.neon.inload.importers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.inload.importers.mediawiki.WikiProfileImporter;

public abstract class AbstractDataImporter implements DataImporter {

	private String name=null;

	private ImporterConfigurationDAO configurationDAO;
    private Logger log = Logger.getLogger(AbstractDataImporter.class);
    protected ProfileDAO profileDAO;

    public void setName(String name) {
    	if (this.name==null || this.name.equals(name)) {
    		this.name=name;
    	}
    	else {
    		throw new DataImportException("system", this, "A profile importer cannot be renamed, and this importer was already called "+this.name);
    	}
    }
    
	public void setProfileDAO(ProfileDAO pd) {
		profileDAO=pd;
	}
	
	public ProfileDAO getProfileDAO() {
		return profileDAO;
	}
    
	@Override
	public String getImporterName() {
		return name;
	}

	@Override
	public abstract boolean providesForNamespace(String namespace);

    @Override
    public void setConfiguration(Map<String, String> configuration) {
        configurationDAO.configureImporter(name, configuration);
    }

	@Override
	public abstract void inload(String userID);

	@Override
	public void inload(Set<String> userIDs) {
		log.info("Retrieving wiki profile data for "+userIDs.size()+" users");
		Iterator<String> userIt = userIDs.iterator();
		while (userIt.hasNext()) {
			inload(userIt.next());
		}
	}

    @Override
    public boolean isEnabled() {
        return configurationDAO.getBooleanConfigurationOption(name, "enabled");
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