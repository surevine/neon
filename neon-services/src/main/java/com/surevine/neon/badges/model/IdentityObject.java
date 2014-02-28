package com.surevine.neon.badges.model;

import org.json.JSONObject;

public class IdentityObject {

	private String identity, type, salt;
	private boolean hashed;
	
	public IdentityObject(JSONObject json) {
		identity = json.getString("identity");
		type = json.getString("type");
		hashed = json.getBoolean("hashed");
		if (json.has("salt")) {
			salt = json.optString("salt");
		}
	}
	
	public JSONObject toJSON() {
		JSONObject rV = new JSONObject();
		rV.accumulate("identity", identity);
		rV.accumulate("type", type);
		rV.accumulate("hashed", hashed);
		if (salt!=null) {
			rV.accumulate("salt", salt);
		}
		return rV;
	}
	
	public String toString() {
		return toJSON().toString();
	}
	
}
