package com.surevine.neon.badges.bakery;

import java.io.InputStream;
import java.io.OutputStream;

import com.surevine.neon.badges.model.BadgeAssertion;

public interface BadgeBakery {

	public void bake(InputStream in, OutputStream out, BadgeAssertion badge);
	
}
