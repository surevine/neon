package com.surevine.neon.model;

import java.util.Date;

public class StatusBean {
    private String location;
    private String presence;
    private Date presenceLastUpdated;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPresence() {
        return presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }

    public Date getPresenceLastUpdated() {
        return presenceLastUpdated;
    }

    public void setPresenceLastUpdated(Date presenceLastUpdated) {
        this.presenceLastUpdated = presenceLastUpdated;
    }
}
