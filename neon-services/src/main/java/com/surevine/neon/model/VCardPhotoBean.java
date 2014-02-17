package com.surevine.neon.model;

import java.net.URL;

public class VCardPhotoBean {
    private String mimeType;
    private URL photoURL;

    public VCardPhotoBean(String mimeType, URL photoURL) {
        this.mimeType = mimeType;
        this.photoURL = photoURL;
    }

    public VCardPhotoBean() {
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public URL getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(URL photoURL) {
        this.photoURL = photoURL;
    }
}
