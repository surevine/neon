package com.surevine.neon.inload.importers.mediawiki;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.inload.importers.DataImportException;

public class WikiProfileImporter implements DataImporter {

    private static final String IMPORTER_NAME = "MEDIAWIKI_PROFILE_IMPORTER";
    private ImporterConfigurationDAO configurationDAO;
    private ProfileDAO profileDAO;
    protected String mediaWikiProfilePage="http://wiki.surevine.net/index.php/User:{username}@client?action=raw";
    protected String personTemplatePattern="\\{\\{person\\|.*?\\}\\}";
    protected String myersBriggsPattern   ="\\{\\{Myers-Briggs\\|.*?\\}\\}";
    protected String amaPattern = "\\{\\{ask me about\\|.*?\\}\\}";
    
	@Override
	public String getImporterName() {
		return IMPORTER_NAME;
	}

	@Override
	public boolean providesForNamespace(String namespace) {
        return ProfileDAO.NS_PROFILE_PREFIX.equals(namespace);
	}
	
	public void setAMAPattern(String pattern) {
		amaPattern=pattern;
	}
	
	public void setMediaWikiProfilePage(String pagePattern) {
		mediaWikiProfilePage=pagePattern;
	}
	
	public void setMyersBriggsPattern(String mbPattern) {
		myersBriggsPattern=mbPattern;
	}

    @Override
    public void setConfiguration(Map<String, String> configuration) {
        configurationDAO.configureImporter(IMPORTER_NAME, configuration);
    }

	@Override
	public void inload(String userID) {
		MediaWikiProfile profile = new MediaWikiProfile(userID);
		String rawMediaWikiProfilePage=getRawContent(userID);
		System.out.println(rawMediaWikiProfilePage);
		Pattern matchPersonTemplate=Pattern.compile(personTemplatePattern, Pattern.CASE_INSENSITIVE);
		Matcher personTemplateMatcher = matchPersonTemplate.matcher(rawMediaWikiProfilePage);
		if (personTemplateMatcher.find()) {
			populateFromPersonTemplate(profile, personTemplateMatcher.group(0));
		}
		Pattern mbPattern=Pattern.compile(myersBriggsPattern, Pattern.CASE_INSENSITIVE);
		Matcher mbMatcher = mbPattern.matcher(rawMediaWikiProfilePage);
		if (mbMatcher.find()) {
			populateFromMysersBriggsTemplate(profile, mbMatcher.group(0));
		}
		Pattern askMeAboutPattern=Pattern.compile(amaPattern, Pattern.CASE_INSENSITIVE);
		Matcher amaMatcher=askMeAboutPattern.matcher(rawMediaWikiProfilePage);
		while (amaMatcher.find()) {
			populateFromAskMeAboutTemplate(profile, amaMatcher.group(0));
		}
		System.out.println(profile);
	}
	
	protected void populateFromAskMeAboutTemplate(MediaWikiProfile profile, String rawAMATemplate) {
		Matcher amaContentMatcher = Pattern.compile("\\{\\{ask me about\\|(.*?)\\}", Pattern.CASE_INSENSITIVE).matcher(rawAMATemplate);
		if (amaContentMatcher.find()) {
			profile.addAskMeAbout(amaContentMatcher.group(1));
		}
	}
	
	protected void populateFromMysersBriggsTemplate(MediaWikiProfile profile, String rawMbTemplate) {
		Matcher mbContentMatcher = Pattern.compile("myers\\-briggs\\|(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawMbTemplate);
		if (mbContentMatcher.find()) {
			profile.setPersonalityType(mbContentMatcher.group(1));
		}
	}
	
	protected void populateFromPersonTemplate(MediaWikiProfile profile, String rawPersonTemplate) {
		
		Matcher imgsrcMatcher=Pattern.compile("\\|imgsrc=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (imgsrcMatcher.find()) {
			profile.setProfileImageLocation(imgsrcMatcher.group(1));
		}
		Matcher nameMatcher=Pattern.compile("\\|name=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (nameMatcher.find()) {
			profile.setName(nameMatcher.group(1));
		}
		Matcher jobMatcher=Pattern.compile("\\|job=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (jobMatcher.find()) {
			profile.setJob(jobMatcher.group(1));
		}
		Matcher nsecMatcher=Pattern.compile("\\|nsec=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (nsecMatcher.find()) {
			profile.setNsec(nsecMatcher.group(1));
		}
		Matcher russettMatcher=Pattern.compile("\\|russett=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (russettMatcher.find()) {
			profile.setRussett(russettMatcher.group(1));
		}
		Matcher roomMatcher=Pattern.compile("\\|room=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (roomMatcher.find()) {
			profile.setRoom(roomMatcher.group(1));
		}
		Matcher PFMatcher=Pattern.compile("\\|PF=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (PFMatcher.find()) {
			profile.setPF(PFMatcher.group(1));
		}
		Matcher SectionMatcher=Pattern.compile("\\|section=(.*?)\\}", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (SectionMatcher.find()) {
			profile.setSection(SectionMatcher.group(1));
		}
	}
	
	protected String getRawContent(String userID) {
		String rV=null;
		InputStream webIn=null;
		URL targetURL=null;
		try {
			targetURL=new URL(mediaWikiProfilePage.replaceAll("\\{username\\}", userID));
			webIn = targetURL.openStream();
			rV=IOUtils.toString(webIn);
		}
		catch (MalformedURLException e) {
			throw new DataImportException(userID, this, "Could not generate a mediawiki profile URL from "+mediaWikiProfilePage, e);
		}
		catch (IOException ioe) {
			throw new DataImportException(userID, this, "Could not retrieve profile page "+targetURL, ioe);
		}
		finally {
			IOUtils.closeQuietly(webIn);
		}
		if (rV==null || rV.trim().equals("")) {
			throw new DataImportException(userID, this, "No data could be found for the user profile at "+targetURL);
		}
		return rV;
	}

	@Override
	public void inload(Set<String> userIDs) {
		Iterator<String> userIt = userIDs.iterator();
		while (userIt.hasNext()) {
			inload(userIt.next());
		}
	}

    @Override
    public boolean isEnabled() {
        return configurationDAO.getBooleanConfigurationOption(IMPORTER_NAME, "enabled");
    }

    public void setConfigurationDAO(ImporterConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }

    public void setProfileDAO(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }
    
    public static void main(String arg[]) {
    	new WikiProfileImporter().inload("simonw");
    }

}
