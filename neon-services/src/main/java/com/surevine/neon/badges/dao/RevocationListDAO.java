package com.surevine.neon.badges.dao;

import com.surevine.neon.badges.model.RevocationList;

public interface RevocationListDAO {

	public RevocationList retrieve(String namespace);
	public void persist(RevocationList rl, String namespace);
	
}
