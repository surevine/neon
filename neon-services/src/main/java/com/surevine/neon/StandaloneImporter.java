package com.surevine.neon;

import com.surevine.neon.inload.ImportRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Allows out of container import to be run
 */
public class StandaloneImporter {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
    
    public static void main(String [] args) {
        System.out.println("Running standalone NEON importer.");
        ResourceBundle bundle = ResourceBundle.getBundle("com.surevine.neon");
        boolean runAsDaemon = Boolean.parseBoolean(bundle.getString("neon.standalone.daemon"));
        boolean runningMultiThreaded = Boolean.parseBoolean(bundle.getString("neon.standalone.multithread_import"));
        
        String pathToSpringConfig = bundle.getString("neon.standalone.springconfig");
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext(pathToSpringConfig);
        ImportRegistry importRegistry = (ImportRegistry) applicationContext.getBean("importRegistry");
        
        if (!runAsDaemon) {
            System.out.println("Running once only (set neon.standalone.daemon to true to run as a daemon).");
            doImport(importRegistry, runningMultiThreaded);
        } else {
            String timeoutString = bundle.getString("neon.import.interval");
            Long timeout = Long.parseLong(timeoutString) * 1000l;
            System.out.println("Daemon mode: running NEON import every " + timeoutString + " seconds.");
            while (true) {
                doImport(importRegistry, runningMultiThreaded);
                System.out.println("Next import running in " + timeoutString + " seconds.");
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: stopping.");
                }
            }
        }
    }
    
    private static void doImport(ImportRegistry importRegistry, boolean runningMultiThreaded) {
        Date before = new Date();
        if (runningMultiThreaded) {
            System.out.println(DATE_FORMAT.format(before) + ": NEON import initialised. Running multi-threaded off main thread.");
            importRegistry.runImport(runningMultiThreaded);
        } else {
            System.out.print(DATE_FORMAT.format(before) + ": NEON import initialised. Running in single thread mode on this thread.");
            importRegistry.runImport(runningMultiThreaded);
            Date after = new Date();
            Long millis = after.getTime() - before.getTime();
            String importTime = (millis > 1000) ? (millis/1000) + " seconds" : millis + "ms";
            System.out.println(" complete (" + importTime + ")");
        }
        
        
    }
}
