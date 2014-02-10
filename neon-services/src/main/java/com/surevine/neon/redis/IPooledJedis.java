package com.surevine.neon.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IPooledJedis {

	Set<String> smembers(String key);

	boolean exists(String key);

	Set<String> keys(String keyPattern);

	String get(String key);

	boolean sismember(String key, String member);

	List<String> lrange(String key, long start, long end);

	Long sadd(String key, String member);

	Long srem(String key, String member);

	String type(String key);

	Long incr(String key);

	Long incrBy(String key, int amount);

	Long decr(String key);

	Long decrBy(String key, int amount);

	String set(String key, String value);

	Long del(String... keys);

	Long expire(String key, int seconds);

	Long sunionstore(String dstkey, String... keys);

	Long hset(String key, String field, String value);

	String hget(String key, String field);

	Map<String, String> hgetAll(String key);

	String set(String key, byte[] value);

	byte[] get(byte[] key);

}