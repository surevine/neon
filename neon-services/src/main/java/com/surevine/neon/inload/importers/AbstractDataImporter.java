package com.surevine.neon.inload.importers;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.util.DateUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Abstract data importer contains all the basic functionality required for importer configuration and state management.
 * Most importers should extend this unless they have good reason not to.
 */
public abstract class AbstractDataImporter implements DataImporter {
	private ImporterConfigurationDAO configurationDAO;
    private Logger log = Logger.getLogger(AbstractDataImporter.class);
    private boolean clearConfig;
    private boolean configCleared;
    protected ProfileDAO profileDAO;
    protected String username=null;
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	protected transient String password=null;
    protected boolean useCertificate=false;
    protected String keyStoreType="pkcs12";
    private String keyStoreLocation="/home/simonw/cacerts";
    
    /**
     * Delegated import implementation
     * @param userID the user ID to run the import for
     */
    protected abstract void runImportImplementation(String userID);

    /**
     * Sets the implementation of the profile DAO
     * @param pd the profile DAO
     */
    public void setProfileDAO(ProfileDAO pd) {
        profileDAO=pd;
    }

    /**
     * Sets the configuration DAO instance
     * @param configurationDAO the configuration DAO
     */
    public void setConfigurationDAO(ImporterConfigurationDAO configurationDAO) {
        this.configurationDAO = configurationDAO;
    }

    @Override
    public void setAdditionalConfiguration(Map<String, String> configuration) {
        firstRunClearConfig();
        configurationDAO.addImporterConfiguration(getImporterName(), configuration);
    }

    @Override
    public int getSourcePriority() {
        return Integer.parseInt(configurationDAO.getStringConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_PRIORITY));
    }

    @Override
    public void runImport() {
        Set<String> userIDs = profileDAO.getUserIDList();
		log.info("Retrieving data for " + userIDs.size() + " users");
		Iterator<String> userID = userIDs.iterator();
		while (userID.hasNext()) {
			runImport(userID.next());
		}
	}

    @Override
    public void runImport(String userID) {
        try {
        	updateConfiguration();
            runImportImplementation(userID);
            configurationDAO.addImporterConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_LAST_IMPORT, DateUtil.dateToString(new Date()));
        } catch (DataImportException die) {
            log.warn("Importer " + getImporterName() + " failed to import data for user " + userID + "[" + die.getMessage() + "]");
        }
    }

    @Override
    public Date getLastRun() {
        String lastRunString = configurationDAO.getStringConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_LAST_IMPORT);
        Date lastRun = null;
        
        if (lastRunString != null) {
            lastRun = DateUtil.stringToDate(lastRunString);
        }
        
        return lastRun;
    }

    @Override
    public boolean cacheLapsed() {
        Date lastRun = getLastRun();
        if (lastRun == null) {
            return true;
        }
        
        String timeoutString = configurationDAO.getStringConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_IMPORTER_TIMEOUT);
        int cacheTimeout = 0; // if there's no cache timeout configured we assume it's zero and the import should run every time the scheduled system job runs
        
        if (timeoutString != null && !timeoutString.isEmpty()) {
            cacheTimeout = Integer.parseInt(timeoutString);
        }
        
        Long diffInMillis = new Date().getTime() - lastRun.getTime();
        
        return diffInMillis > (cacheTimeout * 1000);
    }

    @Override
    public boolean isEnabled() {
        return configurationDAO.getBooleanConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_ENABLED);
    }

    @Override
    public void setCacheTimeout(int timeout) {
        firstRunClearConfig();
        configurationDAO.addImporterConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_IMPORTER_TIMEOUT, timeout + "");
    }

    @Override
    public void setSourcePriority(int priority) {
        firstRunClearConfig();
        configurationDAO.addImporterConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_PRIORITY, priority + "");
    }

    @Override
    public void setEnabled(boolean enabled) {
        firstRunClearConfig();
        configurationDAO.addImporterConfigurationOption(getImporterName(), ImporterConfigurationDAO.NS_ENABLED, Boolean.toString(enabled));
    }
    
    protected String getAuthorizationHeader() {
    	return Base64.encodeBase64String(username.concat(":").concat(password).getBytes());
    }

    protected String getRawWebData(String userID, String urlBase) {
		log.info("Getting raw web content for "+userID);
		String rV=null;
		InputStream webIn=null;
		URL targetURL=null;
		try {
			targetURL=new URL(urlBase.replaceAll("\\{username\\}", userID));
			log.trace("Target URL for import: "+targetURL.toString());
			URLConnection connection = targetURL.openConnection();
			
			if (connection instanceof HttpsURLConnection && useCertificate) {
				try {
				HttpsURLConnection secureConnection = (HttpsURLConnection)connection;
				KeyStore ksClient=KeyStore.getInstance(keyStoreType);
				ksClient.load(new FileInputStream(keyStoreLocation), password.toCharArray());
				
				KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmf.init(ksClient, password.toCharArray());
				
				SSLContext ssl = SSLContext.getInstance("TLS");
				ssl.init(kmf.getKeyManagers(), null, null);
				SSLSocketFactory socketFactory = ssl.getSocketFactory();
				
				secureConnection.setSSLSocketFactory(socketFactory);
				}
				catch(KeyStoreException e) {
					log.warn("Issue handling the keystore", e);
				}
				catch (CertificateException e) {
					log.warn(e);
				}
				catch (NoSuchAlgorithmException e) {
					log.warn(e);
				}
				catch (UnrecoverableKeyException e) {
					log.warn(e);
				}
				catch (KeyManagementException e) {
					log.warn(e);
				}
			}
			
			else {
				if (username!=null) {
					connection.setRequestProperty("Authorization", "Basic "+getAuthorizationHeader());
				}
			}
			
			webIn = connection.getInputStream();
			rV=IOUtils.toString(webIn);
		}
		catch (MalformedURLException e) {
			log.error(e);
			throw new DataImportException(userID, this, "Could not generate a profile URL from "+urlBase, e);
		}
		catch (IOException ioe) {
			log.error(ioe);
			throw new DataImportException(userID, this, "Could not retrieve profile page "+targetURL, ioe);
		}
		finally {
			IOUtils.closeQuietly(webIn);
		}
		if (rV==null || rV.trim().equals("")) {
			throw new DataImportException(userID, this, "No data could be found for the user profile at "+targetURL);
		}
		if(log.isDebugEnabled()) {
			log.debug("Retrieved "+rV.length()+" charecters of profile data");
		}
		if (log.isTraceEnabled()) {
			log.trace("Raw profile data:  |"+rV+"|");
		}
		return rV;
	}
	
	@Override
	public final void updateConfiguration() {
		log.trace("Updating basic importer configuration for " + getImporterName());
		Map<String, String> config = configurationDAO.getConfigurationForImporter(getImporterName());
		if (config!=null) {
			username=config.get("username");
			if (username!=null) {
				log.debug("Username for "+getImporterName()+": "+username);
			}
			password = config.get("password");
			if (password!=null) {
				log.debug("Password for "+getImporterName()+": "+username);
			}
		}
		updateSpecificConfiguration(config);
	}
	
	protected void updateSpecificConfiguration(Map<String, String> config) {
		log.trace("No specific importer configuration to udpate for "+getImporterName());
	}

    /**
     * Clears this data importer's configuration if it hasn't already been done since instantiation
     */
    private void firstRunClearConfig() {
        if (clearConfig && !configCleared) {
            log.trace("Clearing old importer configuration for " + this.getImporterName());
            configurationDAO.clearImporterConfiguration(this.getImporterName());
            configCleared = true;
        } else {
            log.trace(this.getImporterName() + " is configured to retain old configuration.");
        }
    }

    /**
     * Whether this importer will clear its old configuration on instantiation
     * @param clearConfig whether or not this importer will clear its old configuration at initialisation
     */
    public void setClearConfig(boolean clearConfig) {
        this.clearConfig = clearConfig;
    }
}