package com.surevine.neon.inload;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ImportRegistryTest {
    @Test
    public void testSingletonInstantiation() {
        assertNotNull(ImportRegistry.getInstance());
    }
    
    @Test
    public void testCacheExpiredImport() {
        Set<DataImporter> dataImporterSet = new HashSet<DataImporter>();
        MockDataImporter cacheExpiredImporter = new MockDataImporter();
        cacheExpiredImporter.setCacheLapsed(true);
        dataImporterSet.add(cacheExpiredImporter);
        
        ImportRegistry.getInstance().setRegistry(dataImporterSet);
        ImportRegistry.getInstance().runImport();
        
        assertTrue(cacheExpiredImporter.isHasRun());
    }

    @Test
    public void testCacheNotExpiredImport() {
        Set<DataImporter> dataImporterSet = new HashSet<DataImporter>();
        MockDataImporter cacheNotExpiredImporter = new MockDataImporter();
        cacheNotExpiredImporter.setCacheLapsed(false);
        dataImporterSet.add(cacheNotExpiredImporter);

        ImportRegistry.getInstance().setRegistry(dataImporterSet);
        ImportRegistry.getInstance().runImport();

        assertFalse(cacheNotExpiredImporter.isHasRun());
    }

    @Test
    public void testMulti() {
        Set<DataImporter> dataImporterSet = new HashSet<DataImporter>();
        MockDataImporter cacheExpiredImporter = new MockDataImporter();
        cacheExpiredImporter.setCacheLapsed(true);
        dataImporterSet.add(cacheExpiredImporter);
        MockDataImporter cacheNotExpiredImporter = new MockDataImporter();
        cacheNotExpiredImporter.setCacheLapsed(false);
        dataImporterSet.add(cacheNotExpiredImporter);

        ImportRegistry.getInstance().setRegistry(dataImporterSet);
        ImportRegistry.getInstance().runImport();

        assertTrue(cacheExpiredImporter.isHasRun());
        assertFalse(cacheNotExpiredImporter.isHasRun());
    }
    
    @After
    public void clearRegistry() {
        ImportRegistry.getInstance().setRegistry(new HashSet<DataImporter>());
    }
    
    @Before
    public void disableMultithreading() {
        // switch off multi-threaded mode
        ImportRegistry.getInstance().runImportMultithreaded = false;
    }
}


