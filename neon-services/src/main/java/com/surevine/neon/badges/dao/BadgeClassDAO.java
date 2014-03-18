package com.surevine.neon.badges.dao;

import com.surevine.neon.badges.model.BadgeClass;

public interface BadgeClassDAO {

	public void persist(BadgeClass toPersist);
	
	public BadgeClass retrieve(String namespace);
    
    public boolean badgeClassExists(String namespace);
	
}
