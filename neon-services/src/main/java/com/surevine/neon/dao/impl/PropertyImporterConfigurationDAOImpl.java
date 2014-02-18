package com.surevine.neon.dao.impl;

import java.util.Map;
import com.surevine.neon.dao.ImporterConfigurationDAO;

/**
 * For mocking / debugging purposes only
 * @author simonw
 *
 */
public class PropertyImporterConfigurationDAOImpl implements ImporterConfigurationDAO {

	@Override
	public void addImporterConfiguration(String importerName, Map<String, String> config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addImporterConfigurationOption(String importerName, String key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String> getConfigurationForImporter(String importerName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringConfigurationOption(String importerName, String configurationKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBooleanConfigurationOption(String importerName, String configurationKey) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setConfigurationOption(String importerName, String configurationKey, String value) {
		// TODO Auto-generated method stub
		
	}


}
