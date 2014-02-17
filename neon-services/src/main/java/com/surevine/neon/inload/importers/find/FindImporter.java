package com.surevine.neon.inload.importers.find;

import java.util.Map;
import java.util.Set;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;

public class FindImporter implements DataImporter {

	private static final String IMPORTER_NAME="FIND_REALLY_FAST_PROFILE_IMPORTER";
	
	@Override
	public String getImporterName() {
		return IMPORTER_NAME;
	}

	@Override
	public boolean providesForNamespace(String namespace) {
        return ProfileDAO.NS_PROFILE_PREFIX.equals(namespace);
	}

	@Override
	public void setConfiguration(Map<String, String> configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public void inload(String userID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void inload(Set<String> userIDs) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

}
