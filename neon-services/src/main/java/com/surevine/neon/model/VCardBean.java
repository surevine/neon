package com.surevine.neon.model;

import java.util.HashSet;
import java.util.Set;

public class VCardBean {
    private static final String VCARD_VERSION="4.0";

    private String fn;
    private String org;
    private String title;
    private VCardPhotoBean photo = new VCardPhotoBean();
    private Set<VCardTelBean> telephoneNumbers = new HashSet<>();
    private String email;

    public String getVCardVersion() {
        return VCARD_VERSION;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VCardPhotoBean getPhoto() {
        return photo;
    }

    public void setPhoto(VCardPhotoBean photo) {
        this.photo = photo;
    }

    public Set<VCardTelBean> getTelephoneNumbers() {
        return telephoneNumbers;
    }

    public void setTelephoneNumbers(Set<VCardTelBean> telephoneNumbers) {
        this.telephoneNumbers = telephoneNumbers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
