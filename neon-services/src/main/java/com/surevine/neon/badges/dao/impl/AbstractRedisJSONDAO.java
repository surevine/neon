package com.surevine.neon.badges.dao.impl;

import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.redis.PooledJedisProxy;

public class AbstractRedisJSONDAO {

    protected IPooledJedis jedis = new PooledJedisProxy();
	protected String redisNamespace;
    
	public String getRedisNamespace() {
		return redisNamespace;
	}

	public void setRedisNamespace(String redisNamespace) {
		redisNamespace=redisNamespace.trim();
		if (redisNamespace.endsWith(":")) {
			this.redisNamespace = redisNamespace;
		}
		else {
			this.redisNamespace=redisNamespace+":";
		}
	}
	
    public void setJedis(IPooledJedis jedis) {
        this.jedis = jedis;
    }

	
}
