package com.surevine.neon.inload.feeds;

import com.surevine.neon.inload.DataFeed;
import com.surevine.neon.model.ProfileBean;

import java.util.Map;

/**
 * Example data feed
 */
public class DoesNothingExampleFeed implements DataFeed {
    private boolean enabled;
    private int sourcePriority;
    
    @Override
    public String getFeedName() {
        return "EXAMPLE_FEED";
    }

    @Override
    public String getNamespace() {
        return null;
    }

    @Override
    public void setAdditionalConfiguration(Map<String, String> configuration) {
        // no-op
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setSourcePriority(int priority) {
        this.sourcePriority = priority;
    }

    @Override
    public void contributeToProfile(ProfileBean profileBean) {
        // no-op
    }

    @Override
    public int getSourcePriority() {
        return sourcePriority;
    }
}
