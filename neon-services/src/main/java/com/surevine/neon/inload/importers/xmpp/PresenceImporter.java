package com.surevine.neon.inload.importers.xmpp;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.inload.importers.AbstractDataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.StatusBean;

public class PresenceImporter extends AbstractDataImporter implements DataImporter {

	private static final Logger LOG = Logger.getLogger(PresenceImporter.class);

	private static final String IMPORTER_NAME = "XMPP_PRESENCE_IMPORTER";

	private String presenceHost = "http://10.66.2.68:9090";
	private String xmppDomain="10.66.2.68";
	private String externalStatusPrefix = "EXTERNAL";
	private String externalLocationName = "Centre";
	private String internalLocationName = "Office";

	protected String[] supportedNamespaces = {
			ProfileDAO.NS_BASIC_DETAILS,
			ProfileDAO.NS_STATUS
	};

	@Override
	public void updateSpecificConfiguration(Map<String, String> config) {
		if(config.containsKey("presenceHost")) {
			presenceHost=config.get("presenceHost");
		}
		if(config.containsKey("xmppDomain")) {
			xmppDomain=config.get("xmppDomain");
		}
		if(config.containsKey("externalStatusPrefix")) {
			externalStatusPrefix=config.get("externalStatusPrefix");
		}
		if(config.containsKey("externalLocationName")) {
			externalLocationName=config.get("externalLocationName");
		}
		if(config.containsKey("internalLocationName")) {
			internalLocationName=config.get("internalLocationName");
		}
		LOG.debug("presenceHost for "+IMPORTER_NAME+": "+presenceHost);
		LOG.debug("xmppDomain for "+IMPORTER_NAME+": "+xmppDomain);
		LOG.debug("externalStatusPrefix for "+IMPORTER_NAME+": "+externalStatusPrefix);
		LOG.debug("externalLocationName for "+IMPORTER_NAME+": "+externalLocationName);
		LOG.debug("internalLocationName for "+IMPORTER_NAME+": "+internalLocationName);
	}

	@Override
	protected void runImportImplementation(String userID) {

		ProfileBean userProfile = new ProfileBean();

		String jid = userID + "@" + xmppDomain;

		// Augment basic profile details with Jid
		userProfile.getVcard().setJid(jid);

		String presenceURL = presenceHost + "/plugins/presence/status?jid={username}&type=xml";

		LOG.info("Getting XMPP presence for "+jid);
        String rawXMLString = getRawWebData(jid, presenceURL);

		Document presenceXML = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(rawXMLString));
			presenceXML = builder.parse(is);

		} catch (SAXException e) {
			LOG.warn(e);
		} catch (IOException e) {
			LOG.warn(e);
		} catch (ParserConfigurationException e) {
			LOG.warn(e);
		}

        LOG.debug("Parsed XMPP Presence into "+presenceXML);

        userProfile.setUserID(userID);
        StatusBean userStatus = userProfile.getStatus();

        userStatus.setPresence("online");
		userStatus.setLocation(internalLocationName);
		userStatus.setPresenceLastUpdated(new Date());

		NodeList showElements = presenceXML.getElementsByTagName("show");
		if(showElements.getLength() > 0) {
			String showText = showElements.item(0).getTextContent();
			// Rewrite chat status for improved readability on clientside.
			if(showText.equalsIgnoreCase("chat")) {
				showText = "available";
			}
			userStatus.setPresence(showText);
		} else {
			NamedNodeMap presenceAttrs = presenceXML.getElementsByTagName("presence").item(0).getAttributes();
			Node type = presenceAttrs.getNamedItem("type");
			if(type != null) {
				if(type.getNodeValue().equalsIgnoreCase("unavailable")) {
					userStatus.setPresence("offline");
				}
			}
		}

		NodeList statusElements = presenceXML.getElementsByTagName("status");
		if(statusElements.getLength() > 0) {
			String status = statusElements.item(0).getTextContent();
			if(status.startsWith(externalStatusPrefix)) {
				userStatus.setLocation(externalLocationName);
			}
		}

		LOG.trace("Finished importing XMPP presence.  Persisting");
		profileDAO.persistProfile(userProfile, this);
		LOG.info("Import of "+userID+"'s presence from XMPP server complete");

	}

	@Override
	public String getImporterName() {
		return IMPORTER_NAME;
	}

	@Override
	public String[] getSupportedNamespaces() {
		return supportedNamespaces;
	}

}
