package com.surevine.neon.badges.dao.impl;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.model.BadgeClass;
import com.surevine.neon.badges.service.BadgeValidationService;
import com.surevine.neon.badges.service.impl.BadgeValidationServiceImpl;

public class RedisJSONBadgeAssertionDAOImpl extends AbstractRedisJSONDAO implements BadgeAssertionDAO {

	private Logger log = Logger.getLogger(RedisJSONBadgeAssertionDAOImpl.class);

	{ redisNamespace="c:s:a:"; }
	
	@Override
	public void persist(BadgeAssertion toPersist) {
		jedis.set(redisNamespace+toPersist.getNamespace(), toPersist.toString());
	}

	@Override
	public BadgeAssertion retrieve(String namespace) {
		try {
			return new BadgeAssertion(jedis.get(redisNamespace+namespace), namespace);
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Could not retrieve BadgeAssertion from redis for "+namespace, e);
		}
	}

	@Override
	public Collection<BadgeAssertion> getAllBadgesForUser(String username) {
		Collection<BadgeAssertion> rV = new ArrayList<BadgeAssertion>();
		Iterator<String> badgeKeys = jedis.keys(redisNamespace+username+"_*").iterator();
		while (badgeKeys.hasNext()) {
			try {
				String key = badgeKeys.next();
				rV.add(new BadgeAssertion(jedis.get(key), key.substring(redisNamespace.length())));
			}
			catch (MalformedURLException e) {
				log.warn("Could not load a badge assertion from redis", e);
			}
		}
		return rV;
	}

}
