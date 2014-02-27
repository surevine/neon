package com.surevine.neon.badges.dao.impl;

import java.net.MalformedURLException;

import com.surevine.neon.badges.dao.IssuerOrganisationDAO;
import com.surevine.neon.badges.model.BadgeClass;
import com.surevine.neon.badges.model.IssuerOrganisation;
import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.redis.PooledJedisProxy;

public class RedisJSONIssuerOrganisationDAOImpl extends AbstractRedisJSONDAO implements IssuerOrganisationDAO {

	{ redisNamespace="c:s:i:"; }
	
	@Override
	public void persist(IssuerOrganisation toPersist) {
		jedis.set(redisNamespace+toPersist.getNamespace(), toPersist.toString());
	}

	@Override
	public IssuerOrganisation retrieve(String namespace) {
		try {
			return new IssuerOrganisation(jedis.get(redisNamespace+namespace), namespace);
		}
		catch (MalformedURLException e) {
			throw new RuntimeException("Could not retrieve BadgeClass from redis for "+namespace, e);
		}
	}

}
