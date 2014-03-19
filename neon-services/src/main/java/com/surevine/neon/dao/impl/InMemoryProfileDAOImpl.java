package com.surevine.neon.dao.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.dao.ProfileUpdatedListener;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.VCardBean;

/**
 * For demo / mocking purposes only
 * @author simonw
 *
 */
public class InMemoryProfileDAOImpl implements ProfileDAO {

	private Map<String,ProfileBean> storage = new HashMap<String,ProfileBean>();
	private Set<String> explicitUserIDs = new HashSet<String>();

    @Override
    public void addProfileUpdatedListener(ProfileUpdatedListener listener) {
        
    }

    @Override
    public void removeProfileUpdatedListener(ProfileUpdatedListener listener) {

    }

    @Override
	public ProfileBean getProfileForUser(String userID) {
		ProfileBean rV = storage.get(userID);
		if (rV==null) {
			rV = new ProfileBean();
			rV.setUserID(userID);
		}
		return rV;
	}

	@Override
	public Set<String> getUserIDList() {
		Iterator<ProfileBean> profiles = storage.values().iterator();
		Set<String> rV = new HashSet<String>();
		while (profiles.hasNext()) {
			rV.add(profiles.next().getUserID());
		}
		rV.addAll(explicitUserIDs);
		return rV;
	}

	@Override
	public void persistProfile(ProfileBean profile, DataImporter importer) {
		ProfileBean existing = storage.get(profile.getUserID());
		if (existing==null) {
			storage.put(profile.getUserID(), profile);
		}
		else {
			storage.put(profile.getUserID(), dumbMerge(existing, profile));
		}
	}
	
	/**
	 * This is a dumb merge that ignores priority and assumes a > b
	 * @return
	 */
	protected ProfileBean dumbMerge(ProfileBean a, ProfileBean b) {
		a.getActivityStream().addAll(b.getActivityStream());
		a.getAdditionalProperties().putAll(b.getAdditionalProperties());
		if (a.getBio()==null) {
			a.setBio(b.getBio());
		}
		a.getConnections().addAll(b.getConnections());
		a.getProjectActivity().addAll(b.getProjectActivity());
		a.getSkills().addAll(b.getSkills());
		if (a.getStatus()==null) {
			a.setStatus(b.getStatus());
		}
		VCardBean aVcard = a.getVcard();
		VCardBean bVcard = b.getVcard();
		if (aVcard.getEmail()==null) {
			aVcard.setEmail(bVcard.getEmail());
		}
		if (aVcard.getFn()==null) {
			aVcard.setFn(bVcard.getFn());
		}
		if (aVcard.getOrg()==null) {
			aVcard.setOrg(bVcard.getOrg());
		}
		if (aVcard.getPhoto()==null) {
			aVcard.setPhoto(bVcard.getPhoto());
		}
		aVcard.getTelephoneNumbers().addAll(bVcard.getTelephoneNumbers());
		if (aVcard.getTitle()==null) {
			aVcard.setTitle(bVcard.getTitle());
		}
		
		return a;
	}

	@Override
	public void addUserIDToProfileList(String userID) {
		explicitUserIDs.add(userID);

	}

	@Override
	public void removeUserIDFromProfileList(String userID) {
		explicitUserIDs.remove(userID);
	}

    @Override
    public Map<String, VCardBean> getAllUserVCards() {
        return null;
    }

}
