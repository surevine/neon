package com.surevine.neon.badges.model;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class IssuerOrganisation {

	String name, description, email, namespace;
	URL url, revocationList, image;
	
	public IssuerOrganisation() {
	}
	
	public IssuerOrganisation(JSONObject json, String namespace) throws MalformedURLException {
		buildFromJSON(json);
		this.namespace=namespace;
	}
	
	public IssuerOrganisation(String jsonString, String namespace) throws MalformedURLException {
		buildFromJSON(new JSONObject(jsonString));
		this.namespace=namespace;
	}
	
	private void buildFromJSON(JSONObject json) throws MalformedURLException {
		name=json.getString("name");
		if (json.has("description")) {
			description=json.optString("description");
		}
		url = new URL(json.getString("url"));
		if (json.has("image")) {
			image=new URL(json.getString("image"));
		}
		email=json.optString("email");
		if (json.has("revocationList")) {
			revocationList=new URL(json.getString("revocationList"));
		}
	}
	
	public JSONObject toJSON() {
		JSONObject rV = new JSONObject();
		
		rV.accumulate("name", name);
		rV.accumulate("url", url);
		if (description!=null) {
			rV.accumulate("description", description);
		}
		if (image!=null) {
			rV.accumulate("image", image);
		}
		if (email!=null) {
			rV.accumulate("email", email);
		}
		if (revocationList!=null) {
			rV.accumulate("revocationList", revocationList);
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

	public URL getImage() {
		return image;
	}

	public void setImage(URL image) {
		this.image = image;
	}
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public URL getRevocationList() {
		return revocationList;
	}
	public void setRevocationList(URL revocationList) {
		this.revocationList = revocationList;
	}
	
	public static void main(String arg[]) throws MalformedURLException {
		String example="{  \"name\": \"An Example Badge Issuer\",  \"image\": \"https://example.org/logo.png\",  \"url\": \"https://example.org\",  \"email\": \"steved@example.org\",  \"revocationList\": \"https://example.org/revoked.json\"}";
		System.out.println(new IssuerOrganisation(example, "example"));
	}
	
}
