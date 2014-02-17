package com.surevine.neon.inload.importers.mediawiki;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.dao.impl.ProfileDAOImpl;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.inload.importers.AbstractDataImporter;
import com.surevine.neon.inload.importers.DataImportException;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.SkillBean;
import com.surevine.neon.model.VCardTelBean;

public class WikiProfileImporter extends AbstractDataImporter implements DataImporter {
	
	{
		setName("MEDIAWIKI_PROFILE_IMPORTER");
	}

	
    private Logger log = Logger.getLogger(WikiProfileImporter.class);

    protected String mediaWikiProfilePage="http://wiki.surevine.net/index.php/User:{username}@client?action=raw";
    protected String personTemplatePattern="\\{\\{person\\|.*?\\}\\}";
    protected String myersBriggsPattern   ="\\{\\{Myers-Briggs\\|.*?\\}\\}";
    protected String amaPattern = "\\{\\{ask me about\\|.*?\\}\\}";
    protected String wikiImageURLBase="http://wiki.surevine.net/index.php/File:{fileName}";

	@Override
	public boolean providesForNamespace(String namespace) {
        return ProfileDAO.NS_PROFILE_PREFIX.equals(namespace);
	}
	
	public void setAMAPattern(String pattern) {
		amaPattern=pattern;
	}
	
	public void setWikiImageURLBase(String base) {
		wikiImageURLBase=base;
	}
	
	public void setMediaWikiProfilePage(String pagePattern) {
		mediaWikiProfilePage=pagePattern;
	}
	
	public void setMyersBriggsPattern(String mbPattern) {
		myersBriggsPattern=mbPattern;
	}

	@Override
	public void inload(String userID) {
		
		log.info("Creating or updating the user profile of "+userID);
		
		MediaWikiProfile mediaWikiProfile=getMediaWikiProfile(userID);
		log.debug("MediaWiki Profile retrieved, converting to generic profile");
		ProfileBean genericProfile=profileDAO.getProfileForUser(userID);
		try {
			genericProfile.getVcard().getPhoto().setPhotoURL(new URL(wikiImageURLBase.replaceAll("\\{fileName\\}", mediaWikiProfile.getProfileImageLocation())));
		}
		catch (MalformedURLException e) {
			throw new DataImportException(userID,  this, "Could not create a valid profile image URL from "+wikiImageURLBase, e);
		}
		genericProfile.getVcard().setFn(mediaWikiProfile.getName());
		genericProfile.setAdditionalProperty("Job Title", mediaWikiProfile.getJob());
		if (mediaWikiProfile.getNsec()!=null && !mediaWikiProfile.getNsec().trim().equals("")) {
			genericProfile.getVcard().getTelephoneNumbers().add(new VCardTelBean("PSTN", mediaWikiProfile.getNsec()));
		}
		if (mediaWikiProfile.getRussett()!=null && !mediaWikiProfile.getRussett().trim().equals("")) {
			genericProfile.getVcard().getTelephoneNumbers().add(new VCardTelBean("R", mediaWikiProfile.getRussett()));
		}
		genericProfile.setAdditionalProperty("Typical Location", mediaWikiProfile.getRoom());
		genericProfile.setAdditionalProperty("PF Number", mediaWikiProfile.getPF());
		genericProfile.getVcard().setOrg(mediaWikiProfile.getSection());
		Iterator<String> amas = mediaWikiProfile.getAskMeAbouts();
		while (amas.hasNext()) {
			SkillBean skill = new SkillBean();
			skill.setSkillName(amas.next());
			skill.setInferred(true);
			skill.setRating(SkillBean.SKILL_MENTOR);
			genericProfile.addOrUpdateSkill(skill);
		}
		log.debug("Generic Profile generated: "+genericProfile);
		profileDAO.persistProfile(genericProfile, this);
	}
	protected MediaWikiProfile getMediaWikiProfile(String userID) {
		log.info("Creating A MediaWiki user profile object for "+userID);
		MediaWikiProfile profile = new MediaWikiProfile(userID);
		String rawMediaWikiProfilePage=getRawWebData(userID, mediaWikiProfilePage);
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
		log.debug("Profile generated: "+profile);
		return profile;
	}
	
	protected void populateFromAskMeAboutTemplate(MediaWikiProfile profile, String rawAMATemplate) {
		log.info("Populating the user profile of "+profile.getSid()+" from the AMA template");
		Matcher amaContentMatcher = Pattern.compile("\\{\\{ask me about\\|(.*?)\\}", Pattern.CASE_INSENSITIVE).matcher(rawAMATemplate);
		if (amaContentMatcher.find()) {
			log.debug("Setting AMA to: "+amaContentMatcher.group(1));
			profile.addAskMeAbout(amaContentMatcher.group(1));
		}
	}
	
	protected void populateFromMysersBriggsTemplate(MediaWikiProfile profile, String rawMbTemplate) {
		log.info("Populsting the user profile of "+profile.getSid()+" from the myers briggs template");
		Matcher mbContentMatcher = Pattern.compile("myers\\-briggs\\|(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawMbTemplate);
		if (mbContentMatcher.find()) {
			log.debug("Setting personality type to: "+mbContentMatcher.group(1));
			profile.setPersonalityType(mbContentMatcher.group(1));
		}
	}
	
	protected void populateFromPersonTemplate(MediaWikiProfile profile, String rawPersonTemplate) {
		log.info("Populating the user profile of "+profile.getSid()+" from the wiki person template");
		
		Matcher imgsrcMatcher=Pattern.compile("\\|imgsrc=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (imgsrcMatcher.find()) {
			log.debug("Setting the image URL to: "+imgsrcMatcher.group(1));
			profile.setProfileImageLocation(imgsrcMatcher.group(1));
		}
		Matcher nameMatcher=Pattern.compile("\\|name=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (nameMatcher.find()) {
			log.debug("Setting the name to: "+nameMatcher.group(1));
			profile.setName(nameMatcher.group(1));
		}
		Matcher jobMatcher=Pattern.compile("\\|job=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (jobMatcher.find()) {
			log.debug("Setting the job description to: "+jobMatcher.group(1));
			profile.setJob(jobMatcher.group(1));
		}
		Matcher nsecMatcher=Pattern.compile("\\|nsec=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (nsecMatcher.find()) {
			log.debug("Setting nsec to: "+nsecMatcher.group(1));
			profile.setNsec(nsecMatcher.group(1));
		}
		Matcher russettMatcher=Pattern.compile("\\|russett=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (russettMatcher.find()) {
			log.debug("Setting russett to: "+russettMatcher.group(1));
			profile.setRussett(russettMatcher.group(1));
		}
		Matcher roomMatcher=Pattern.compile("\\|room=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (roomMatcher.find()) {
			log.debug("Setting room to: "+roomMatcher.group(1));
			profile.setRoom(roomMatcher.group(1));
		}
		Matcher PFMatcher=Pattern.compile("\\|PF=(.*?)\\|", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (PFMatcher.find()) {
			log.debug("Setting PF to: "+PFMatcher.group(1));
			profile.setPF(PFMatcher.group(1));
		}
		Matcher sectionMatcher=Pattern.compile("\\|section=(.*?)\\}", Pattern.CASE_INSENSITIVE).matcher(rawPersonTemplate);
		if (sectionMatcher.find()) {
			log.debug("Setting section to: "+sectionMatcher.group(1));
			profile.setSection(sectionMatcher.group(1));
		}
	}
	
    public static void main(String arg[]) {
    	WikiProfileImporter importer = new WikiProfileImporter();
    	importer.setProfileDAO(new ProfileDAOImpl());
    	importer.inload("simonw");
    }

}
