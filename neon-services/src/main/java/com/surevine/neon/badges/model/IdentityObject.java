package com.surevine.neon.badges.model;

import org.json.JSONObject;

public class IdentityObject {

	private String identity, type, salt;
	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public boolean isHashed() {
		return hashed;
	}

	public void setHashed(boolean hashed) {
		this.hashed = hashed;
	}

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
