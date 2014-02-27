package com.surevine.neon.badges.dao.impl;

import java.net.MalformedURLException;

import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.model.BadgeClass;

public class RedisJSONBadgeAssertionDAOImpl extends AbstractRedisJSONDAO implements BadgeAssertionDAO {

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

}
