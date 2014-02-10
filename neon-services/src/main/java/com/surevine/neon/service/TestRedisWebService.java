package com.surevine.neon.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

import com.surevine.neon.ServletBase;
import com.surevine.neon.redis.PooledJedis;

public class TestRedisWebService extends ServletBase {


	private static final long serialVersionUID = -1598226494852457642L;

	@SuppressWarnings("unchecked")
	protected JSONObject get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	JSONObject rV = new JSONObject();
    	
    	rV.put("count", String.valueOf(getInvocationCount()));
    	return rV;
    }
	
	public Long getInvocationCount() {
		return PooledJedis.get().incr("neon:test:testIncr");
	}

}