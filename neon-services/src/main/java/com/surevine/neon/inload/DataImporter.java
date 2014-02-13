package com.surevine.neon.inload;

import java.util.Set;

/**
 * Data providers provide some profile information as name:value pairs. They 
 * support one or more namespaces.
 */
public interface DataImporter {
    public boolean providesForNamespace(String namespace);
    public void inload(String userID);
    public void inload(Set<String> userIDs);
}
