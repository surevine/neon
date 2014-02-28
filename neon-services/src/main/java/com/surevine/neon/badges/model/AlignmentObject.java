package com.surevine.neon.badges.model;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class AlignmentObject {

	private String name, description;
	private URL url;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public AlignmentObject() {
		
	}
	
	public AlignmentObject(JSONObject json) throws MalformedURLException {
		buildFromJSON(json);
	}
	
	public AlignmentObject(String jsonString) throws MalformedURLException {
		buildFromJSON(new JSONObject(jsonString));
	}
	
	private void buildFromJSON(JSONObject json) throws MalformedURLException {
		name=json.getString("name");
		description=json.optString("description");
		url = new URL(json.getString("url"));
	}
	
	public JSONObject toJSON() {
		JSONObject rV = new JSONObject();
		rV.accumulate("name", name);
		if (description!=null) {
			rV.accumulate("description", description);
		}
		rV.accumulate("url", url);
		return rV;
	}
	
	public String toString() {
		return toJSON().toString();
	}
}
