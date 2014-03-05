package com.surevine.neon.badges.dao;

import java.util.Collection;

import com.surevine.neon.badges.model.BadgeAssertion;

public interface BadgeAssertionDAO {

	public void persist(BadgeAssertion toPersist);
	
	public BadgeAssertion retrieve(String namespace);
	
	public Collection<BadgeAssertion> getAllBadgesForUser(String username);
	
}
