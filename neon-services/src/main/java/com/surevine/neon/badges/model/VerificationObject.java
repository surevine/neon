package com.surevine.neon.badges.model;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class VerificationObject {

	private String type;
	private URL url;
	
	public VerificationObject(JSONObject json) throws MalformedURLException, JSONException {
		type=json.getString("type");
		url = new URL(json.getString("url"));
	}
	
	public JSONObject toJSON() {
		JSONObject rV = new JSONObject();
		rV.accumulate("type", type);
		rV.accumulate("url", url);
		return rV;
	}
	
	public String toString() {
		return toJSON().toString();
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	
}
