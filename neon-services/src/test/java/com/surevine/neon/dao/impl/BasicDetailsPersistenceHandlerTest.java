package com.surevine.neon.dao.impl;

import com.surevine.neon.dao.ImporterConfigurationDAO;
import com.surevine.neon.dao.ProfileDAO;
import com.surevine.neon.inload.MockDataImporter;
import com.surevine.neon.model.ProfileBean;
import com.surevine.neon.model.VCardTelBean;
import com.surevine.neon.redis.IPooledJedis;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

import static org.easymock.EasyMock.*;

/**
 * Tests BasicDetailsPersistenceHandler
 */
public class BasicDetailsPersistenceHandlerTest {
    private BasicDetailsPersistenceHandler underTest;
    private IPooledJedis mockJedis;
    private ImporterConfigurationDAO mockImporterConfigurationDAO;

    @Test
    public void testPersistGetsAllTheFieldsWeAreInterestedIn() {
        ProfileBean bean = new ProfileBean();
        bean.setUserID("user1");
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
        bean.setBio("In 1972 sent to prison by a military court for a crime they didn't commit. This man promptly escaped from a maximum-security stockade to the Los Angeles underground. Today, still wanted by the government, he survives as a soldier of fortune.");

        MockDataImporter importer = new MockDataImporter();
        importer.setImporterName("importer1");
        importer.setNamespace(ProfileDAO.NS_BASIC_DETAILS);
        String hkey = "NEON:" + ProfileDAO.NS_PROFILE_PREFIX + ":" + bean.getUserID() + ":" + ProfileDAO.NS_BASIC_DETAILS;
        
        expect(mockJedis.hset(hkey, BasicDetailsPersistenceHandler.FIELD_FN + ":" + importer.getImporterName(), bean.getVcard().getFn())).andReturn(1l);
        expect(mockJedis.hset(hkey, BasicDetailsPersistenceHandler.FIELD_EMAIL + ":" + importer.getImporterName(), bean.getVcard().getEmail())).andReturn(1l);
        expect(mockJedis.hset(hkey, BasicDetailsPersistenceHandler.FIELD_ORG + ":" + importer.getImporterName(), bean.getVcard().getOrg())).andReturn(1l);
        expect(mockJedis.hset(hkey, BasicDetailsPersistenceHandler.FIELD_TITLE + ":" + importer.getImporterName(), bean.getVcard().getTitle())).andReturn(1l);
        expect(mockJedis.hset(hkey, BasicDetailsPersistenceHandler.FIELD_PHOTO_MIME + ":" + importer.getImporterName(), bean.getVcard().getPhoto().getMimeType())).andReturn(1l);
        expect(mockJedis.hset(hkey, BasicDetailsPersistenceHandler.FIELD_PHOTO_URL + ":" + importer.getImporterName(), bean.getVcard().getPhoto().getPhotoURL().toString())).andReturn(1l);
        expect(mockJedis.hset(hkey, BasicDetailsPersistenceHandler.FIELD_BIO + ":" + importer.getImporterName(), bean.getBio())).andReturn(1l);
        expect(mockJedis.hset(hkey, BasicDetailsPersistenceHandler.FIELD_TEL + ":" + importer.getImporterName() + ":0", "n;11111")).andReturn(1l);
        expect(mockJedis.hset(hkey, BasicDetailsPersistenceHandler.FIELD_TEL + ":" + importer.getImporterName() + ":1", "s;22222")).andReturn(1l);
        replay(mockJedis);
        
        underTest.persist(bean, importer);
        verify(mockJedis);
    }

    @Before
    public void setup() {
        underTest = new BasicDetailsPersistenceHandler();
        mockJedis = createMock(IPooledJedis.class);
        mockImporterConfigurationDAO = createMock(ImporterConfigurationDAO.class);
        underTest.setJedis(mockJedis);
        underTest.setImporterConfigurationDAO(mockImporterConfigurationDAO);
    }

    @After
    public void after() {
        underTest = null;
        mockJedis = null;
        mockImporterConfigurationDAO = null;
    }
}
