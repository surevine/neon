package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.NamespaceHandler;
import com.surevine.neon.model.ImporterMetaData;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.redis.PooledJedis;
import com.surevine.neon.util.DateUtil;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Abstract handler that provides common functionality for handlers dealing with Neon profiles from Redis
 */
public abstract class AbstractNamespaceHandler implements NamespaceHandler {
    protected Logger logger;
    protected Map<String, ImporterMetaData> metaDataCache = new HashMap<String, ImporterMetaData>();
    protected IPooledJedis jedis;
    private ImporterConfigurationDAO importerConfigurationDAO;
    
    protected AbstractNamespaceHandler() {
        logger = Logger.getLogger(this.getClass());
    }

    /**
     * Persists a single field with the appropriate namespace against a user's profile hset
     * @param hsetKey the hset key for the user
     * @param field the field we are persisting the data value for
     * @param importer the name of the importer that is providing the data value
     * @param valueObject the value (usually a string but if it's not provide a serialisation via toString())
     */
    protected void setSingleField(final String hsetKey, final String field, final String importer, final Object valueObject) {
        if (valueObject != null) {
            String value = valueObject.toString();
            if (value != null && value.length() > 0) {
                jedis.hset(hsetKey, field + ":" + importer, value);
            }
        }
    }

    /**
     * Persists multiple profile values for a single profile field
     * @param hsetKey the hset key for the user's profile
     * @param field the field we are persisting data for
     * @param importer the name of the importer that is providing the data values
     * @param values the values, usually string, but if not should provide serialisation via toString()
     */
    protected void setMultipleField(final String hsetKey, final String field, final String importer, final Collection<? extends Object> values) {
        int index = 0;
        for (Object valueObject:values) {
            String value = valueObject.toString();
            if (value != null && value.length() > 0) {
                jedis.hset(hsetKey, field + ":" + importer + ":" + index++, valueObject.toString());
            }
        }
    }

    /**
     * Extracts and returns a single field from a user's data based on the highest priority importer that has contributed to that field
     * @param profileData the user's profile data as extraced from redis
     * @param field the name of the field
     * @param profile the user's profile bean (used to hold metadata)
     * @return the value of the String
     */
    protected String getSingleField(final Map<String,String> profileData, String field, ProfileBean profile) {
        String fieldValue = null;
        String fieldImporter = getHighestPriorityImporterNameAndCacheMetadata(profileData, field);

        if (fieldImporter != null) {
            fieldValue = profileData.get(field + ":" + fieldImporter);
        }

        if (fieldValue != null) {
            if (profile.getMetaDataMap().get(field) == null) {
                profile.getMetaDataMap().put(field, new HashSet<ImporterMetaData>());
            }
            profile.getMetaDataMap().get(field).add(metaDataCache.get(fieldImporter));
        }
        
        return fieldValue;
    }

    /**
     * Gets a collection of values from a field from a user's profile data
     * @param profileData the raw profile data
     * @param field the field we want the data for
     * @param profile the profile bean we want to store source metadata in (TODO)
     * @return the collection of values
     */
    protected Collection<String> getMultipleField(final Map<String,String> profileData, String field, ProfileBean profile) {
        List<String> values = new ArrayList<String>();
        
        for (Map.Entry<String,String> entry:profileData.entrySet()) {
            if (entry.getKey().startsWith(field + ":") && entry.getValue() != null) {
                String importerName = entry.getKey().split(":")[1];

                /*
                 * There's one to many, many to many, and many to one importer => value to be dealt with. Up to the subclasses to aggregate as required.
                 * TODO: Metadata structure doesn't provide for linking each individual value back to its importer - only contain a collection of importers that contributed data to the field
                 */
                if (!metaDataCache.containsKey(importerName)) {
                    metaDataCache.put(importerName, createMetaDataForImporter(importerName));
                }

                if (profile.getMetaDataMap().get(field) == null) {
                    profile.getMetaDataMap().put(field, new HashSet<ImporterMetaData>());
                }
                profile.getMetaDataMap().get(field).add(metaDataCache.get(importerName));
                
                values.add(entry.getValue());
            }
        }
        
        return values;
    }

    /**
     * We store metadata about the source of profile information. This is added to a user's profile on the way out to the client.
     * This method creates a meta data item for inclusion with a profile
     * @param importer the name of the importer we want metadata about
     * @return the metadata
     */
    protected ImporterMetaData createMetaDataForImporter(final String importer) {
        ImporterMetaData imd = new ImporterMetaData();
        imd.setSourceName(importer);
        String lastImportString = importerConfigurationDAO.getStringConfigurationOption(importer, ImporterConfigurationDAO.NS_LAST_IMPORT);
        String priorityString = importerConfigurationDAO.getStringConfigurationOption(importer, ImporterConfigurationDAO.NS_PRIORITY);
        if (lastImportString != null && lastImportString.length() > 0) {
            imd.setLastImport(lastImportString);
        }

        if (priorityString != null && priorityString.length() > 0) {
            imd.setSourcePriority(Integer.parseInt(priorityString));
        }
        return imd;
    }

    /**
     * Gets the name of the highest priority importer that has contributed to a specific field on a user's profile 
     * @param dataMap the data map of raw user profile information
     * @param field the field we are interested in
     * @return the name of the highest priority importer for the argument field, or null if no data exists for that field
     */
    protected String getHighestPriorityImporterNameAndCacheMetadata(final Map<String,String> dataMap, final String field) {
        String highestPriorityImporter = null;
        int highestPrioritySoFar = -1;
        
        Set<String> importerNames = getImportersContributingToField(dataMap, field);
        for (String importerName:importerNames) {
            int priority = -1;

            try {
                if (!metaDataCache.containsKey(importerName)) {
                    metaDataCache.put(importerName, createMetaDataForImporter(importerName));
                }
                priority = metaDataCache.get(importerName).getSourcePriority();
            } catch (NumberFormatException nfe) {
                // ignore this - just check the next one for a valid priority
            }
            
            if (priority > -1 && (highestPrioritySoFar == -1 || highestPrioritySoFar > priority)) {
                highestPriorityImporter = importerName;
                highestPrioritySoFar = priority;
            }
        }

        return highestPriorityImporter;
    }

    /**
     * Gets a collection of importer names for all importers that have contributed to a specific field on a user's profile
     * @param dataMap the data map of raw user profile information
     * @param field the field we are interested in
     * @return a collection of importer name (will be empty in no data stored for the argument field)
     */
    protected Set<String> getImportersContributingToField(final Map<String,String> dataMap, final String field) {
        Set<String> importers = new HashSet<String>();
        for (Map.Entry<String,String> entry:dataMap.entrySet()) {
            if (entry.getKey().startsWith(field + ":")) {
                String importerName = entry.getKey().split(":")[1];
                if ((!importers.contains(importerName)) && importerName != null && importerName.length() > 0) {
                    importers.add(importerName);
                }
            }
        }
        return importers;
    }

    public void setImporterConfigurationDAO(ImporterConfigurationDAO importerConfigurationDAO) {
        this.importerConfigurationDAO = importerConfigurationDAO;
    }

    public void setJedis(IPooledJedis jedis) {
        this.jedis = jedis;
    }
}
