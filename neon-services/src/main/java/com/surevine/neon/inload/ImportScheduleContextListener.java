package com.surevine.neon.inload;

import com.surevine.neon.util.Properties;
import com.surevine.neon.util.SpringApplicationContext;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Schedules an import job to run every hour.
 */
public class ImportScheduleContextListener implements ServletContextListener {
    private static final String CONTEXT_KEY = "profileImportTimer";
    
    private Logger logger = Logger.getLogger(ImportScheduleContextListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Initialising data import timer");
        Timer timer = new Timer();
        Calendar cal = Calendar.getInstance();
        Date start = cal.getTime();
        TimerTask importTask = new TimerTask() {
            @Override
            public void run() {
                logger.debug("Running scheduled data import");
                ImportRegistry registry = (ImportRegistry) SpringApplicationContext.getBean("importRegistry");
                if (registry != null) {
                    registry.runImport();
                } else {
                    logger.debug("Could not run the data import: the import registry hasn't been initialised correctly");
                }
            }
        };
        
        timer.scheduleAtFixedRate(importTask, start, Properties.getProperties().getImportPollIntervalSeconds());
        servletContextEvent.getServletContext().setAttribute (CONTEXT_KEY, timer);
        logger.info("Data import timer initialised");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("Shutting down data import timer task");
        Timer timer = (Timer) servletContextEvent.getServletContext().getAttribute(CONTEXT_KEY);
        
        if (timer != null) {
            timer.cancel();
        }

        servletContextEvent.getServletContext().removeAttribute(CONTEXT_KEY);
        logger.info("Data import timer task shutdown complete");
    }
}
