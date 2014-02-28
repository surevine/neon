package com.surevine.neon.model;

import com.surevine.neon.util.DateUtil;
import org.json.JSONObject;

import java.util.Date;

public class ActivityBean implements NeonSerializableObject {
    private static final String JSONKEY_SOURCESYSTEM = "sourceSystem";
    private static final String JSONKEY_TYPE = "activityType";
    private static final String JSONKEY_DESC = "activityDescription";
    private static final String JSONKEY_TIME = "activityTime";
    
    private String sourceSystem;
    private String activityType;
    private String activityDescription;
    private Date activityTime;

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public Date getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(Date activityTime) {
        this.activityTime = activityTime;
    }
    
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        if (getActivityDescription() != null && !getActivityDescription().isEmpty()) {
            jsonObject.put(JSONKEY_DESC, getActivityDescription());
        }
        if (getActivityType() != null && !getActivityType().isEmpty()) {
            jsonObject.put(JSONKEY_TYPE, getActivityType());
        }
        if (getSourceSystem() != null && !getSourceSystem().isEmpty()) {
            jsonObject.put(JSONKEY_SOURCESYSTEM, getSourceSystem());
        }
        if (getActivityTime() != null) {
            jsonObject.put(JSONKEY_TIME, DateUtil.dateToString(getActivityTime()));
        }
        return jsonObject.toString();
    }

    @Override
    public void populateFromString(String serialisedData) {
        JSONObject jsonObject = new JSONObject(serialisedData);
        if (!jsonObject.isNull(JSONKEY_DESC)) {
            setActivityDescription(jsonObject.getString(JSONKEY_DESC));
        }
        if (!jsonObject.isNull(JSONKEY_SOURCESYSTEM)) {
            setSourceSystem(jsonObject.getString(JSONKEY_SOURCESYSTEM));
        }
        if (!jsonObject.isNull(JSONKEY_TYPE)) {
            setActivityType(jsonObject.getString(JSONKEY_TYPE));
        }
        if (!jsonObject.isNull(JSONKEY_TIME)) {
            setActivityTime(DateUtil.stringToDate(jsonObject.getString(JSONKEY_TIME)));
        }
    }
}
