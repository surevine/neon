package com.surevine.neon.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Proxies calls to a new PooledJedis instance for each call.
 * This class exists to enabled PooledJedis to a new instance each call but also to use Spring to manage and repplace
 * our shared instance of IPooledJedis at will.
 */
public class PooledJedisProxy implements IPooledJedis {

    @Override
    public Set<String> smembers(String key) {
        return PooledJedis.get().smembers(key);
    }

    @Override
    public boolean exists(String key) {
        return PooledJedis.get().exists(key);
    }

    @Override
    public Set<String> keys(String keyPattern) {
        return PooledJedis.get().keys(keyPattern);
    }

    @Override
    public String get(String key) {
        return PooledJedis.get().get(key);
    }

    @Override
    public boolean sismember(String key, String member) {
        return PooledJedis.get().sismember(key, member);
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        return PooledJedis.get().lrange(key, start, end);
    }

    @Override
    public Long sadd(String key, String member) {
        return PooledJedis.get().sadd(key, member);
    }

    @Override
    public Long srem(String key, String member) {
        return PooledJedis.get().srem(key, member);
    }

    @Override
    public String type(String key) {
        return PooledJedis.get().type(key);
    }

    @Override
    public Long incr(String key) {
        return PooledJedis.get().incr(key);
    }

    @Override
    public Long incrBy(String key, int amount) {
        return PooledJedis.get().incrBy(key, amount);
    }

    @Override
    public Long decr(String key) {
        return PooledJedis.get().decr(key);
    }

    @Override
    public Long decrBy(String key, int amount) {
        return PooledJedis.get().decrBy(key, amount);
    }

    @Override
    public String set(String key, String value) {
        return PooledJedis.get().set(key, value);
    }

    @Override
    public Long del(String... keys) {
        return PooledJedis.get().del(keys);
    }

    @Override
    public Long expire(String key, int seconds) {
        return PooledJedis.get().expire(key, seconds);
    }

    @Override
    public Long sunionstore(String dstkey, String... keys) {
        return PooledJedis.get().sunionstore(dstkey, keys);
    }

    @Override
    public Long hset(String key, String field, String value) {
        return PooledJedis.get().hset(key, field, value);
    }

    @Override
    public String hget(String key, String field) {
        return PooledJedis.get().hget(key, field);
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        return PooledJedis.get().hgetAll(key);
    }

    @Override
    public String set(String key, byte[] value) {
        return PooledJedis.get().set(key, value);
    }

    @Override
    public byte[] get(byte[] key) {
        return PooledJedis.get().get(key);
    }
}
