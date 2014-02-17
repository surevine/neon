package com.surevine.neon.inload.importers;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import org.apache.log4j.Logger;

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
    	if (name==null || this.name.equals(name)) {
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

}
