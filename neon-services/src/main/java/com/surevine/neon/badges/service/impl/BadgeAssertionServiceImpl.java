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
import java.util.ArrayList;
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
	public Collection<BadgeAssertion> getBadgeAssertions(String username, boolean validate, List<URL> trustedIssuers) {
		if (username==null) {
			username="*";
		}
		if (!validate) {
			return dao.getAllBadgesForUser(username);
		}
		else {
			Collection<BadgeAssertion> rV = new ArrayList<BadgeAssertion>();
			Iterator<BadgeAssertion> badges = dao.getAllBadgesForUser(username).iterator();
			while (badges.hasNext()) {
				try {
					BadgeAssertion badge = badges.next();
					validator.validate(badge.getVerify().getUrl(), trustedIssuers);
					rV.add(badge);
				}
				catch (BadgeValidationException e) {
					logger.debug("Found an invalid badge", e);
				}
			}
			return rV;
		}
	}
	
	@Override
	public String getBadgeMarkup(String username, List<URL> trustedIssuers) {
		StringBuilder sb = new StringBuilder(); 
		sb.append("<div class='openbadges'>");
		Iterator<BadgeAssertion> badges = getBadgeAssertions(username, true, trustedIssuers).iterator();
		while (badges.hasNext()) {
			BadgeAssertion badge = badges.next();
			sb.append("<img class='openbadge-badge' alt='");
			sb.append(badge.getNamespace());
			sb.append(" badge' src='").append(Properties.getProperties().getBaseURL()).append("rest/badges/bake/").append(badge.getNamespace()).append("'/> ");
		}
		sb.append("</div>");
		return sb.toString();
	}

}
