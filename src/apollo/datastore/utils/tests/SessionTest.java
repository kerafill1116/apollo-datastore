package apollo.datastore.utils.tests;

import static org.junit.Assert.*;
import apollo.datastore.MiscFunctions.HashAlgorithms;
import apollo.datastore.MiscFunctions;
import apollo.datastore.Session;
import apollo.datastore.User;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import java.util.Date;

import org.junit.*;

public class SessionTest {

    private final static LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @BeforeClass
    public static void setUp() {
        helper.setUp();
    }

    @AfterClass
    public static void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testConstructor() {
        String userId = "admin";
        String password = "admin";
        String emailAddress = "kerafill1116@gmail.com";
        Key timeZoneKey = null;
        User user = new User(userId, password, emailAddress, timeZoneKey);

        Date dateNow = new Date();
        String sessionId = MiscFunctions.getEncryptedHash(MiscFunctions.toUTCDateString(dateNow) + user.getKeyString(), HashAlgorithms.SHA_256);

        Session session = new Session(sessionId, user, dateNow);
        assertEquals(session.getKey().getName(), sessionId);
        assertEquals(session.getSessionId(), sessionId);
        assertEquals(session.getUserKey(), user.getKey());
        assertEquals(session.getDateSignedIn().toString(), dateNow.toString());
        assertEquals(session.getLastSessionCheck().toString(), dateNow.toString());
        assertEquals(session.getSessionTimeout(), user.getSessionTimeout());
    }

    @Test
    public void testConstructorFromEntity() {
        String userId = "admin";
        String password = "admin";
        String emailAddress = "kerafill1116@gmail.com";
        Key timeZoneKey = null;
        User user = new User(userId, password, emailAddress, timeZoneKey);

        Date dateNow = new Date();
        String sessionId = MiscFunctions.getEncryptedHash(MiscFunctions.toUTCDateString(dateNow) + user.getKeyString(), HashAlgorithms.SHA_256);

        Session sessionDummy = new Session(sessionId, user, dateNow);
        Session session = new Session(sessionDummy.getEntity());
        assertEquals(session.getKey().getName(), sessionId);
        assertEquals(session.getSessionId(), sessionId);
        assertEquals(session.getUserKey(), user.getKey());
        assertEquals(session.getDateSignedIn().toString(), dateNow.toString());
        assertEquals(session.getLastSessionCheck().toString(), dateNow.toString());
        assertEquals(session.getSessionTimeout(), user.getSessionTimeout());
    }
}
