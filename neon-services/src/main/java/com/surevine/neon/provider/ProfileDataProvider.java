package com.surevine.neon.provider;

import java.util.Map;

/**
 * Data providers provide some profile information as name:value pairs. They 
 * support one or more namespaces.
 */
public interface ProfileDataProvider {
    public boolean providesForNamespace(String namespace);
    public Map<String,String> getDataForNamespace(String namespace);
}
