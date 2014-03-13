package com.surevine.neon.inload.importers;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.surevine.neon.util.DateUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Tests the concrete methods of AbstractDataImporter
 */
public class AbstractDataImporterTest {
    private AbstractDataImporter underTest;
    private ProfileDAO mockProfileDAO;
    private ImporterConfigurationDAO mockImporterConfigurationDAO;
    
    @Test
    public void testSetAdditionalConfiguration() {
        Map<String,String> config = new HashMap<String, String>();
        config.put("KEY1","VALUE1");
        config.put("KEY2","VALUE2");

        mockImporterConfigurationDAO.addImporterConfiguration("MOCK_IMPORTER", config);
        replay(mockImporterConfigurationDAO);
        
        underTest.setAdditionalConfiguration(config);
        verify(mockImporterConfigurationDAO);
    }
    
    @Test
    public void testUsesFullUserSet() {
        Set<String> users = new HashSet<String>();
        users.add("user1");
        users.add("user2");

        expect(mockImporterConfigurationDAO.getConfigurationForImporter("MOCK_IMPORTER")).andReturn(null);
        mockImporterConfigurationDAO.addImporterConfigurationOption(eq("MOCK_IMPORTER"), eq(ImporterConfigurationDAO.NS_LAST_IMPORT), anyString());
        expect(mockImporterConfigurationDAO.getConfigurationForImporter("MOCK_IMPORTER")).andReturn(null);
        mockImporterConfigurationDAO.addImporterConfigurationOption(eq("MOCK_IMPORTER"), eq(ImporterConfigurationDAO.NS_LAST_IMPORT), anyString());
        
        expect(mockProfileDAO.getUserIDList()).andReturn(users);
        replay(mockProfileDAO);
        replay(mockImporterConfigurationDAO);
        
        underTest.runImport();
        verify(mockProfileDAO);
        verify(mockImporterConfigurationDAO);
    }
    
    @Test
    public void testRunImportSetsLastRunDate() {
        String userID = "user1";
        expect(mockImporterConfigurationDAO.getConfigurationForImporter("MOCK_IMPORTER")).andReturn(null);
        mockImporterConfigurationDAO.addImporterConfigurationOption(eq("MOCK_IMPORTER"), eq(ImporterConfigurationDAO.NS_LAST_IMPORT), anyString());
        replay(mockImporterConfigurationDAO);
        
        underTest.runImport(userID);
        verify(mockImporterConfigurationDAO);
    }
    
    @Test
    public void testGetLastRun() {
        String dateString = "2014-01-02 10:11:12";
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("MOCK_IMPORTER", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn(dateString);
        replay(mockImporterConfigurationDAO);
        
        Date resultDate = underTest.getLastRun();
        verify(mockImporterConfigurationDAO);
        assertTrue(dateString.equals(DateUtil.dateToString(resultDate)));   
    }
    
    @Test
    public void testCacheLapsed() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, -61);
        Date lastRun = c.getTime();
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("MOCK_IMPORTER", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn(DateUtil.dateToString(lastRun));
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("MOCK_IMPORTER", ImporterConfigurationDAO.NS_IMPORTER_TIMEOUT)).andReturn("60");
        replay(mockImporterConfigurationDAO);
        
        assertTrue(underTest.cacheLapsed());
        verify(mockImporterConfigurationDAO);
    }

    @Test
    public void testCacheNotLapsed() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, -59);
        Date lastRun = c.getTime();
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("MOCK_IMPORTER", ImporterConfigurationDAO.NS_LAST_IMPORT)).andReturn(DateUtil.dateToString(lastRun));
        expect(mockImporterConfigurationDAO.getStringConfigurationOption("MOCK_IMPORTER", ImporterConfigurationDAO.NS_IMPORTER_TIMEOUT)).andReturn("60");
        replay(mockImporterConfigurationDAO);

        assertFalse(underTest.cacheLapsed());
        verify(mockImporterConfigurationDAO);
    }
    
    @Before
    public void setUp() {
        underTest = new MockedAbstractDataImporter();
        mockProfileDAO = createMock(ProfileDAO.class);
        mockImporterConfigurationDAO = createMock(ImporterConfigurationDAO.class);
        underTest.setProfileDAO(mockProfileDAO);
        underTest.setConfigurationDAO(mockImporterConfigurationDAO);
    }
    
    @After
    public void cleanUp() {
        underTest = null;
        mockImporterConfigurationDAO = null;
        mockProfileDAO = null;
    }
}
