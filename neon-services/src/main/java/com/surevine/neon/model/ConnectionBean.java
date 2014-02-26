package com.surevine.neon.model;

import org.json.JSONObject;

public class ConnectionBean implements NeonSerializableObject {
    private static final String JSONKEY_USERID = "userID";
    private static final String JSONKEY_ANNOTATION = "annotation";
    
	private String userID;
	private String annotation;
	
	public ConnectionBean(String userID, String annotation) {
		this.userID=userID;
		this.annotation=annotation;
	}
    
    public ConnectionBean() {
        // no-op
    }
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getAnnotation() {
		return annotation;
	}
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	@Override
	public int hashCode() {
		return userID.toLowerCase().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ConnectionBean) {
			return ((ConnectionBean)o).userID.equalsIgnoreCase(userID);
		}
		return false;
	}
	
    @Override
	public String toString() {
        JSONObject jsonObject = new JSONObject();
        if (getUserID() != null && !getUserID().isEmpty()) {
            jsonObject.put(JSONKEY_USERID, getUserID());
        }
        if (getAnnotation() != null && !getAnnotation().isEmpty()) {
            jsonObject.put(JSONKEY_ANNOTATION, getAnnotation());
        }
        return jsonObject.toString();
	}

    @Override
    public void populateFromString(String serialisedData) {
        JSONObject jsonObject = new JSONObject(serialisedData);
        if (!jsonObject.isNull(JSONKEY_USERID)) {
            setUserID(jsonObject.getString(JSONKEY_USERID));
        }
        if (!jsonObject.isNull(JSONKEY_ANNOTATION)) {
            setAnnotation(jsonObject.getString(JSONKEY_ANNOTATION));
        }
    }

}
