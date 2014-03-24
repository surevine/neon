package com.surevine.neon.service.bean;

import com.surevine.neon.model.VCardBean;

/**
 * User summary information
 */
public class UserSummaryServiceBean {
    private String userID;
    private VCardBean vcard;

    public UserSummaryServiceBean(String userID, VCardBean vcard) {
        this.userID = userID;
        this.vcard = vcard;
    }
    
    public UserSummaryServiceBean() {
        
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public VCardBean getVcard() {
        return vcard;
    }

    public void setVcard(VCardBean vcard) {
        this.vcard = vcard;
    }
}
