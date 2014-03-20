package com.surevine.neon.service;

import com.surevine.neon.service.bean.SystemHealthServiceBean;

/**
 * Provides system health information
 */
public interface SystemHealthService {
    /**
     * Gets the system status / health
     * @return the system status / health
     */
    public SystemHealthServiceBean getSystemStatus();
}
