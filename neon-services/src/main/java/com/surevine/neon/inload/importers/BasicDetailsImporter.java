package com.surevine.neon.inload.importers;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.DataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.VCardTelBean;
import org.apache.log4j.Logger;

import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * Mock importer that acts as an example and proof of concept for the importer framework
 */
public class BasicDetailsImporter extends AbstractDataImporter implements DataImporter {
    Logger logger = Logger.getLogger(BasicDetailsImporter.class);
    
    private static final String IMPORTER_NAME = "VCARD_BASIC_DETAILS";
    
    @Override
    public String getImporterName() {
        return IMPORTER_NAME;
    }

    @Override
    public String getNamespace() {
        return ProfileDAO.NS_BASIC_DETAILS;
    }

    @Override
    protected void runImportImplementation(String userID) throws DataImportException {
        logger.debug(getImporterName() + " is importing data for " + userID);
        // TODO: mocked for testing import mechanism
        ProfileBean bean = new ProfileBean();
        bean.setUserID(userID);
        bean.getVcard().setOrg("HR");
        bean.getVcard().setFn("Dave Smith");
        bean.getVcard().setEmail("dsmith@localhost");
        bean.getVcard().setTitle("Java Developer");
        try {
            bean.getVcard().getPhoto().setMimeType("image/gif");
            bean.getVcard().getPhoto().setPhotoURL(new URL("http://someurl/image.gif"));
        } catch (Exception e) {
            // do nothing
        }

        bean.getVcard().getTelephoneNumbers().add(new VCardTelBean("n", "11111"));
        bean.getVcard().getTelephoneNumbers().add(new VCardTelBean("s", "22222"));
        
        profileDAO.persistProfile(bean,this);
    }

	@Override
	public int getSourcePriority() {
		return 0;
	}
}
