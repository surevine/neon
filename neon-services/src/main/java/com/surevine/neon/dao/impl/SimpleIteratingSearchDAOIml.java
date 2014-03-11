package com.surevine.neon.dao.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.model.BadgeClass;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.dao.SearchDAO;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.SkillBean;
import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.redis.PooledJedisProxy;
import com.surevine.neon.util.Properties;

public class SimpleIteratingSearchDAOIml implements SearchDAO {

	private IPooledJedis jedis = new PooledJedisProxy();
	private ProfileDAO profileDAO = new ProfileDAOImpl();

	public void setJedis(IPooledJedis jedis) {
		this.jedis = jedis;
	}

	public void setProfileDAO(ProfileDAO profileDAO) {
		this.profileDAO = profileDAO;
	}

	private Logger logger = Logger.getLogger(SimpleIteratingSearchDAOIml.class);

	@Override
	public Collection<ProfileBean> getPeopleBySkill(String skillName, int minLevel) {
		String peopleWithSkillsKeyPattern=Properties.getProperties().getSystemNamespace()+":PROFILE:*:SKILLS";
		Collection<ProfileBean> rV = new ArrayList<ProfileBean>();
		
		Iterator<String> people = jedis.keys(peopleWithSkillsKeyPattern).iterator(); //For each person with at least one skill...
		while (people.hasNext()) { 
			String key = people.next();
			
			Iterator<String> skillJSONStr = jedis.hgetAll(key).values().iterator(); //...iterate through all their skills...
			while (skillJSONStr.hasNext()) {
				SkillBean skill = new SkillBean();
				try {
					skill.populateFromString(skillJSONStr.next());
				}
				catch (Exception e) {
					logger.warn("Could not assemble skill from the database so ignoring", e);
					continue;
				}
				if (skill.getSkillName().equalsIgnoreCase(skillName) && skill.getRating() >= minLevel && !skill.isDisavowed()) { //... If we have the skill and it's at the right level...
					String username = key.replace(Properties.getProperties().getSystemNamespace()+":PROFILE:", "").replace(":SKILLS", "");
					logger.debug(username+" has the skill "+skill.getSkillName());
					ProfileBean profile = profileDAO.getProfileForUser(username); //...get the user and add it to our return value
					rV.add(profile);
				}
			}
			
		}
		return rV;
	}

	@Override
	public Collection<ProfileBean> getPeopleByBadge(String namespace) {
		
		Collection<ProfileBean> rV = new ArrayList<ProfileBean>();
		
		String allBadgesKey="c:s:a:*";
		Iterator<String> assertionKeys = jedis.keys(allBadgesKey).iterator(); //For each badge assertion...
		while (assertionKeys.hasNext()) {
			String assertionData = jedis.get(assertionKeys.next());
			try {
				BadgeAssertion assertion = new BadgeAssertion(assertionData, "temp");
				if (assertion.getBadge().toString().endsWith("/"+namespace)) { //... if we have the required assertion...
					String identity = assertion.getRecipient().getIdentity(); //get the username, which, if it has an @ in, is everything before the @
					if (identity.contains("@")) {
						identity = identity.substring(0, identity.indexOf('@'));
					}
					try {
						ProfileBean pb = profileDAO.getProfileForUser(identity);
						rV.add(pb);
					}
					catch (Exception e) {
						logger.warn("Could not find a profile for the user "+identity+" Even though we have a badge record for him", e);
					}
				}
			}
			catch (MalformedURLException e) {
				logger.warn("Could not retrieve a badge assertion during a search", e);
			}
		}
		return rV;
	}

}
