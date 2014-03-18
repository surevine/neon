package com.surevine.neon.inload;

import com.surevine.neon.util.Properties;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Spring configured singleton registry containing a list of data provides that contribute to a profile
 */
public class ImportRegistry {
    /**
     * Singleton instance
     */
    private static ImportRegistry instance;

    /**
     * logger
     */
    private Logger logger = Logger.getLogger(ImportRegistry.class);

    /**
     * Whether or not to multi-thread the import jobs
     */
    boolean runImportMultithreaded;

    /**
     * Registry of importers
     */
    private Set<DataImporter> registry = new HashSet<DataImporter>();

    /**
     * Private constructor to support singleton pattern
     */
    private ImportRegistry() {
        runImportMultithreaded = Properties.getProperties().isMultiThreadImport();
    }

    
    /**
     * Spring injected registry of importers
     * @param registry the registry
     */
    public void setRegistry(Set<DataImporter> registry) {
        this.registry = registry;
    }

    /**
     * Run data importers
     */              
    public void runImport() {
        logger.info("Running scheduled data import for all users using " + registry.size() + " importer(s)");
        if (runImportMultithreaded) {
            logger.debug("Importing using multi-threaded mode.");
            Executor executor = Executors.newFixedThreadPool(Properties.getProperties().getImportExecutors());
            for (DataImporter imp: this.registry) {
                if (imp.cacheLapsed()) {
                    final DataImporter importer = imp;
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            importer.runImport();
                        }
                    });
                }
            }
        } else {
            logger.debug("Importing using single thread mode.");
            for (DataImporter imp: this.registry) {
                if (imp.cacheLapsed()) {
                	try {
                		imp.runImport();
                	}
                	catch (Exception e) {
                		logger.error("Exception occured when running "+imp.getClass(), e);
                	}
                }
            }
        }
    }

    /**
     * Run data importers for a specified user
     * @param userID the userID
     */
    public void runImport(String userID) {
        logger.debug("Running data import for " + userID + " using " + registry.size() + " importer(s)");
        for (DataImporter prov: this.registry) {
            prov.runImport(userID);
        }
    }
    
    /**
     * Gets the singleton instance
     * @return the singleton instance
     */
    public static ImportRegistry getInstance() {
        if (instance == null) {
            instance = new ImportRegistry();
        }
        return instance;
    }
}
