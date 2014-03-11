package com.surevine.neon;

import com.surevine.neon.badges.dao.BadgeClassDAO;
import com.surevine.neon.badges.dao.IssuerOrganisationDAO;
import com.surevine.neon.badges.model.BadgeClass;
import com.surevine.neon.badges.model.IssuerOrganisation;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

public class Bootstrap {
    private Logger logger = Logger.getLogger(Bootstrap.class);
    private IssuerOrganisationDAO issuerOrganisationDAO;
    private BadgeClassDAO badgeClassDAO;

    public void bootstrap() {
        InputStream bootstrapIS = Bootstrap.class.getClassLoader().getResourceAsStream("bootstrap.json");
        if (bootstrapIS != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(bootstrapIS));
            String line;
            StringBuffer buffer = new StringBuffer();
            try {
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
            } catch (IOException e) {
                logger.error("Could not read bootstrap.json",e);
            }
            
            if (buffer.length() > 0) {
                processBootstrapJSON(new JSONObject(buffer.toString()));
            }
        } else {
            logger.warn("Could not bootstrap as bootstrap.json was not found on the classpath.");
        }
    }
    
    public void bootstrap(String bootstrapJSON) {
        processBootstrapJSON(new JSONObject(bootstrapJSON));
    }

    public void bootstrap(JSONObject bootstrapJSON) {
        processBootstrapJSON(bootstrapJSON);
    }

    public void setIssuerOrganisationDAO(IssuerOrganisationDAO issuerOrganisationDAO) {
        this.issuerOrganisationDAO = issuerOrganisationDAO;
    }

    public void setBadgeClassDAO(BadgeClassDAO badgeClassDAO) {
        this.badgeClassDAO = badgeClassDAO;
    }

    private void processBootstrapJSON(JSONObject bootstrapJSON) {
        logger.info("Bootstrapping");
        parseIssuers(bootstrapJSON);
        parseBadges(bootstrapJSON);
    }

    private void parseIssuers(JSONObject bootstrapJSON) {
        if (bootstrapJSON.has("issuerOrganisations")) {
            JSONArray issuerOrganisations = bootstrapJSON.getJSONArray("issuerOrganisations");
            for (int i = 0; i < issuerOrganisations.length(); i++) {
                JSONObject issuerOrganisationJSO = issuerOrganisations.getJSONObject(i);
                if (issuerOrganisationJSO != null) {
                    try {
                        IssuerOrganisation issuerOrganisation = new IssuerOrganisation(issuerOrganisationJSO,issuerOrganisationJSO.getString("namespace"));
                        issuerOrganisationDAO.persist(issuerOrganisation);
                    } catch (MalformedURLException e) {
                        logger.error("Could not parse issuer organisation during bootstrap", e);
                    }
                }
            }
        }
    }

    private void parseBadges(JSONObject bootstrapJSON) {
        if (bootstrapJSON.has("badgeClasses")) {
            JSONArray badges = bootstrapJSON.getJSONArray("badgeClasses");
            for (int i = 0; i < badges.length(); i++) {
                JSONObject badgeJSO = badges.getJSONObject(i);
                if (badgeJSO != null) {
                    try {
                        BadgeClass badgeClass = new BadgeClass(badgeJSO,badgeJSO.getString("namespace"));
                        badgeClassDAO.persist(badgeClass);
                    } catch (MalformedURLException e) {
                        logger.error("Could not parse badge class during bootstrap", e);
                    }
                }
            }
        }
    }
}
