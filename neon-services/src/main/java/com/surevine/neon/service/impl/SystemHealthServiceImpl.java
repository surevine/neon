package com.surevine.neon.service.impl;

import com.surevine.neon.service.InloadControlService;
import com.surevine.neon.service.SystemHealthService;
import com.surevine.neon.service.bean.SystemHealthServiceBean;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Implementation of SystemHealthService
 */
public class SystemHealthServiceImpl implements SystemHealthService {
    private InloadControlService inloadControlService;
    
    @Override
    public SystemHealthServiceBean getSystemStatus() {
        SystemHealthServiceBean healthServiceBean = new SystemHealthServiceBean();
        populateImporters(healthServiceBean);
        populateSystemProperties(healthServiceBean);
        return healthServiceBean;
    }
    
    private void populateSystemProperties(SystemHealthServiceBean healthServiceBean) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.surevine.neon");
        if (bundle != null) {
            Enumeration<String> keys = bundle.getKeys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                healthServiceBean.getSystemProperties().put(key,bundle.getString(key));
            }
        }
    }

    private void populateImporters(SystemHealthServiceBean healthServiceBean) {
        healthServiceBean.setImporters(inloadControlService.getAllImporterConfigurations());
    }

    public void setInloadControlService(InloadControlService inloadControlService) {
        this.inloadControlService = inloadControlService;
    }
}
