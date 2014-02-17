package com.surevine.neon.inload.importers.Gitlab;

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
import com.surevine.neon.inload.importers.mediawiki.MediaWikiProfile;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;

public class GitlabProfileImporter extends AbstractDataImporter implements DataImporter {

	{ setName("GITLAB_PROFILE_IMPORTER"); }

	private Logger log = Logger.getLogger(GitlabProfileImporter.class);

	private transient String authentication_token="Pf4JKf6GEdeNmYEWxwBs";
	private String dateFormat="YYYY-MM-dd'T'HH:mm:ss'Z'";
	private String profileBaseURL="http://10.66.2.254/api/v3/users?private_token={auth_token}";
	private String projectBaseURL="http://10.66.2.254/api/v3/projects?private_token={auth_token}";
	private String snippetBaseURL="http://10.66.2.254/api/v3/project_snippets?private_token={auth_token}";
	private String projectMembershipURLBase="http://10.66.2.254/api/v3/projects/{projectId}/members?private_token={auth_token}&query={username}";
	
	public void setGitlabAuthToken(String token) {
		authentication_token=token;
	}
	
	@Override
	public boolean providesForNamespace(String namespace) {
		return namespace.equals(ProfileDAO.NS_PROFILE_PREFIX) || namespace.equals(ProfileDAO.NS_PROJECT_DETAILS);
	}
	
	public void setProfileServiceBaseURL(String base) {
		profileBaseURL=base;
	}
	
	public void setProjectBaseURL(String base) {
		projectBaseURL=base;
	}
	
	public void setSnippetBaseURL(String base) {
		snippetBaseURL=base;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat=dateFormat;
	}

	@Override
	public void inload(String userID) {
		log.info("Loading gitlab profile data for "+userID);
		ProfileBean genericProfile=profileDAO.getProfileForUser(userID);
		getBioDetails(genericProfile, userID);
		getProjectMembershipDetails(genericProfile, userID);
		getProjectSnippetDetails(genericProfile, userID);
	}
	
	protected void getProjectSnippetDetails(ProfileBean profile, String userID) {
		log.info("Parsing gitlab snippet service");
		String url = getURLWithAuthToken(snippetBaseURL);
		//JSONArray profileJson = new JSONArray(getRawWebData(userID, url));
		//log.trace("Retrieved JSON from Gitlab snippet service: "+profileJson);
	}
	
	protected void getBioDetails(ProfileBean profile, String userID) {
		log.info("Parsing gitlab profile service");
		String url = getURLWithAuthToken(profileBaseURL);
		JSONArray profileJson = new JSONArray(getRawWebData(userID, url));
		log.trace("Retrieved JSON from Gitlab profile service: "+profileJson);
		
		//Iterate through user profiles until we find the user we are looking for
		for (int i=0; i<profileJson.length(); i++) {
			JSONObject user = profileJson.getJSONObject(i);
			if (user.getString("username").equalsIgnoreCase(userID)) {
				log.debug("Setting bio to: "+getSafeJsonString(user,  "bio"));
				profile.setBio(getSafeJsonString(user,  "bio"));
				log.debug("Setting name to: "+getSafeJsonString(user, "name"));
				profile.getVcard().setFn(getSafeJsonString(user,  "name"));
				log.debug("Setting gitlab ID to: "+getSafeJsonString(user,  "id"));
				profile.setAdditionalProperty("Gitlab User ID", getSafeJsonString(user,  "id"));
				break;
			}
		}
	}
	
	protected void getProjectMembershipDetails(ProfileBean profile, String userID) {
		log.info("Parsing gitlab project service");
		String url = getURLWithAuthToken(projectBaseURL);
		JSONArray projects = new JSONArray(getRawWebData(userID, url));
		log.trace("Retrieved JSON from Gitlab project service: "+projects);
		
		for (int i=0; i < projects.length(); i++) {
			JSONObject project = projects.getJSONObject(i);
			String projectID=Integer.toString(project.getInt("id"));
			log.debug("Project ID: "+projectID);
			
			//Project ownership
			if (project.getJSONObject("owner").getString("username").equalsIgnoreCase(userID)) {
				String createdAtStr = project.getJSONObject("owner").getString("created_at");
				Date createdAt = new Date(0l);
				try {
					createdAt = new SimpleDateFormat(dateFormat).parse(createdAtStr);
				} catch (ParseException e) {
					log.warn("Could not parse project creation date", e);
				}
				log.debug("Owner of project created on: "+createdAt);
				String webURL = getSafeJsonString(project,  "web_url");
				log.debug("Owner of project with URL: "+webURL);
				String text = "Owner of the '"+getSafeJsonString(project,  "name")+"' project";
				log.debug("Owner of project text: "+text);
				
				ProjectActivityBean pab = new ProjectActivityBean(text, webURL, createdAt, projectID);
				profile.addProjectActivity(pab);
				captureConnections(userID, "Owner of a projet where {destination} is a member", projectID);
			}
			else {
				//Project membership
				String memberURL = getURLWithAuthToken(projectMembershipURLBase).replaceAll("\\{projectId\\}", projectID);
				JSONArray isMember = new JSONArray(getRawWebData(userID, getURLWithAuthToken(memberURL)));
				if (isMember.length()>0) {
					String createdAtStr = isMember.getJSONObject(0).getString("created_at");
					Date createdAt = new Date(0l);
					try {
						createdAt = new SimpleDateFormat(dateFormat).parse(createdAtStr);
					} catch (ParseException e) {
						log.warn("Could not parse project creation date", e);
					}
					log.debug("Member of project on: "+createdAt);
					String webURL = getSafeJsonString(project,  "web_url");
					log.debug("Memeber of a project with URL: "+webURL);
					String text="Member of the '"+getSafeJsonString(project,  "name")+"' project";
					log.debug("Memeber of a project text: "+text);
					ProjectActivityBean pab = new ProjectActivityBean(text, webURL, createdAt, projectID);
					profile.addProjectActivity(pab);
					captureConnections(userID, "Member of the same project as {destination}", projectID);
				}
			}
			
		}
	}
	
	protected void captureConnections(String userID, String string, String projectID) {
		// TODO Auto-generated method stub
		
	}

	protected String getURLWithAuthToken(String base) {
		return base.replaceAll("\\{auth_token\\}", authentication_token);
	}
	
	public static void main(String arg[]) {
		GitlabProfileImporter importer = new GitlabProfileImporter();
		importer.setProfileDAO(new ProfileDAOImpl());
		importer.inload("root");
	}

}
