package com.surevine.neon.model;

import java.util.*;

/**
 * Bean holding the details of a profile.
 * 
 * Field namespaces:
 *     vcard:                   ProfileDAO.NS_BASIC_DETAILS
 *     bio:                     ProfileDAO.NS_BASIC_DETAILS
 *     skills:                  ProfileDAO.NS_SKILLS
 *     activityStream:          ProfileDAO.NS_ACTIVITY
 *     status:                  ProfileDAO.NS_STATUS
 *     projectActivity:         ProfileDAO.NS_PROJECT_DETAILS
 *     connections:             ProfileDAO.NS_CONNECTIONS
 *     additionalProperties:    ProfileDAO.NS_ADDITIONAL_PROPERTIES
 */
public class ProfileBean {
    private String userID;
    private VCardBean vcard = new VCardBean();
    private Set<SkillBean> skills = new HashSet<SkillBean>();
    private List<ActivityBean> activityStream = new ArrayList<ActivityBean>();
    private Map<String,String> additionalProperties = new HashMap<String,String>();
    private StatusBean status = new StatusBean();
    private String bio; // needs a priority for multiple importers
    private Set<ProjectActivityBean> projectActivity = new HashSet<ProjectActivityBean>();
    private Set<ConnectionBean> connections = new HashSet<ConnectionBean>();
    private Map<String,Collection<ImporterMetaData>> metaDataMap = new HashMap<String,Collection<ImporterMetaData>>();

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public Set<ConnectionBean> getConnections() {
    	return connections;
    }
    
    public void addConnection(ConnectionBean cb) {
    	connections.add(cb);
    }
    
    public Set<ProjectActivityBean> getProjectActivity() {
    	return projectActivity;
    }
    
    public void addProjectActivity(ProjectActivityBean pab) {
    	projectActivity.add(pab);
    }

    public Set<SkillBean> getSkills() {
        return skills;
    }

    protected void setSkills(Set<SkillBean> skills) {
        this.skills = skills;
    }
    
    public void addOrUpdateSkill(SkillBean skill) {
    	if (hasSkill(skill)) {
    		skills.remove(skill);
    	}
    	skills.add(skill);
    }
    
    public boolean hasSkill(SkillBean skill) {
    	return skills.contains(skill);
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
    
    public void setAdditionalProperty(String propertyName, String propertyValue) {
    	if (propertyValue==null || propertyValue.trim().equals("")) { //If we only have a blank or null value, don't record anything
    		return;
    	}
    	this.additionalProperties.put(propertyName, propertyValue);
    }

    public StatusBean getStatus() {
        return status;
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public VCardBean getVcard() {
        return vcard;
    }

    public void setVcard(VCardBean vcard) {
        this.vcard = vcard;
    }

    public List<ActivityBean> getActivityStream() {
        return activityStream;
    }

    public void setActivityStream(List<ActivityBean> activityStream) {
        this.activityStream = activityStream;
    }
    
    public Iterator<String> getIDsOfActiveProjects() {
    	Set<String> target = new HashSet<String>();
    	Iterator<ProjectActivityBean> pabs = projectActivity.iterator();
    	while (pabs.hasNext()) {
    		target.add(pabs.next().getProjectID());
    	}
    	return target.iterator();
    }
    
    public String getKnownProjectName(String projectID) {
    	Iterator<ProjectActivityBean> pabs = projectActivity.iterator();
    	while (pabs.hasNext()) {
    		ProjectActivityBean pab = pabs.next();
    		if (pab.getProjectID()!=null && pab.getProjectID().equalsIgnoreCase(projectID)) {
    			return pab.getProjectName();
    		}
    	}
    	return null;
    }

    public Iterator<String> getIDsOfOwnedProjects() {
        Set<String> target = new HashSet<String>();
        Iterator<ProjectActivityBean> pabs = projectActivity.iterator();
        while (pabs.hasNext()) {
            ProjectActivityBean pab = pabs.next();
            if (ProjectActivityBean.ProjectActivityType.PROJECT_OWN.equals(pab.getType())) {
                target.add(pabs.next().getProjectID());
            }
        }
        return target.iterator();
    }

    public Iterator<String> getIDsOfJoinedProjects() {
        Set<String> target = new HashSet<String>();
        Iterator<ProjectActivityBean> pabs = projectActivity.iterator();
        while (pabs.hasNext()) {
            ProjectActivityBean pab = pabs.next();
            if (ProjectActivityBean.ProjectActivityType.PROJECT_JOIN.equals(pab.getType())) {
                target.add(pabs.next().getProjectID());
            }
        }
        return target.iterator();
    }

    public Map<String, Collection<ImporterMetaData>> getMetaDataMap() {
        return metaDataMap;
    }

    public void setMetaDataMap(Map<String, Collection<ImporterMetaData>> metaDataMap) {
        this.metaDataMap = metaDataMap;
    }
}
