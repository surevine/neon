package com.surevine.neon.inload.importers.jive;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.dao.impl.InMemoryProfileDAOImpl;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.inload.importers.AbstractDataImporter;
import com.surevine.neon.model.ConnectionBean;
import com.surevine.neon.model.ProfileBean;

public class JiveImporter extends AbstractDataImporter implements DataImporter {
	protected static final String IMPORTER_NAME="JIVE_PROFILE_IMPORTER";
	private Logger logger = Logger.getLogger(JiveImporter.class);
	
	private String hostname="10.66.2.89";
	private String jiveUrlBase="http://{hostname}/api/core/v3/";
	private String peopleService="people/username/{username}";
	private String followersService="people/{username}/@followers"; //Note that username here is jive ID not the sid
	private String followingService="people/{username}/@following"; //Note that username here is jive ID not the sid


	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void setJiveUrlBase(String jiveUrlBase) {
		this.jiveUrlBase = jiveUrlBase;
	}

	public void setPeopleService(String peopleService) {
		this.peopleService = peopleService;
	}

	public void setFollowersService(String followersService) {
		this.followersService = followersService;
	}

	public void setFollowingService(String followingService) {
		this.followingService = followingService;
	}

	@Override
	public String getImporterName() {
		return IMPORTER_NAME;
	}

    @Override
    public String getNamespace() {
        return ProfileDAO.NS_BASIC_DETAILS;
    }

	@Override
	protected void runImportImplementation(String userID) {
		logger.debug("Importing Jive profile for "+userID);
        ProfileBean genericProfile = profileDAO.getProfileForUser(userID);
        populateFromPeopleService(genericProfile, userID);
        populateFromFollowersService(genericProfile, userID);
	}
	
	protected void populateFromFollowersService(ProfileBean profile, String userID) {
		String jiveID = profile.getAdditionalProperties().get("Internal Jive ID");
		if (jiveID!=null) {
			Set<String> followingUsers = getFollowingUsers(jiveID);
			Set<String> followerUsers = getFollowerUsers(jiveID);
			
			Iterator<String> following = followingUsers.iterator();
			while (following.hasNext()) {
				String userName = following.next();
				if (followerUsers.contains(userName)) { // A mututal relationship
					ConnectionBean connection = new ConnectionBean(userName, profile.getVcard().getFn()+" and "+userName+" follow each other in Jive");
					logger.debug("Created the connection: "+connection);
					profile.addConnection(connection);
				}
				else { //X only follows Y
					ConnectionBean connection = new ConnectionBean(userName, profile.getVcard().getFn()+" follows "+userName+" in Jive");
					logger.debug("Created the connection: "+connection);
					profile.addConnection(connection);
				}
			}
			
			Iterator<String> followers = followerUsers.iterator();
			while (followers.hasNext()) {
				String userName = followers.next();
				if (followingUsers.contains(userName)) { //Mutual relationship we will have already captured above
					continue;
				}
				else { //Y only follows X
					ConnectionBean connection = new ConnectionBean(userName, profile.getVcard().getFn()+" is followed by "+userName+" in Jive");
					logger.debug("Created the connection: "+connection);
					profile.addConnection(connection);
				}
			}
			
		}
		else {
			logger.warn("Could not find a Jive ID for "+userID+" so no connections could be imported");
		}
	}
	
	protected Set<String> getFollowingUsers(String jiveID) {
		return getFollowsImpl(jiveID, false);
	}
	
	protected Set<String> getFollowerUsers(String jiveID) {
		return getFollowsImpl(jiveID, true);
	}
	
	private Set<String> getFollowsImpl(String jiveID, boolean following) {
		logger.debug("Retrieving followers for "+jiveID+" : "+following);
		Set<String> rV = new HashSet<String>();
		
		String service="";
		if (following) {
			service=followingService;
		}
		else {
			service=followersService;
		}
		
		String targetURL=generateURL(service);
		String followWebContent = getRawWebData(jiveID, targetURL).replaceAll("throw 'allowIllegalResourceCall is false.';", "");
		logger.trace("Plain text content: "+followWebContent);
		JSONObject follows = new JSONObject(followWebContent);
		JSONArray list = follows.getJSONArray("list");
		for (int i=0; i < list.length(); i++) {
			String userName = list.getJSONObject(i).getJSONObject("jive").getString("username");
			logger.trace("Found a follow: "+userName);
			rV.add(userName);
		}
		return rV;
	}
	
	protected void populateFromPeopleService(ProfileBean profile, String userID) {
		logger.debug("Populating from people service for user "+userID);
		String targetURL=generateURL(peopleService);
		String peopleWebContent=getRawWebData(userID, targetURL).replaceAll("throw 'allowIllegalResourceCall is false.';", "");
		logger.trace("Plain text content: "+peopleWebContent);
		JSONObject person = new JSONObject(peopleWebContent);
		
		String name=person.getJSONObject("name").getString("formatted");
		logger.debug("Setting name to "+name);
		profile.getVcard().setFn(name);
		
		String avatar=person.getString("thumbnailUrl");
		try {
			profile.getVcard().getPhoto().setPhotoURL(new URL(avatar));
			logger.debug("Setting avatar to "+avatar);
		}
		catch (MalformedURLException e) {
			logger.warn("Could not create a profile URL from: "+avatar);
		}
		
		String jiveLevel = person.getJSONObject("jive").getJSONObject("level").getString("name")+" ("+person.getJSONObject("jive").getJSONObject("level").getInt("points")+" points)";
		logger.debug("Setting jive level to: "+jiveLevel);
		profile.setAdditionalProperty("Jive Level", jiveLevel);
		
		//Now we parse the jive profile fields
		//VCard only has organisation and dept so we're going to concatanate the two here and set them at the end
		String org="";
		String dept="";
		JSONArray profileFields = person.getJSONObject("jive").getJSONArray("profile");
		for (int i=0; i < profileFields.length(); i++) {
			String value = profileFields.getJSONObject(i).getString("value");
			String label = profileFields.getJSONObject(i).getString("jive_label");
			
			if (label.equals("Title")) {
				logger.debug("Setting title to: "+value);
				profile.getVcard().setTitle(value);
			}
			else if (label.equals("Department")) {
				logger.debug("Found a department: "+value+".  Will set it later");
				dept=value;
			}
			else if (label.equals("Company")) {
				logger.debug("Found a company: "+value+".  Will set it later");
				org=value;
			}
			else if (label.equals("Biography")) {
				logger.debug("Setting bio to: "+value);
				profile.setBio(value);
			}
			else if (label.equals("Expertise")) { //Although this seems like 'skills' it's just plain text so we have to append it to the bio
				String bio = profile.getBio();
				String newBio="";
				if (bio==null || bio.equals("")) {
					newBio=value;
				}
				else {
					newBio=bio+" <br/> "+value;
				}
				logger.debug("Setting bio from expertise to: "+newBio);
				profile.setBio(value);
			}
		}
		if (!(org.equals("") && dept.equals(""))) {
			String vCardOrg="";
			if (org.equals("")) {
				vCardOrg=dept;
			}
			else if (dept.equals("")) {
				vCardOrg=org;
			}
			else {
				vCardOrg=org+" - "+dept;
			}
			logger.debug("Setting organisation to: "+vCardOrg);
			profile.getVcard().setOrg(vCardOrg);
		}
		//End of parsing the "jive" object
		
		JSONArray emails = person.getJSONArray("emails");
		for (int i=0; i < emails.length(); i++) {
			boolean primary = emails.getJSONObject(i).getBoolean("primary");
			if (primary || i==emails.length()) { //get the primary email but if none is set just get the last one (essentially, a random one)
				String email = emails.getJSONObject(i).getString("value");
				logger.debug("Setting e-mail to: "+email);
				profile.getVcard().setEmail(email);
				break;
			}
		}
		
		String jiveID=person.getString("id");
		logger.debug("Setting Jive ID to: "+jiveID);
		profile.setAdditionalProperty("Internal Jive ID", jiveID);
	}
	
	protected String generateURL(String service) {
		String fullPattern=jiveUrlBase+service;
		return fullPattern.replaceAll("\\{hostname\\}", hostname);
	}
	
	public static void main(String arg[]) {
		JiveImporter importer = new JiveImporter();
		importer.setProfileDAO(new InMemoryProfileDAOImpl());
		importer.setUsername("admin");
		importer.setPassword("admin");
		importer.runImportImplementation("admin");
	}

}
