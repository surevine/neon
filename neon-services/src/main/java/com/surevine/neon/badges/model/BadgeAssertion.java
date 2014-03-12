package com.surevine.neon.badges.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import org.json.JSONObject;

public class BadgeAssertion {

	private String uid, namespace;
	private IdentityObject recipient;
	private URL badge, image, evidence;
	private VerificationObject verify;
	private Date issuedOn, expires;
	
	public BadgeAssertion() {
		
	}
	
	public BadgeAssertion(JSONObject json, String namespace) throws MalformedURLException {
		buildFromJSON(json, namespace);
	}
	
	public BadgeAssertion(String json, String namespace) throws MalformedURLException {
		buildFromJSON(new JSONObject(json), namespace);
	}
	
	private void buildFromJSON(JSONObject json, String namespace) throws MalformedURLException {
		this.namespace=namespace;
		uid=json.getString("uid");
		recipient=new IdentityObject(json.getJSONObject("recipient"));
		badge = new URL(json.getString("badge"));
		verify = new VerificationObject(json.getJSONObject("verify"));
        if (json.has("issuedOn")) {
		    issuedOn = new Date(json.getLong("issuedOn")*1000l);
        }
		if (json.has("image")) {
			image = new URL(json.getString("image"));
		}
		if (json.has("evidence")) {
			evidence=new URL(json.getString("evidence"));
		}
		if (json.has("expires") && !json.isNull("expires")) {
			expires = new Date(json.getLong("expires")*1000l);
		}
	}
	
	public JSONObject toJSON() {
		JSONObject rV = new JSONObject();
		rV.accumulate("uid", uid);
		rV.accumulate("recipient", recipient.toJSON());
		rV.accumulate("badge", badge);
		rV.accumulate("verify", verify.toJSON());
        if (issuedOn != null) {
		    rV.accumulate("issuedOn", issuedOn.getTime()/1000l);
        }
		if (image!=null) {
			rV.accumulate("image", image);
		}
		if (evidence!=null) {
			rV.accumulate("evidence", evidence);
		}
		if (expires!=null) {
			rV.accumulate("expires", expires.getTime() / 1000l);
		}
		return rV;
	}
	
	public String toString() {
		return toJSON().toString();
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public IdentityObject getRecipient() {
		return recipient;
	}
	public void setRecipient(IdentityObject recipient) {
		this.recipient = recipient;
	}
	public URL getBadge() {
		return badge;
	}
	public void setBadge(URL badge) {
		this.badge = badge;
	}
	public URL getImage() {
		return image;
	}
	public void setImage(URL image) {
		this.image = image;
	}
	public URL getEvidence() {
		return evidence;
	}
	public void setEvidence(URL evidence) {
		this.evidence = evidence;
	}
	public VerificationObject getVerify() {
		return verify;
	}
	public void setVerify(VerificationObject verify) {
		this.verify = verify;
	}
	public Date getIssuedOn() {
		return issuedOn;
	}
	public void setIssuedOn(Date issuedOn) {
		this.issuedOn = issuedOn;
	}
	public Date getExpires() {
		return expires;
	}
	public void setExpires(Date expires) {
		this.expires = expires;
	}
	
	public static void main (String arg[]) throws MalformedURLException {
		String example="{  \"uid\": \"f2c20\",  \"recipient\": {    \"type\": \"email\",    \"hashed\": true,    \"salt\": \"deadsea\",    \"identity\": \"sha256$c7ef86405ba71b85acd8e2e95166c4b111448089f2e1599f42fe1bba46e865c5\"  },  \"image\": \"https://example.org/beths-robot-badge.png\",  \"evidence\": \"https://example.org/beths-robot-work.html\",  \"issuedOn\": 1359217910,  \"badge\": \"https://example.org/robotics-badge.json\",  \"verify\": {    \"type\": \"hosted\",    \"url\": \"https://example.org/beths-robotics-badge.json\"  }}";
		System.out.println(new BadgeAssertion(example, "example"));
	}
 }
