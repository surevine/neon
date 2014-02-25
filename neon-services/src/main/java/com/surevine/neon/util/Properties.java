package com.surevine.neon.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Properties {
    private String _hostname = "10.66.2.127";
    private boolean _profileRedis = false;
    private String systemNamespace = "NEON";
    private boolean useMockProfile = false;
    private long importPollIntervalSeconds = 1000*60;
    private int importExecutors = 10;
    private boolean multiThreadImport;

    private int feedExecutors = 10;
    private boolean multiThreadFeed;
    
    private static Properties _instance = null;
    
    private long _connectionLifespan=60l*5l*1000l;
    private long _redisCacheLifespan=60l*1000l;

    private Properties() {
        ResourceBundle bundle = ResourceBundle.getBundle("com.surevine.neon");

        String connectionLifeInSeconds = getBundleString(bundle, "redis.connection.refresh.seconds");
        if (connectionLifeInSeconds != null) {
            _connectionLifespan = Long.parseLong(connectionLifeInSeconds) * 1000l;
        }

        String redisCacheLifespan = getBundleString(bundle, "redis.cache.lifespan.seconds");
        if (redisCacheLifespan != null) {
            _redisCacheLifespan = Long.parseLong(redisCacheLifespan) * 1000l;
        }
        
        String redisHostName= getBundleString(bundle, "redis.hostname");
        if (redisHostName!=null) {
        	_hostname=redisHostName;
        }

        String systemNamespace = getBundleString(bundle, "redis.system.namespace");
        if (systemNamespace!=null) {
            this.systemNamespace=systemNamespace;
        }

        String useMockString = getBundleString(bundle, "dev.use_mock_profile");
        if (useMockString!=null) {
            this.useMockProfile= Boolean.parseBoolean(useMockString);
        }

        String importPollTimeoutString = getBundleString(bundle, "neon.import.interval");
        if (importPollTimeoutString != null) {
            importPollIntervalSeconds = Long.parseLong(importPollTimeoutString) * 1000l;
        }

        String importExecutorsString = getBundleString(bundle, "neon.import.executors");
        if (importExecutorsString != null) {
            importExecutors = Integer.parseInt(importExecutorsString);
        }

        String multiThreadImportString = getBundleString(bundle, "neon.import.multithread_import");
        if (multiThreadImportString!=null) {
            this.multiThreadImport= Boolean.parseBoolean(multiThreadImportString);
        }

        String feedExecutorsString = getBundleString(bundle, "neon.feed.executors");
        if (feedExecutorsString != null) {
            feedExecutors = Integer.parseInt(feedExecutorsString);
        }

        String multiThreadFeedString = getBundleString(bundle, "neon.feed.multithread_import");
        if (multiThreadFeedString!=null) {
            this.multiThreadFeed= Boolean.parseBoolean(multiThreadFeedString);
        }
    }
    
    private String getBundleString(final ResourceBundle bundle, final String key) {
        try {
            return bundle.getString(key);
        } catch(MissingResourceException eMR) {
            return null;
        }
    }

    public synchronized static Properties getProperties() {
        if (_instance == null) {
            _instance = new Properties();
        }
        return _instance;
    }

    public String getRedisHostname() {
        return _hostname;
    }

    public long getRedisConnectionLifespan() {
        return _connectionLifespan;
    }

    public long getRedisCacheLifespan() {
        return _redisCacheLifespan;
    }

	public boolean getProfileRedis() {
		return _profileRedis;
	}
    
    public String getSystemNamespace() {
        return systemNamespace;
    }

    public boolean isUseMockProfile() {
        return useMockProfile;
    }

    public long getImportPollIntervalSeconds() {
        return importPollIntervalSeconds;
    }

    public int getImportExecutors() {
        return importExecutors;
    }

    public boolean isMultiThreadImport() {
        return multiThreadImport;
    }

    public int getFeedExecutors() {
        return feedExecutors;
    }

    public boolean isMultiThreadFeed() {
        return multiThreadFeed;
    }
}
