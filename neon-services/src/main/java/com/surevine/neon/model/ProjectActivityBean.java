package com.surevine.neon.model;

import com.surevine.neon.util.DateUtil;
import org.json.JSONObject;

import java.util.Date;

public class ProjectActivityBean implements NeonSerializableObject {
	private static final String JSONKEY_URL = "url";
    private static final String JSONKEY_DESC = "activityDescription";
    private static final String JSONKEY_WHEN = "when";
    private static final String JSONKEY_PROJECT_ID = "projectID";
    private static final String JSONKEY_PROJECT_NAME = "projectName";
    
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
    
    public ProjectActivityBean() {
        // no-op
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
    
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        if (getActivityDescription() != null && !getActivityDescription().isEmpty()) {
            jsonObject.put(JSONKEY_DESC, getActivityDescription());
        }
        if (getUrl() != null && !getUrl().isEmpty()) {
            jsonObject.put(JSONKEY_URL, getUrl());
        }
        if (getProjectID() != null && !getProjectID().isEmpty()) {
            jsonObject.put(JSONKEY_PROJECT_ID, getProjectID());
        }
        if (getProjectName() != null && !getProjectName().isEmpty()) {
            jsonObject.put(JSONKEY_PROJECT_NAME, getProjectName());
        }
        if (getWhen() != null) {
            jsonObject.put(JSONKEY_WHEN, DateUtil.dateToString(getWhen()));
        }
        return jsonObject.toString();
    }

    @Override
    public void populateFromString(String serialisedData) {
        JSONObject jsonObject = new JSONObject(serialisedData);
        if (!jsonObject.isNull(JSONKEY_DESC)) {
            setActivityDescription(jsonObject.getString(JSONKEY_DESC));
        }
        if (!jsonObject.isNull(JSONKEY_URL)) {
            setUrl(jsonObject.getString(JSONKEY_URL));
        }
        if (!jsonObject.isNull(JSONKEY_PROJECT_ID)) {
            setProjectID(jsonObject.getString(JSONKEY_PROJECT_ID));
        }
        if (!jsonObject.isNull(JSONKEY_PROJECT_NAME)) {
            setProjectName(jsonObject.getString(JSONKEY_PROJECT_NAME));
        }
        if (!jsonObject.isNull(JSONKEY_WHEN)) {
            setWhen(DateUtil.stringToDate(jsonObject.getString(JSONKEY_WHEN)));
        }
    }
}
