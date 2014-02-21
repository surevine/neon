package com.surevine.neon.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

import com.google.common.base.Charsets;
import com.surevine.neon.util.Properties;
import com.surevine.neon.util.Profiler;

public class PooledJedis implements IPooledJedis {
	
	private Jedis _jedis;
	private boolean executed=false;
	private static final boolean DEBUG_MODE = Properties.getProperties().getProfileRedis();
	static { System.out.println("Using debug mode?  "+DEBUG_MODE); }
	
	private PooledJedis() {
		_jedis = JedisConnectionFactory.getInstance().getConnection();
	}
	
	@Override
	public Set<String> smembers(String key) {
		try {
			check();
			return _jedis.smembers(key);
		}
		finally {
			close();
		}
	}
	
	@Override
	public boolean exists(String key) {
		check();
		try {
			return _jedis.exists(key);
		}
		finally {
			close();
		}
	}
	
	/**
	 * Deprecated; performance of @{code keys()} is poor on site.
	 * 
	 * Retrieval of a data structure is preferred.
	 */
	@Deprecated
	@Override
	public Set<String> keys(String keyPattern) {
		check();
		try {
			return _jedis.keys(keyPattern);
		}
		finally {
			close();
		}
	}
	
	@Override
	public String get(String key) {
		check();
		try {
			return _jedis.get(key);
		}
		finally {
			close();
		}
	}
	
	@Override
	public byte[] get(byte[] key) {
		check();
		try {
			return _jedis.get(key);
		}
		finally {
			close();
		}
	}
	
	@Override
	public boolean sismember(String key, String member) {
		check();
		try {
			return _jedis.sismember(key, member);
		}
		finally {
			close();
		}
	}
	
	@Override
	public List<String> lrange (String key, long start, long end) {
		check();
		try {
			return _jedis.lrange(key, start, end);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Long sadd(String key, String member) {
		check();
		try {
			return _jedis.sadd(key, member);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Long srem(String key, String member) {
		check();
		try {
			return _jedis.srem(key, member);
		}
		finally {
			close();
		}
	}
	
	@Override
	public String type(String key) {
		check();
		try {
			return _jedis.type(key);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Long incr(String key) {
		check();
		try {
			return _jedis.incr(key);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Long incrBy(String key, int amount) {
		check();
		try { 
			return _jedis.incrBy(key, amount);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Long decr(String key) {
		check();
		try {
			return _jedis.decr(key);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Long decrBy(String key, int amount) {
		check();
		try {
			return _jedis.decrBy(key, amount);
		}
		finally {
			close();
		}
	}
	
	@Override
	public String set (String key, String value) {
		check();
		try {
			return _jedis.set(key, value);
		}
		finally {
			close();
		}
	}
	
	@Override
	public String set (String key, byte[] value) {
		check();
		try {
			return _jedis.set(key.getBytes(Charsets.UTF_8), value);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Long del(String... keys) {
		check();
		try {
			return _jedis.del(keys);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Long expire(String key, int seconds) {
		check();
		try {
			return _jedis.expire(key, seconds);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Long sunionstore(String dstkey, String... keys) {
		check();
		try {
			return _jedis.sunionstore(dstkey, keys);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Long hset(String key, String field, String value) {
		check();
		try {
			return _jedis.hset(key, field, value);
		}
		finally {
			close();
		}
	}
	
	@Override
	public String hget(String key, String field) {
		check();
		try {
			return _jedis.hget(key, field);
		}
		finally {
			close();
		}
	}
	
	@Override
	public Map<String,String> hgetAll(String key) {
		check();
		try {
			return _jedis.hgetAll(key);
		}
		finally {
			close();
		}
	}
	
	private void check() {
		if (executed==true) {
			throw new IllegalStateException("A single PooledJedis can only be used once");
		}
		executed=true;
	}
	
	private void close() {
			JedisConnectionFactory.getInstance().returnConnection(_jedis);
			_jedis=null;
	}

	static IPooledJedis get() {
		if (!DEBUG_MODE) {
			return new PooledJedis();
		} else {
			return new Profiler<IPooledJedis>().wrap(IPooledJedis.class, new PooledJedis());
		}
	}
}