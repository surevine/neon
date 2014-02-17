package com.surevine.neon.model;

import java.util.Date;

public class ProjectActivityBean {
	
	private String url;
	private String activityDescription;
	private Date when;
	private String projectID;
	private String projectName;
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

	public ProjectActivityBean(String activityDescription, String url, Date when, String projectID, String projectName) {
		this.activityDescription=activityDescription;
		this.url=url;
		this.when=when;
		this.projectID=projectID;
		this.projectName=projectName;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getActivityDescription() {
		return activityDescription;
	}
	public void setActivityDescription(String activityDescription) {
		this.activityDescription = activityDescription;
	}
	public Date getWhen() {
		return when;
	}
	public void setWhen(Date when) {
		this.when = when;
	}

}
