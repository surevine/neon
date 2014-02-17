package com.surevine.neon.model;

public class VCardTelBean {
    private String type;
    private String number;

    public VCardTelBean(String type, String number) {
        this.number = number;
        this.type = type;
    }

    public VCardTelBean() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
