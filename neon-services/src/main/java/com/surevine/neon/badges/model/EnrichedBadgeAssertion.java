package com.surevine.neon.badges.model;

import java.net.MalformedURLException;

public class EnrichedBadgeAssertion extends BadgeAssertion {

	public EnrichedBadgeAssertion(BadgeAssertion base) throws MalformedURLException {
		super(base.toJSON(), base.getNamespace());
	}
}
