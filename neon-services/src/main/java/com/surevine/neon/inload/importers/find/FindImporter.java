package com.surevine.neon.inload.importers.find;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.dao.impl.ProfileDAOImpl;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.inload.importers.AbstractDataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.VCardTelBean;

public class FindImporter extends AbstractDataImporter implements DataImporter {

	{ setName("FIND_REALLY_FAST_PROFILE_IMPORTER");	}
	
	private Logger log = Logger.getLogger(FindImporter.class);
	private String URLBase="http://10.66.2.127:8080/neon-services/example_fsrf.json?user={username}";
	private String dateFormat="YYYY-MM-dd";
	
	@Override
	public boolean providesForNamespace(String namespace) {
        return ProfileDAO.NS_PROFILE_PREFIX.equals(namespace);
	}
	
	public void setURLBase(String newBase) {
		URLBase=newBase;
	}
	
	public void setTimestampFormat(String format) {
		dateFormat=format;
	}

	@Override
	public void inload(String userID) {
		log.info("Getting FSRF profile for "+userID);
		String raw = getRawWebData(userID, URLBase);
		log.debug("Parsing raw data as JSON");
		JSONObject findProfile = new JSONObject(raw);
		log.debug("Parsed FSRF into "+findProfile);
		ProfileBean genericProfile = profileDAO.getProfileForUser(userID);
		
		String managerSid = findProfile.getJSONObject("manager").getString("userid");
		log.debug("Setting manager SID to: "+managerSid);
		genericProfile.setAdditionalProperty("Manager SID", managerSid);
		
		String PF = findProfile.getString("pfNumber");
		log.debug("Setting PF to: "+PF);
		genericProfile.setAdditionalProperty("PF Number", PF);
		
		String employeeType = findProfile.getString("typeOfEmployment");
		log.debug("Setting employee type to: "+employeeType);
		genericProfile.setAdditionalProperty("Employee Type", employeeType);
		
		String userName = findProfile.getString("displayName");
		log.debug("Setting name to: "+userName);
		genericProfile.getVcard().setFn(userName);
		
		String businessShortTitle = findProfile.getString("bst");
		log.debug("Setting business short title to: "+businessShortTitle);
		genericProfile.setAdditionalProperty("Business Short Title", businessShortTitle);
		
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
		
		String currentLocation = findProfile.getJSONArray("sightings").getJSONObject(0).getJSONObject("desk").getString("name");
		log.debug("Setting current location to: "+currentLocation);
		genericProfile.getStatus().setLocation(currentLocation);
		
		String currentLocationDetails =findProfile.getJSONArray("sightings").getJSONObject(0).getJSONObject("desk").toString();
		log.debug("Setting current location fine details to "+currentLocationDetails);
		genericProfile.setAdditionalProperty("Current Location Fine Details", currentLocationDetails);
		
		JSONArray phones = findProfile.getJSONArray("phones");
		for (int i=0; i < phones.length(); i++) {
			VCardTelBean telephone = new VCardTelBean();
			telephone.setNumber(phones.getJSONObject(i).getString("number"));
			telephone.setType(phones.getJSONObject(i).getString("type"));
			log.debug("Addint a telephone number: "+telephone);
			genericProfile.getVcard().getTelephoneNumbers().add(telephone);
		}
		
		String usualLocation = findProfile.getString("roomNumber");
		log.debug("Setting usual location to: "+usualLocation);
		genericProfile.setAdditionalProperty("Typical Location", usualLocation);
		
		String bio=findProfile.getString("description");
		log.debug("Setting bio to: "+bio);
		genericProfile.setBio(bio);
		
		log.trace("Finished importing of FSRF record.  Persisting");
		
		profileDAO.persistProfile(genericProfile, this);
		log.info("Import of "+userID+" from FSRF complete");
	}
	
	public static void main(String arg[]) {
		FindImporter importer = new FindImporter();
		importer.setProfileDAO(new ProfileDAOImpl());
		importer.inload("simonw");
	}
	


}
