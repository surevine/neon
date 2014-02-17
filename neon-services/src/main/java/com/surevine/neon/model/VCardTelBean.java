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
    
    @Override
    public int hashCode() {
    	return (type+number).hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
    	if (o instanceof VCardTelBean) {
    		return ((VCardTelBean)o).number.equals(number) && ((VCardTelBean)o).type.equals("type");
    	}
    	else {
    		return false;
    	}
    }
    
    @Override
    public String toString() {
    	return new StringBuilder().append("TelephoneNumber[type=").append(type).append("&number=").append(number).append("]").toString();
    }
}
