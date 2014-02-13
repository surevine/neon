package com.surevine.neon.service.rest;

import com.surevine.neon.redis.PooledJedis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.Map;

@Path("/testredis/")
@Produces("application/json")
public class TestRedisWebService {
    @GET
    public Map<String,String> test() {
        Map<String,String> testMap = new HashMap<>();
        testMap.put("count", String.valueOf(getInvocationCount()));
        return testMap;
    }

	public Long getInvocationCount() {
		return PooledJedis.get().incr("neon:test:testIncr");
	}

}