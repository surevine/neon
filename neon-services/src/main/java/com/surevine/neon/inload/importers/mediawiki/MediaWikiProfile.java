package com.surevine.neon.inload.importers.mediawiki;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

public class MediaWikiProfile {

	private Logger log = Logger.getLogger(MediaWikiProfile.class);


	private String profileImageLocation, name, job, sid, nsec, russett, room, PF, section, personalityType;
	
	private Set<String> askMeAbouts;
	
	public MediaWikiProfile(String sid) {
		this.sid=sid;
		askMeAbouts = new HashSet<String>();
	}
	
	public String getProfileImageLocation() {
		return profileImageLocation;
	}
	public void setProfileImageLocation(String profileImageLocation) {
		this.profileImageLocation = profileImageLocation;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getNsec() {
		return nsec;
	}
	public void setNsec(String nsec) {
		this.nsec = nsec;
	}
	public String getRussett() {
		return russett;
	}
	public void setRussett(String russett) {
		this.russett = russett;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getPF() {
		return PF;
	}
	public void setPF(String pF) {
		PF = pF;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	
	public void addAskMeAbout(String ama) {
		askMeAbouts.add(ama);
	}
	
	public Iterator<String> getAskMeAbouts() {
		return askMeAbouts.iterator();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("MediaWikiProfile["+name+"|"+ job+"|"+ sid+"|"+ nsec+"|"+ russett+"|"+ room+"|"+ PF+"|"+ section+"|"+profileImageLocation+"|"+personalityType);
		sb.append("|amas=");
		Iterator<String> amas = getAskMeAbouts();
		while  (amas.hasNext()) {
			sb.append(amas.next());
			if (amas.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MediaWikiProfile) {
			return o.toString().equals(toString());
		}
		return false;
	}

	public String getPersonalityType() {
		return personalityType;
	}

	public void setPersonalityType(String personalityType) {
		this.personalityType = personalityType;
	}
	
}
