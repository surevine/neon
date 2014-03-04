package com.surevine.neon.badges.bakery;

import java.io.InputStream;
import java.io.OutputStream;

import com.surevine.neon.badges.model.BadgeAssertion;

public class PNGBakery implements BadgeBakery {

	private String badgeKey="openbadges";
	
	public String getBadgeKey() {
		return badgeKey;
	}

	public void setBadgeKey(String badgeKey) {
		this.badgeKey = badgeKey;
	}

	@Override
	public void bake(InputStream in, OutputStream out, BadgeAssertion badge) {
		PNGITXTFactory converter = new PNGITXTFactory();
		converter.copyAndAddITXT(in, badgeKey, badge.toJSON().toString(), out);
	}

}
