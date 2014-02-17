package com.surevine.neon.model;

public class ConnectionBean {
	
	private String userID;
	private String annotation;
	
	public ConnectionBean(String userID, String annotation) {
		this.userID=userID;
		this.annotation=annotation;
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
	
	public String toString() {
		return "Connection[user="+userID+"&annotation="+annotation+"]";
	}

}
