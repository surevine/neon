package com.surevine.neon.inload.feeds;

import com.surevine.neon.model.ProfileBean;
import org.apache.log4j.Logger;

/**
 * Example data feed
 */
public class DoesNothingExampleFeed extends AbstractDataFeed {
    /**
     * Logger
     */
    Logger logger = Logger.getLogger(DoesNothingExampleFeed.class);
    
    @Override
    public String getFeedName() {
        return "EXAMPLE_FEED";
    }

    @Override
    public void contributeToProfile(final ProfileBean profileBean) {
        // no-op
        logger.debug(getFeedName() + " contributeToProfile() has been executed.");
    }
}
