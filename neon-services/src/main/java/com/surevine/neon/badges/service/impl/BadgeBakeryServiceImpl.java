package com.surevine.neon.badges.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.spi.UnsupportedMediaTypeException;

import com.surevine.neon.badges.bakery.BadgeBakery;
import com.surevine.neon.badges.bakery.PNGBakery;
import com.surevine.neon.badges.dao.BadgeAssertionDAO;
import com.surevine.neon.badges.dao.impl.RedisJSONBadgeAssertionDAOImpl;
import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.model.BadgeClass;
import com.surevine.neon.badges.service.BadgeBakeryService;

public class BadgeBakeryServiceImpl implements BadgeBakeryService {

	private int initialChunkSize=1024*1024; // Allocate 1 meg chunk by default
	private BadgeAssertionDAO dao = new RedisJSONBadgeAssertionDAOImpl();
	
	public void setDao(BadgeAssertionDAO dao) {
		this.dao = dao;
	}

	public int getInitialChunkSize() {
		return initialChunkSize;
	}

	public void setInitialChunkSize(int initialChunkSize) {
		this.initialChunkSize = initialChunkSize;
	}

	@Override
	public byte[] bake(URL source, String namespace) throws IOException {
		
		BadgeAssertion badge = dao.retrieve(namespace);
		if (source==null) {
			source=badge.getImage();
			if (source==null) {
				source=getImageFromBadgeClass(badge.getBadge());
			}
		}
		
		URLConnection connection = source.openConnection();
		String encoding = connection.getContentEncoding();
		
		if (encoding==null || encoding.indexOf("png")!=-1) { //if no encoding supplied, assume PNG
			return processPNGBakery(connection, badge);
		}
		throw new UnsupportedOperationException("Only PNG bakery is currently supported.  A "+encoding+" was supplied");
	}
	
	protected URL getImageFromBadgeClass(URL badgeClass) throws IOException {
		URLConnection connection = badgeClass.openConnection();
		InputStream is=connection.getInputStream();
		try {
			String badgeString = IOUtils.toString(is);
			BadgeClass bc = new BadgeClass(badgeString, "temp");
			return bc.getImage();
		}
		finally {
			is.close();
		}
	}
	
	protected byte[] processPNGBakery(URLConnection connection, BadgeAssertion badge) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(initialChunkSize);
		InputStream is = connection.getInputStream();
		BadgeBakery bakery = new PNGBakery();
		bakery.bake(is, baos, badge);
		return baos.toByteArray();
	}

}
