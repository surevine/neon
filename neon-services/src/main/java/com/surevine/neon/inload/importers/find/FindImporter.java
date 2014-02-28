package com.surevine.neon.inload.importers.find;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.dao.impl.ProfileDAOImpl;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.inload.importers.AbstractDataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.VCardTelBean;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class FindImporter extends AbstractDataImporter implements DataImporter {
	private static final String IMPORTER_NAME = "FIND_REALLY_FAST_PROFILE_IMPORTER";
	private Logger log = Logger.getLogger(FindImporter.class);
	private String URLBase="http://10.66.2.127:8080/neon-services/example_fsrf.json?user={username}";
	private String dateFormat="YYYY-MM-dd";
	
	@Override
	public void updateSpecificConfiguration(Map<String, String> config) {
		if (config.containsKey("urlBase")) {
			URLBase=config.get("urlBase");
		}
		if (config.containsKey("dateFormat")) {
			dateFormat=config.get("dateFormat");
		}
		log.debug("URLBase for "+IMPORTER_NAME+": "+URLBase);
		log.debug("Date Format for "+IMPORTER_NAME+": "+dateFormat);
	}

	public static void main(String arg[]) {
		FindImporter importer = new FindImporter();
		importer.setProfileDAO(new ProfileDAOImpl());
		importer.runImportImplementation("simonw");
	}

    @Override
    protected void runImportImplementation(String userID) {
        log.info("Getting FSRF profile for "+userID);
        String raw = getRawWebData(userID, URLBase);
        log.debug("Parsing raw data as JSON");
        JSONObject findProfile = new JSONObject(raw);
        log.debug("Parsed FSRF into "+findProfile);
        ProfileBean genericProfile = new ProfileBean();
        genericProfile.setUserID(userID);
        
        try {
	        String managerSid = findProfile.getJSONObject("manager").getString("userid");
	        log.debug("Setting manager SID to: "+managerSid);
	        genericProfile.setAdditionalProperty("Manager SID", managerSid);
        }
        catch (JSONException e) {
        	log.info("Manager information not provided: "+e.getMessage());
        }

        try {
	        String PF = findProfile.getString("pfNumber");
	        log.debug("Setting PF to: "+PF);
	        genericProfile.setAdditionalProperty("PF Number", PF);
        }
        catch (JSONException e) {
        	log.info("PF Information not available: "+e.getMessage());
        }
        
        try {
	        String employeeType = findProfile.getString("typeOfEmployment");
	        log.debug("Setting employee type to: "+employeeType);
	        genericProfile.setAdditionalProperty("Employee Type", employeeType);
        }
        catch (JSONException e) {
        	log.info("Employee type information not available: "+e.getMessage());
        }
        
        try {
        	String userName = findProfile.getString("displayName");
        	log.debug("Setting name to: "+userName);
        	genericProfile.getVcard().setFn(userName);
        }
        catch (JSONException e) {
        	log.info("Username information not available: "+e.getMessage());
        }

        try {
        	String businessShortTitle = findProfile.getString("bst");
        	log.debug("Setting business short title to: "+businessShortTitle);
        	genericProfile.setAdditionalProperty("Business Short Title", businessShortTitle);
        }
        catch (JSONException e) {
        	log.info("BST information not available: "+e.getMessage());
        }

        try {
            String datePresenceLastUpdatedStr=findProfile.getJSONArray("sightings").getJSONObject(0).getJSONObject("presence").getString("date");
            log.trace("String value of last updated timestamp: "+datePresenceLastUpdatedStr+". Converting to date");
            Date datePresenceLastUpdated = new SimpleDateFormat(dateFormat).parse(datePresenceLastUpdatedStr);
            log.debug("Setting date presence last updated to: "+datePresenceLastUpdated);
            genericProfile.getStatus().setPresenceLastUpdated(datePresenceLastUpdated);
        }
        catch (ParseException e) {
            log.warn("Could not parse the last presence updated value", e);
        }
        catch (JSONException e) {
        	log.info("Presence date information not available: "+e.getMessage());
        }

        try {
        	String currentLocation = findProfile.getJSONArray("sightings").getJSONObject(0).getJSONObject("desk").getString("name");
        	log.debug("Setting current location to: "+currentLocation);
        	genericProfile.getStatus().setLocation(currentLocation);


        	String currentLocationDetails =findProfile.getJSONArray("sightings").getJSONObject(0).getJSONObject("desk").toString();
        	log.debug("Setting current location fine details to "+currentLocationDetails);
        	genericProfile.setAdditionalProperty("Current Location Fine Details", currentLocationDetails);
        }
        catch (JSONException e) {
        	log.info("Current Location not available: "+e.getMessage());
        }

        try {
        JSONArray phones = findProfile.getJSONArray("phones");
	        for (int i=0; i < phones.length(); i++) {
	            VCardTelBean telephone = new VCardTelBean();
	            telephone.setNumber(phones.getJSONObject(i).getString("number"));
	            telephone.setType(phones.getJSONObject(i).getString("type"));
	            log.debug("Adding a telephone number: "+telephone);
	            genericProfile.getVcard().getTelephoneNumbers().add(telephone);
	        }
        }
        catch (JSONException e) {
        	log.info("Could not import all telephone information: "+e.getMessage());
        }

        try {
	        String usualLocation = findProfile.getString("roomNumber");
	        log.debug("Setting usual location to: "+usualLocation);
	        genericProfile.setAdditionalProperty("Typical Location", usualLocation);
        }
        catch (JSONException e) {
        	log.info("Room Number not available: "+e.getMessage());
        }
        
        try {
        	String bio=findProfile.getString("description");
        	log.debug("Setting bio to: "+bio);
        	genericProfile.setBio(bio);
        }
        catch (JSONException e) {
        	log.info("Bio not available: "+e.getMessage());
        }
        log.trace("Finished importing of FSRF record.  Persisting");

        profileDAO.persistProfile(genericProfile, this);
        log.info("Import of "+userID+" from FSRF complete");
    }

    @Override
    public String getImporterName() {
        return IMPORTER_NAME;
    }

    @Override
    public String getNamespace() {
        return ProfileDAO.NS_BASIC_DETAILS;
    }
}
