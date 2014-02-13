package com.surevine.neon.redis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import com.surevine.neon.util.Properties;

public class JedisConnectionFactory {

	private JedisPool _connectionPool = null;
	private volatile Set<JedisStack> inUse = new HashSet<JedisStack>();
	private static JedisConnectionFactory _instance;

	private class JedisStack {
		private Jedis _jedis;
		private StackTraceElement[] _els;
		private String _threadName;
		public JedisStack(StackTraceElement[] stack, Jedis jedis) { _els=stack; _jedis=jedis; _threadName=Thread.currentThread().getName();}
		public boolean equals(Object o) { 
			if (!(o instanceof JedisStack)) {
				return false;
			}
			JedisStack other = (JedisStack)o;
			return _jedis.equals(other._jedis);
		}
		public int hashCode() { return _jedis.hashCode(); }
	}
	
	private JedisConnectionFactory() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(5);
		_connectionPool = new JedisPool(config, Properties.getProperties().getRedisHostname(),Protocol.DEFAULT_PORT,60000);
		
		System.out.println(String.format("Connecting to %s:%d", Properties.getProperties().getRedisHostname(), Protocol.DEFAULT_PORT));
	}
	
	public static JedisConnectionFactory getInstance() {
		if (_instance == null)
		{
			_instance = new JedisConnectionFactory();
		}
		return _instance;
	}
	
	public Jedis getConnection() {
		Jedis jedis = _connectionPool.getResource();
		inUse.add(new JedisStack(Thread.currentThread().getStackTrace(), jedis));
		return jedis;
	}
	
	public void returnConnection (Jedis connection) {
		if (connection!=null) {
			inUse.remove(new JedisStack(Thread.currentThread().getStackTrace(), connection));
			_connectionPool.returnResource(connection);
		}
	}
	
	public String getPoolInfo() {
		StringBuffer sb= new StringBuffer(100);
		sb.append("<pre>");
		sb.append(inUse.size()).append(" Connections in use:\n\n");
		Iterator<JedisStack> connections = inUse.iterator();
		while (connections.hasNext()) {
			JedisStack connection = connections.next();
			sb.append("Connection (").append(connection._threadName).append(")");
			for (int i = 0; i < connection._els.length; i++) {
				sb.append("    ").append(connection._els[i]).append("\n");
			}
			sb.append("\n\n");
		}
		sb.append("</pre>");
		return sb.toString();
	}	
}