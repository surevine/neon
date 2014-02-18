package com.surevine.neon.inload.importers;

import java.util.Iterator;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.dao.impl.InMemoryProfileDAOImpl;
import com.surevine.neon.dao.impl.PropertyImporterConfigurationDAOImpl;
import com.surevine.neon.inload.importers.find.FindImporter;
import com.surevine.neon.inload.importers.gitlab.GitlabProfileImporter;
import com.surevine.neon.inload.importers.mediawiki.WikiProfileImporter;
import com.surevine.neon.model.ActivityBean;
import com.surevine.neon.model.ConnectionBean;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.SkillBean;
import com.surevine.neon.model.StatusBean;
import com.surevine.neon.model.VCardBean;
import com.surevine.neon.model.VCardTelBean;

public class Demonstrator {
	
	public static void main (String arg[]) {
		
		if (arg.length!=1) {
			System.out.println("Expects one paramater - user ID");
			return;
		}
		
		String userID=arg[0];
		
		ProfileDAO dao = new InMemoryProfileDAOImpl();
		ImporterConfigurationDAO config = new PropertyImporterConfigurationDAOImpl();
		
		FindImporter find = new FindImporter();
		String findBase=System.getProperty("find");
		if (findBase!=null) {
			find.setURLBase(findBase);
		}
		find.setProfileDAO(dao);
		find.setConfigurationDAO(config);
		
		GitlabProfileImporter gitlab = new GitlabProfileImporter();
		String gitlabHostname=System.getProperty("gitlab_url");
		if (gitlabHostname!=null) {
			gitlab.setGitlabBaseURL(gitlabHostname);
		}
		String gitlabAuth=System.getProperty("gitlab_auth");
		if (gitlabAuth!=null) {
			gitlab.setGitlabAuthToken(gitlabAuth);
		}
		gitlab.setProfileDAO(dao);
		gitlab.setConfigurationDAO(config);
		
		WikiProfileImporter wiki = new WikiProfileImporter();
		String wikiBase=System.getProperty("wiki");
		if (wikiBase!=null) {
			wiki.setMediaWikiProfilePage(wikiBase);
		}
		wiki.setProfileDAO(dao);
		wiki.setConfigurationDAO(config);
		
		find.runImport(userID);
		gitlab.runImport(userID);
		wiki.runImport(userID);
		
		ProfileBean profile = dao.getProfileForUser(userID);
		VCardBean vcard = profile.getVcard();
		StatusBean status = profile.getStatus();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<html><body><table><tr><th>Property Name</th><th>Property Value</th></tr>\n");
		sb.append("<tr><td>User ID").append("<td>").append(profile.getUserID()).append("</td></tr>\n");
		sb.append("<tr><td>VCard - FN").append("<td>").append(vcard.getFn()).append("</td></tr>\n");
		sb.append("<tr><td>VCard - Organisation").append("<td>").append(vcard.getOrg()).append("</td></tr>\n");
		sb.append("<tr><td>VCard - Job Title").append("<td>").append(vcard.getTitle()).append("</td></tr>\n");
		sb.append("<tr><td>VCard - Photo").append("<td>").append(vcard.getPhoto().getPhotoURL()).append("</td></tr>\n");
		sb.append("<tr><td>VCard - Telephone Numbers").append("<td>");
		Iterator<VCardTelBean> tels = vcard.getTelephoneNumbers().iterator();
		while (tels.hasNext()) {
			VCardTelBean tel = tels.next();
			sb.append("<div>").append(tel.getType()).append(": ").append(tel.getNumber()).append("</div>");
		}
		sb.append("</td></tr>\n");
		sb.append("<tr><td>VCard - Email").append("<td>").append(vcard.getEmail()).append("</td></tr>\n");
		sb.append("<tr><td>Skills").append("<td>");
		Iterator<SkillBean> skills = profile.getSkills().iterator();
		while (skills.hasNext()) {
			SkillBean skill = skills.next();
			sb.append("<div>").append(skill.getSkillName()).append(" (").append(skill.getRating()).append(")</div>");
		}
		sb.append("</td></tr>\n");
		sb.append("<tr><td>Activity Stream").append("<td>");
		Iterator<ActivityBean> activities = profile.getActivityStream().iterator();
		while (activities.hasNext()) {
			ActivityBean activity = activities.next();
			sb.append("<div>").append(activity.getActivityDescription()).append("</div>");
		}
		sb.append("</td></tr>\n");
		sb.append("<tr><td>Status - Location").append("<td>").append(status.getLocation()).append("</td></tr>\n");
		sb.append("<tr><td>Status - Presence").append("<td>").append(status.getPresence()).append("</td></tr>\n");
		sb.append("<tr><td>Status - Last Seen").append("<td>").append(status.getPresenceLastUpdated()).append("</td></tr>\n");
		sb.append("<tr><td>Biography").append("<td>").append(profile.getBio()).append("</td></tr>\n");
		sb.append("<tr><td>Connections").append("<td>");
		Iterator<ConnectionBean> connections = profile.getConnections().iterator();
		while (connections.hasNext()) {
			ConnectionBean connection = connections.next();
			sb.append("<div>").append(connection.getUserID()).append(" - ").append(connection.getAnnotation()).append("</div>");
		}
		sb.append("</td></tr>\n");
		Iterator<String> additional = profile.getAdditionalProperties().keySet().iterator();
		while (additional.hasNext()) {
			String propertyName=additional.next();
			sb.append("<tr><td>Additional - ").append(propertyName).append("<td>").append(profile.getAdditionalProperties().get(propertyName)).append("</td></tr>\n");
		}
		sb.append("</table></body></html>");

		System.out.println(sb.toString());
	}

}
