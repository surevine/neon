package com.surevine.neon.badges.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JavaBean implementation of:  https://github.com/mozilla/openbadges-specification/blob/master/Assertion/latest.md#badgeclass
 * @author simonw
 *
 */
public class BadgeClass {
	
	private String namespace;

	private String name, description;
	private URL image, criteria, issuer;
	private Collection<AlignmentObject> alignment = new HashSet<AlignmentObject>();
	private Collection<String> tags = new HashSet<String>();
	
	public BadgeClass(String jsonString, String namespace) throws MalformedURLException {
		buildFromJSON(new JSONObject(jsonString));
		this.namespace=namespace;
	}
	
	public BadgeClass(JSONObject json, String namespace) throws MalformedURLException {
		buildFromJSON(json);
		this.namespace=namespace;
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	private void buildFromJSON(JSONObject json) throws MalformedURLException {
		name=json.getString("name");
		description=json.getString("description");
		image=new URL(json.getString("image"));
		criteria = new URL(json.getString("criteria"));
		issuer = new URL(json.getString("issuer"));
		JSONArray alignmentJSON = json.optJSONArray("alignment");
		if (alignmentJSON!=null) {
			for (int i=0; i < alignmentJSON.length(); i++) {
				alignment.add(new AlignmentObject(alignmentJSON.getJSONObject(i)));
			}
		}
		JSONArray tagsJSON = json.optJSONArray("tags");
		if (tagsJSON!=null) {
			for (int i=0; i < tagsJSON.length(); i++) {
				tags.add((tagsJSON.getString(i)));
			}
		}
	}
	
	public BadgeClass() {
		
	}
	
	public JSONObject toJSON() {
		JSONObject rV = new JSONObject();
		rV.accumulate("name", name);
		rV.accumulate("description", description);
		rV.accumulate("image", image);
		rV.accumulate("criteria", criteria);
		rV.accumulate("issuer", issuer);
		if (alignment.size()>0) {
			rV.accumulate("alignment", alignment);
		}
		if (tags.size()>0) {
			rV.accumulate("tags", tags);
		}
		return rV;
	}
	
	public String toString() {
		return toJSON().toString();
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
	public URL getImage() {
		return image;
	}
	public void setImage(URL image) {
		this.image = image;
	}
	public URL getCriteria() {
		return criteria;
	}
	public void setCriteria(URL criteria) {
		this.criteria = criteria;
	}
	public URL getIssuer() {
		return issuer;
	}
	public void setIssuer(URL issuer) {
		this.issuer = issuer;
	}
	public Collection<AlignmentObject> getAlignment() {
		return alignment;
	}
	public void setAlignment(Collection<AlignmentObject> alignment) {
		this.alignment = alignment;
	}
	public Collection<String> getTags() {
		return tags;
	}
	public void setTags(Collection<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * Should just parse in and out a hard-coded example badge class
	 * @throws MalformedURLException
	 */
	public static void main(String arg[]) throws MalformedURLException {
		String exampleBadgeClass = "{  \"name\": \"Awesome Robotics Badge\",  \"description\": \"For doing awesome things with robots that people think is pretty great.\",  \"image\": \"https://example.org/robotics-badge.png\",  \"criteria\": \"https://example.org/robotics-badge.html\",  \"tags\": [\"robots\", \"awesome\"],  \"issuer\": \"https://example.org/organization.json\",  \"alignment\": [    { \"name\": \"CCSS.ELA-Literacy.RST.11-12.3\",      \"url\": \"http://www.corestandards.org/ELA-Literacy/RST/11-12/3\",      \"description\": \"Follow precisely a complex multistep procedure when carrying out experiments, taking measurements, or performing technical tasks; analyze the specific results based on explanations in the text.\"    },    { \"name\": \"CCSS.ELA-Literacy.RST.11-12.9\",      \"url\": \"http://www.corestandards.org/ELA-Literacy/RST/11-12/9\",      \"description\": \" Synthesize information from a range of sources (e.g., texts, experiments, simulations) into a coherent understanding of a process, phenomenon, or concept, resolving conflicting information when possible.\"    }  ]}";
		BadgeClass bc = new BadgeClass(exampleBadgeClass, "example");
		System.out.println(bc.toString());
	}
}
