package com.surevine.neon.badges.dao.impl;

import java.net.MalformedURLException;

import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.model.BadgeClass;
import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.redis.PooledJedisProxy;

/**
 * For badges, we are simply taking the shortcut of storing the JSON directly in redis,
 * rather than parsing it in and out of a richer datamodel we will never use
 */
public class RedisJSONBadgeClassDAOImpl extends AbstractRedisJSONDAO implements BadgeClassDAO {

	{ redisNamespace="c:s:b:"; }

	@Override
	public void persist(BadgeClass toPersist) {
		jedis.set(redisNamespace+toPersist.getNamespace(), toPersist.toString());
	}

	@Override
	public BadgeClass retrieve(String namespace) {
		try {
			return new BadgeClass(jedis.get(redisNamespace+namespace), namespace);
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Could not retrieve BadgeClass from redis for "+namespace, e);
		}
	}

    public boolean badgeClassExists(String namespace) {
        String badgeJSON = jedis.get(redisNamespace+namespace);
        return badgeJSON == null || badgeJSON.trim().isEmpty();
    }

}
