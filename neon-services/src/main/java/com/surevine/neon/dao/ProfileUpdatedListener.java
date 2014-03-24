package com.surevine.neon.dao;

import com.surevine.neon.model.ProfileBean;

/**
 * Realisations of this interface are interested in being informed when a profile has been updated.
 */
public interface ProfileUpdatedListener {
    public void profileUpdated(ProfileBean profileBean);
}
