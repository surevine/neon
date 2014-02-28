package com.surevine.neon.badges.dao.impl;

import com.surevine.neon.badges.dao.RevocationListDAO;
import com.surevine.neon.badges.model.IssuerOrganisation;
import com.surevine.neon.badges.model.RevocationList;

public class RedisJSONRevocationListDAOImpl extends AbstractRedisJSONDAO implements RevocationListDAO {

	{ redisNamespace="c:s:r:"; }
	
	@Override
	public RevocationList retrieve(String namespace) {
		return new RevocationList(jedis.get(redisNamespace+namespace), namespace);
	}

	@Override
	public void persist(RevocationList toPersist, String namespace) {
		jedis.set(redisNamespace+toPersist.getNamespace(), toPersist.toString());
	}

}
