package com.surevine.neon.badges.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

public class RevocationList {
	private Map<String, String> revoked = new HashMap<String, String>();
	private String namespace;
	
	public RevocationList(String jsonString, String namespace) {
		this.namespace=namespace;
		JSONObject json = new JSONObject(jsonString);
		String[] names = JSONObject.getNames(json);
		for (int i=0; i<names.length; i++) {
			revoked.put(names[i], json.getString(names[i]));
		}
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public Map<String, String> getRevoked() {
		return revoked;
	}

	public void setRevoked(Map<String, String> revoked) {
		this.revoked = revoked;
	}
	
	public void addToCRL(String uid, String reason) {
		revoked.put(uid,  reason);
	}
	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		Iterator<String> uids = revoked.keySet().iterator();
		while (uids.hasNext()) {
			String uid = uids.next();
			json.accumulate(uid, revoked.get(uid));
		}
		return json;
	}
	
	public String toString() {
		return toJSON().toString();
	}
	
}
