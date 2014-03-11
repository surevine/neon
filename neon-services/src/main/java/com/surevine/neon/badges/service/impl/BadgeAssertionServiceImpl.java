package com.surevine.neon.badges.service.impl;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.service.BadgeAssertionService;
import com.surevine.neon.badges.service.BadgeValidationException;
import com.surevine.neon.badges.service.BadgeValidationService;
import com.surevine.neon.util.Properties;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class BadgeAssertionServiceImpl implements BadgeAssertionService {

	private Logger logger = Logger.getLogger(BadgeAssertionServiceImpl.class);
	
	private BadgeAssertionDAO dao;
	
	private BadgeValidationService validator = new BadgeValidationServiceImpl();
	
	public void setValidtor(BadgeValidationService validtor) {
		this.validator = validtor;
	}
    
	public void setDao(BadgeAssertionDAO dao) {
		this.dao = dao;
	}

	@Override
	public String getJSONString(String namespace) {
		return dao.retrieve(namespace).toString();
	}

	@Override
	public void createBadgeAssertionFromJSON(String jsonString, String namespace) throws MalformedURLException {
		dao.persist(new BadgeAssertion(jsonString, namespace));

	}

	@Override
	public Collection<BadgeAssertion> getBadgeAssertions(String username) {
		if (username==null) {
			username="*";
		}
		return dao.getAllBadgesForUser(username);
	}
	
	@Override
	public String getBadgeMarkup(String username, List<URL> trustedIssuers) {
		StringBuilder sb = new StringBuilder(); 
		sb.append("<div class='openbadges'>");
		Iterator<BadgeAssertion> badges = getBadgeAssertions(username).iterator();
		while (badges.hasNext()) {
			BadgeAssertion badge = badges.next();
			try {
				validator.validate(badge.getVerify().getUrl(), trustedIssuers);
			
				sb.append("<img class='openbadge-badge' alt='");
				sb.append(badge.getNamespace());
				sb.append(" badge' src='").append(Properties.getProperties().getBaseURL()).append("/rest/badges/bake/").append(badge.getNamespace()).append("'/> ");
			}
			catch (BadgeValidationException e) {
				logger.debug("Found an invalid badge", e);
			}
		}
		sb.append("</div>");
		return sb.toString();
	}

}
