package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.model.ImporterMetaData;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.redis.IPooledJedis;
import com.surevine.neon.util.DateUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

/**
 * Tests AbstractNamespaceHandler
 */
public class AbstractNamespaceHandlerTest {
    private AbstractNamespaceHandler underTest;
    private IPooledJedis mockJedis;
    private ImporterConfigurationDAO mockImporterConfigurationDAO;
    
    @Test
    public void testSetSingleField() {
        String hsetKey = "HSET_KEY";
        String field = "MOCK_FIELD";
        String importer = "IMPORTER";
        String value = "VALUE";
        
        expect(mockJedis.hset(hsetKey, field + ":" + importer, value)).andReturn(1l);
        replay(mockJedis);
        
        underTest.setSingleField(hsetKey,field,importer,value);
        verify(mockJedis);
    }
    
    @Test
    public void testSetMultipleField() {
        String hsetKey = "HSET_KEY";
        String field = "MOCK_FIELD";
        String importer = "IMPORTER";
        List<String> values = new ArrayList<String>();
        values.add("value0");
        values.add("value1");
        values.add("value2");
        values.add("value3");

        expect(mockJedis.hset(hsetKey, field + ":" + importer + ":0", "value0")).andReturn(1l);
        expect(mockJedis.hset(hsetKey, field + ":" + importer + ":1", "value1")).andReturn(1l);
        expect(mockJedis.hset(hsetKey, field + ":" + importer + ":2", "value2")).andReturn(1l);
        expect(mockJedis.hset(hsetKey, field + ":" + importer + ":3", "value3")).andReturn(1l);
        replay(mockJedis);
        
        underTest.setMultipleField(hsetKey,field,importer,values);
        verify(mockJedis);
    }
    
    @Test
    public void testGetSingleField() {
        Map<String,String> data = new HashMap<String, String>();
        ProfileBean profileBean = new ProfileBean();
        
        data.put("a:importer1", "apple");
        data.put("b:importer1", "pear");
        data.put("b:importer2", "banana");
        data.put("c:importer2", "cabbage");
        data.put("d:importer3", null);
        
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer1", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn("2014-02-01 10:00:00");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer1", ImporterConfigurationDAO.NS_PRIORITY)).andReturn("3");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer2", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn("2014-02-01 10:00:00");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer2", ImporterConfigurationDAO.NS_PRIORITY)).andReturn("2");
        replay(mockImporterConfigurationDAO);
        
        assertTrue("banana".equals(underTest.getSingleField(data, "b", profileBean)));
        assertTrue(profileBean.getMetaDataMap().containsKey("b"));
        verify(mockImporterConfigurationDAO);
    }
    
    @Test
    public void testGetMultipleField() {
        Map<String,String> data = new HashMap<String, String>();
        ProfileBean profileBean = new ProfileBean();

        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer1", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn("2014-02-01 10:00:00");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer1", ImporterConfigurationDAO.NS_PRIORITY)).andReturn("1");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer2", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn("2014-02-01 10:00:00");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer2", ImporterConfigurationDAO.NS_PRIORITY)).andReturn("2");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer3", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn("2014-02-01 10:00:00");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer3", ImporterConfigurationDAO.NS_PRIORITY)).andReturn("3");
        replay(mockImporterConfigurationDAO);

        data.put("a:importer1", "apple");
        data.put("b:importer1", "pear");
        data.put("b:importer2", "banana");
        data.put("c:importer2", "cabbage");
        data.put("d:importer2", "cherry");
        data.put("bb:importer1", "lettuce");
        data.put("b:importer3:0", "orange");
        data.put("b:importer3:1", "grape");
        data.put("b:importer3:2", "garlic");
        data.put("b:importer4", null);
        
        // should be 5 entries - method should support 1..* importers mapped to 1..* values
        Collection results = underTest.getMultipleField(data,"b",profileBean);
        assertEquals(results.size(), 5);
        assertTrue(results.contains("pear"));
        assertTrue(results.contains("banana"));
        assertTrue(results.contains("orange"));
        assertTrue(results.contains("grape"));
        assertTrue(results.contains("garlic"));
        
        verify(mockImporterConfigurationDAO);
    }
    
    @Test
    public void testCreateMetaDataForImporter() {
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer1", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn("2014-02-01 10:00:00");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer1", ImporterConfigurationDAO.NS_PRIORITY)).andReturn("1");
        replay(mockImporterConfigurationDAO);

        ImporterMetaData imd = underTest.createMetaDataForImporter("importer1");
        verify(mockImporterConfigurationDAO);
        assertEquals(imd.getSourceName(), "importer1");
        assertEquals(imd.getLastImport(), "2014-02-01 10:00:00");
        assertEquals(imd.getSourcePriority(), 1);
    }
    
    @Test
    public void testGetHighestPriorityImporterNameAndCacheMetadata() {
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer1", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn("2014-03-10 10:20:30");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer2", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn("2014-03-10 10:20:30");
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("importer3", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn("2014-03-10 10:20:30");
        replay(mockImporterConfigurationDAO);
        
        ImporterMetaData imd1 = new ImporterMetaData();
        imd1.setSourceName("importer1");
        imd1.setSourcePriority(2);
        ImporterMetaData imd2 = new ImporterMetaData();
        imd2.setSourceName("importer2");
        imd2.setSourcePriority(3);
        ImporterMetaData imd3 = new ImporterMetaData();
        imd3.setSourceName("importer3");
        imd3.setSourcePriority(1);
        
        underTest.metaDataCache.put(imd1.getSourceName(), imd1);
        underTest.metaDataCache.put(imd2.getSourceName(), imd2);
        underTest.metaDataCache.put(imd3.getSourceName(), imd3);

        Map<String,String> data = new HashMap<String, String>();
        data.put("a:importer1", "apple");
        data.put("a:importer2", "pear");
        data.put("a:importer3", "orange");
        
        assertEquals("importer3", underTest.getHighestPriorityImporterNameAndCacheMetadata(data, "a"));
    }
    
    @Test
    public void testGetImportersContributingToField() {
        Map<String,String> data = new HashMap<String, String>();

        data.put("a:importer1", "apple");
        data.put("b:importer1", "pear");
        data.put("b:importer2", "banana");
        data.put("c:importer2", "cabbage");
        data.put("b:importer3:0", "orange");
        data.put("b:importer3:1", "grape");
        data.put("d:importer4", "cherry");
        
        Set<String> contributingImporters = underTest.getImportersContributingToField(data, "b");
        assertEquals(3, contributingImporters.size());
        assertTrue(contributingImporters.contains("importer1"));
        assertTrue(contributingImporters.contains("importer2"));
        assertTrue(contributingImporters.contains("importer3"));
    }

    @Before
    public void setup() {
        underTest = new MockAbstractNamespaceHandler();
        mockJedis = createMock(IPooledJedis.class);
        mockImporterConfigurationDAO = createMock(ImporterConfigurationDAO.class);
        underTest.setJedis(mockJedis);
        underTest.setImporterConfigurationDAO(mockImporterConfigurationDAO);
    }
    
    @After
    public void after() {
        underTest = null;
        mockJedis = null;
        mockImporterConfigurationDAO = null;
    }
}
