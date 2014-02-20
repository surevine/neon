package com.surevine.neon.inload.importers.gitlab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.dao.impl.ImporterConfigurationDAOImpl;
import com.surevine.neon.dao.impl.ProfileDAOImpl;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.inload.importers.AbstractDataImporter;
import com.surevine.neon.model.ConnectionBean;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.ProjectActivityBean;

public class GitlabProfileImporter extends AbstractDataImporter implements DataImporter {

	private static final String IMPORTER_NAME ="GITLAB_PROFILE_IMPORTER";

	private Logger log = Logger.getLogger(GitlabProfileImporter.class);

	private transient String authentication_token="Pf4JKf6GEdeNmYEWxwBs";
	private String gitlabURLBase="http://10.66.2.254/";
	private String dateFormat="YYYY-MM-dd'T'HH:mm:ss'Z'";
	private String commitDateFormat="yyyy-MM-dd'T'HH:mm:ssXXX";
	private String profileBaseURL="api/v3/users?private_token={auth_token}&per_page=100&page={page_id}";
	private String projectBaseURL="api/v3/projects?private_token={auth_token}&per_page=100&page={page_id}";
	private String commitsBaseURL="api/v3/projects/{projectId}/repository/commits?private_token={auth_token}&per_page=100&page={page_id}";
	private String projectMembershipURLBase="api/v3/projects/{projectId}/members?private_token={auth_token}&query={username}&per_page=100";
	private String listProjectMembersURLBase="api/v3/projects/{projectId}/members?private_token={auth_token}&per_page=100&page={page_id}";
	private String commitWebURLBase="{project_name}/commit/{commit_id}";
	private String issueURLBase="api/v3/issues?private_token={auth_token}&per_page=100&page={page_id}";
	private String issueWebURLBase="{project_name}/issues/{issue_id}";

	public void setGitlabBaseURL(String base) {
		gitlabURLBase=base;
	}

	public void setGitlabAuthToken(String token) {
		authentication_token=token;
	}

	public boolean providesForNamespace(String namespace) {
		return namespace.equals(ProfileDAO.NS_PROFILE_PREFIX) || namespace.equals(ProfileDAO.NS_PROJECT_DETAILS);
	}

	public void setCommitDateFormat(String format) {
		commitDateFormat=format;
	}

	public void setProfileServiceBaseURL(String base) {
		profileBaseURL=base;
	}

	public void setIssueURLBase(String base) {
		issueURLBase=base;
	}

	public void setProjectBaseURL(String base) {
		projectBaseURL=base;
	}

	public void setCommitsBaseURL(String base) {
		commitsBaseURL=base;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat=dateFormat;
	}

	public void setCommitWebURLBase(String base) {
		commitWebURLBase=base;
	}

	public void setListProjectMembersBaseURL(String base) {
		listProjectMembersURLBase=base;
	}

	protected void runImportImplementation(String userID) {
		log.info("Loading gitlab profile data for "+userID);
		ProfileBean genericProfile=profileDAO.getProfileForUser(userID);
		getBioDetails(genericProfile, userID);
		getProjectMembershipDetails(genericProfile, userID);
		getCommits(genericProfile, userID);
		getIssues(genericProfile, userID);
	}

	protected void getIssues(ProfileBean profile, String userID) {
		log.info("Retrieving isues for user :"+userID);
		int page=1;
		while (true) {
			String url = getURLWithAuthToken(issueURLBase).replaceAll("\\{page_id\\}", Integer.toString(page++));
			JSONArray issues = new JSONArray(getRawWebData(userID, url));
			if (issues.length()==0) {
				break;
			}
			log.trace("Retrieved JSON from Gitlab issues service: "+issues);
			for (int i=0; i < issues.length(); i++) {
				JSONObject issue = issues.getJSONObject(0);

				//Work out if we're recording an event, and if so is it an authorship or an assignation?
				boolean record=false;
				boolean author=false;
				JSONObject event=null;
				if (issue.getJSONObject("author").optString("username").equalsIgnoreCase(userID)) {
					record=true;
					author=true;
					event=issue.getJSONObject("author");
				}
				else if (issue.getJSONObject("assignee").optString("username").equals(userID)) {
					record=true;
					event=issue.getJSONObject("assignee");
				}

				//If we have something to record...
				if (record) {

					String createdAtStr = event.getString("created_at");
					Date createdAt = new Date(0l);
					try {
						createdAt = new SimpleDateFormat(dateFormat).parse(createdAtStr);
					} catch (ParseException e) {
						log.warn("Could not parse project creation date", e);
					}
					StringBuilder text = new StringBuilder();
					if (author) {
						text.append("Created the issue '");
					}
					else {
						text.append("Assigned to the issue '");
					}
					text.append(issue.optString("title")).append("'");
					String projectID=Integer.toString(issue.getInt("project_id"));
					String projectName=profile.getKnownProjectName(projectID);
					String webURL = gitlabURLBase.concat(issueWebURLBase).replaceAll("\\{project_name\\}", projectName).replaceAll("\\{issue_id\\}", Integer.toString(issue.getInt("id")));
					String issueText=text.toString();
					log.debug("Issue text: "+text);
					log.debug("Issue date: "+createdAt);
					log.debug("Issue URL: "+webURL);
					profile.addProjectActivity(new ProjectActivityBean(issueText, webURL, createdAt, projectID, projectName));
				}

			}
		}
	}

	protected void getCommits(ProfileBean profile, String userID) {

		Iterator<String> projects=profile.getIDsOfActiveProjects();
		while (projects.hasNext()) {
			String projectID = projects.next();
			String projectName=profile.getKnownProjectName(projectID);
			log.info("Looking for commits for user "+userID+" in project "+projectID);
			int page=1;
			while (true) {
				String url = getURLWithAuthToken(commitsBaseURL).replaceAll("\\{projectId\\}", projectID).replaceAll("\\{page_id\\}", Integer.toString(page++));
				JSONArray commits = new JSONArray(getRawWebData(userID, url));
				if (commits.length()==0) {
					break;
				}
				log.trace("Retrieved JSON from Gitlab commit service: "+commits);

				for (int i=0; i < commits.length(); i++) {
					JSONObject commit = commits.getJSONObject(i);
					String text="Commited to '"+projectName+"' : "+getSafeJsonString(commit,  "title");
					log.debug("Text for commit: "+text);
					String createdAtStr = commit.getString("created_at");
					Date createdAt = new Date(0l);
					try {
						createdAt = new SimpleDateFormat(commitDateFormat).parse(createdAtStr);
					} catch (ParseException e) {
						log.warn("Could not parse project creation date", e);
					}
					log.debug("Date for commit: "+createdAt);
					String webURL=gitlabURLBase.concat(commitWebURLBase).replaceAll("\\{project_name\\}", projectName).replaceAll("\\{commit_id\\}", commit.getString("id"));
					log.debug("URL for commit: "+webURL);
					ProjectActivityBean pab = new ProjectActivityBean(text,  webURL, createdAt, projectID, projectName);
					profile.addProjectActivity(pab);
				}
			}

		}

	}

	protected void getBioDetails(ProfileBean profile, String userID) {

		log.info("Parsing gitlab profile service");
		int page=1;
		while (true) {
				String url = getURLWithAuthToken(profileBaseURL.replaceAll("\\{page_id\\}", Integer.toString(page++)));
				JSONArray profileJson = new JSONArray(getRawWebData(userID, url));
				if (profileJson.length()==0) {
					break;
				}
				log.trace("Retrieved JSON from Gitlab profile service: "+profileJson);

				//Iterate through user profiles until we find the user we are looking for
				for (int i=0; i<profileJson.length(); i++) {
					JSONObject user = profileJson.getJSONObject(i);
					if (user.getString("username").equalsIgnoreCase(userID)) {
						log.debug("Setting bio to: "+getSafeJsonString(user,  "bio"));
						profile.setBio(getSafeJsonString(user,  "bio"));
						log.debug("Setting name to: "+getSafeJsonString(user, "name"));
						profile.getVcard().setFn(getSafeJsonString(user,  "name"));
						log.debug("Setting gitlab ID to: "+Integer.toString(user.getInt("id")));
						profile.setAdditionalProperty("Gitlab User ID", Integer.toString(user.getInt("id")));
						break;
					}
				}
		}
	}

	protected void getProjectMembershipDetails(ProfileBean profile, String userID) {
		log.info("Parsing gitlab project service");
		int page=1;
		while (true) {
			String url = getURLWithAuthToken(projectBaseURL.replaceAll("\\{page_id\\}", Integer.toString(page++)));
			JSONArray projects = new JSONArray(getRawWebData(userID, url));
			if (projects.length()==0) {
				break;
			}
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

					ProjectActivityBean pab = new ProjectActivityBean(text, webURL, createdAt, projectID, getSafeJsonString(project,  "name"));
					profile.addProjectActivity(pab);
					captureConnections(userID, "Owner of a projet that {destination} is a member of", projectID, profile);
				}
				else {
					//Project membership
					String memberURL = getURLWithAuthToken(projectMembershipURLBase).replaceAll("\\{projectId\\}", projectID);
					JSONArray isMember = new JSONArray(getRawWebData(userID, memberURL));
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
						ProjectActivityBean pab = new ProjectActivityBean(text, webURL, createdAt, projectID, getSafeJsonString(project,  "path_with_namespace"));
						profile.addProjectActivity(pab);
						captureConnections(userID, "Member of the same project as {destination}", projectID, profile);
					}
				}

			}
		}
	}

	protected void captureConnections(String userID, String annotation, String projectID, ProfileBean profile) {
		log.info("Capturing connections");
		int page=1;
		while (true) {
			String url = getURLWithAuthToken(listProjectMembersURLBase).replaceAll("\\{projectId\\}", projectID).replaceAll("\\{page_id\\}", Integer.toString(page++));
			JSONArray members = new JSONArray(getRawWebData(userID, url));
			log.debug("Members length: "+members.length());
			if (members.length()==0) {
				break;
			}
			log.trace("Retrieved JSON from Gitlab projec members service: "+members);

			for (int i=0; i < members.length(); i++) {
				JSONObject member = members.getJSONObject(i);
				String userName = getSafeJsonString(member,  "username");
				String longName = getSafeJsonString(member,  "name");
				if (!userName.equalsIgnoreCase(userID)) {
					ConnectionBean connection = new ConnectionBean(userName, annotation.replaceAll("\\{destination\\}", longName));
					log.debug("Adding a connection for "+userID+": "+connection);
					profile.addConnection(connection);
				}
			}
		}
	}

	protected String getURLWithAuthToken(String base) {
		return gitlabURLBase.concat(base.replaceAll("\\{auth_token\\}", authentication_token));
	}

	public static void main(String arg[]) {
		GitlabProfileImporter importer = new GitlabProfileImporter();
		importer.setProfileDAO(new ProfileDAOImpl());
		importer.setConfigurationDAO(new ImporterConfigurationDAOImpl());
		importer.runImport("rich");
	}

	@Override
	public String getImporterName() {
		return IMPORTER_NAME;
	}

	@Override
	public String getNamespace() {
		return ProfileDAO.NS_PROJECT_DETAILS;
	}

	@Override
	public int getSourcePriority() {
		return 0;
	}

}