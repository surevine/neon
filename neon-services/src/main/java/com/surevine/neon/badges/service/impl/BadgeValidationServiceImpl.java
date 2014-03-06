package com.surevine.neon.badges.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import ar.com.hjg.pngj.PngReader;
import ar.com.hjg.pngj.chunks.ChunkHelper;
import ar.com.hjg.pngj.chunks.PngChunk;
import ar.com.hjg.pngj.chunks.PngChunkTextVar;

import com.surevine.neon.badges.model.BadgeAssertion;
import com.surevine.neon.badges.model.BadgeClass;
import com.surevine.neon.badges.model.EnrichedBadgeAssertion;
import com.surevine.neon.badges.model.IssuerOrganisation;
import com.surevine.neon.badges.model.RevocationList;
import com.surevine.neon.badges.service.BadgeValidationException;
import com.surevine.neon.badges.service.BadgeValidationService;

public class BadgeValidationServiceImpl implements BadgeValidationService {
	Logger logger = Logger.getLogger(BadgeValidationServiceImpl.class);
	
	private String badgekey="openbadges";
	
	public String getBadgekey() {
		return badgekey;
	}

	public void setBadgekey(String badgekey) {
		this.badgekey = badgekey;
	}

	@Override
	public EnrichedBadgeAssertion validateAndEnrich(URL assertion, List<URL> trustedIssuers) throws MalformedURLException {
		validate(assertion, trustedIssuers);
		return new EnrichedBadgeAssertion(assertionFromURL(assertion));
	}
	
	protected BadgeAssertion assertionFromURL(URL url) {
		try {
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			try {
				logger.debug("Reading the PNG at: "+url);
				PngReader reader = new PngReader(is);
				reader.readSkippingAllRows();
				logger.trace(reader.getChunksList().toStringFull());
				Iterator<PngChunk> chunks = reader.getChunksList().getChunks().iterator();
				while (chunks.hasNext()) {
					PngChunk chunk = chunks.next();
					logger.trace("Found a chunk: "+chunk.toString());
					if (ChunkHelper.isText(chunk)) {
						PngChunkTextVar text = (PngChunkTextVar)chunk;
						logger.debug("Chunk has text for key: "+text.getKey());
						if (text.getKey().equals(badgekey)) {
							return new BadgeAssertion(text.getVal(), "temp");
						}
					}
				}
				throw new BadgeValidationException("Could not find any openbadges metadata within the badge");
			}
			finally {
				IOUtils.closeQuietly(is);
			}
		}
		catch (IOException e) {
			throw new BadgeValidationException("Could not find a badge at "+url, e);
		}
		
	}

	@Override
	public void validate(URL assertionURL, List<URL> trustedIssuers) {

		BadgeAssertion assertion = assertionFromURL(assertionURL);
		
		String verificationType = assertion.getVerify().getType();
		if (!verificationType.equalsIgnoreCase("hosted")) {
			throw new BadgeValidationException("Badge could not be verified.  A verification type of " + verificationType + " was supplied, and this service only supports 'hosted' verification");
		}

		URL validationURL = assertion.getVerify().getUrl();
		
		//Has the badge metadata been tampred with?
		BadgeAssertion fromURL = getAssertionFromURL(validationURL);
		compareAssertions(assertion, fromURL);

		IssuerOrganisation issuer = getIssuer(fromURL);
		
		if (trustedIssuers!=null) {
			//Do we trust the issuer?
			Iterator<URL> trustedIssuersIt=trustedIssuers.iterator();
			boolean trusted=false;
			while (trustedIssuersIt.hasNext()) {
				URL org = trustedIssuersIt.next();
				logger.trace("Comparing provided URL of "+org+" with supplied URL of "+issuer.getUrl());
				if (org.equals(issuer.getUrl())) {
					trusted=true;
					break;
				}
			}
			if (!trusted) {
				throw new BadgeValidationException("This badge was issued by "+issuer.getName()+" / "+issuer.getUrl()+", which is not trusted");
			}
		}
		else {
			logger.info("No trusted issuers were supplied, so skipping the trusted issuer check");
		}
		//Has the badge been revoked?
		if (issuer.getRevocationList()!=null) {
			try {
				URL url = issuer.getRevocationList();
				URLConnection connection = url.openConnection();
				InputStream is = connection.getInputStream();
				try {
					RevocationList rL = new RevocationList(IOUtils.toString(is), "temp");
					String revocationReason = rL.getRevoked().get(fromURL.getUid());
					if (revocationReason!=null) {
						throw new BadgeValidationException("The issuer has revoked this badge.  The reason provided was: "+revocationReason);
					}
				}
				finally {
					IOUtils.closeQuietly(is);
				}
			}
			catch (IOException e) {
				throw new BadgeValidationException("Could not retrieve the badge revocation list", e);
			}
		}
	}
	
	protected IssuerOrganisation getIssuer(BadgeAssertion badge) {
		try {
			URL badgeURL = badge.getBadge();
			URLConnection connection = badgeURL.openConnection();
			InputStream is = connection.getInputStream();
			BadgeClass bc = new BadgeClass(IOUtils.toString(is), "temp");
			URL issuerURL = bc.getIssuer();
			URLConnection issueConnection = issuerURL.openConnection();
			InputStream issueIS =issueConnection.getInputStream();
			IssuerOrganisation rV = new IssuerOrganisation(IOUtils.toString(issueIS), "temp");
			return rV;
		}
		catch (IOException e) {
			throw new BadgeValidationException("Could not connect to the badge class: "+badge.getBadge());
		}
	}
	
	
	/**
	 * Throw a BadgeValidationException if the source badge doesn't seem to match the data in it's validation URL, which implies
	 * a ham-fisted attempt at forgery
	 */
	protected void compareAssertions(BadgeAssertion provided, BadgeAssertion served) {
		if (!provided.getBadge().equals(served.getBadge())) {
			throw new BadgeValidationException("The provided verification URL does not match the served URL");
		}
		
		if (served.getEvidence()!=null && (!provided.getEvidence().equals(served.getEvidence()))) {
			throw new BadgeValidationException("The provided evidence URL does not match the served URL");
		}
		
		if (served.getExpires()!=null && (!provided.getExpires().equals(served.getExpires()))) {
			throw new BadgeValidationException("The provided expiration date does not match the served date");
		}
		
		if (served.getExpires()!=null && new Date().after(served.getExpires())) {
			throw new BadgeValidationException("The badge expired on "+served.getExpires());
		}
		
		if (served.getImage()!=null && (!provided.getImage().equals(served.getImage()))) {
			throw new BadgeValidationException("The provided and supplied image URLs are different");
		}
		
		if (served.getIssuedOn().before(provided.getIssuedOn())) { //Allow the served badge to be re-issued (ie. have a later date than the provided)
			throw new BadgeValidationException("The provided badge was issued after the verification date");
		}
		
		if (!served.getRecipient().toString().equals(provided.getRecipient().toString())) {
			throw new BadgeValidationException("The recipient details are not correct.  This badge may have been forged");
		}
		
	}

	protected BadgeAssertion getAssertionFromURL(URL url) {
		try {
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			try {
				return new BadgeAssertion(IOUtils.toString(is), "temp");
			} finally {
				IOUtils.closeQuietly(is);
			}
		} catch (IOException e) {
			throw new BadgeValidationException("Could not reach the validation URL supplied at " + url, e);
		}
	}

	@Override
	public EnrichedBadgeAssertion validateAndEnrich(URL assertion, List<URL> trustedIssuers, String expectedRecipient) throws MalformedURLException {
		validate(assertion, trustedIssuers, expectedRecipient);
		return new EnrichedBadgeAssertion(assertionFromURL(assertion));
	}

	@Override
	public void validate(URL assertion, List<URL> trustedIssuers, String expectedRecipient) {
		validate(assertion, trustedIssuers);
		BadgeAssertion ba = assertionFromURL(assertion);
		if (!ba.getRecipient().getIdentity().equals(expectedRecipient)) {
			throw new BadgeValidationException("This badge was issued to "+ba.getRecipient().getIdentity()+", not "+expectedRecipient+".  It may haave been forged");
		}
	}

}
