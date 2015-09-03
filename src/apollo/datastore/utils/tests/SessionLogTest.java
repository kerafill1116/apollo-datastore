package apollo.datastore.utils.tests;

import static org.junit.Assert.*;
import apollo.datastore.MiscFunctions.HashAlgorithms;
import apollo.datastore.MiscFunctions;
import apollo.datastore.Session;
import apollo.datastore.SessionLog;
import apollo.datastore.User;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import java.util.Date;

import org.junit.*;

public class SessionLogTest {

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
        SessionLog sessionLog = new SessionLog(session);
        assertEquals(sessionLog.getKey().getName(), sessionId);
        assertEquals(sessionLog.getSessionId(), sessionId);
        assertEquals(sessionLog.getUserKey(), user.getKey());
        assertEquals(sessionLog.getDateSignedIn().toString(), dateNow.toString());
        assertEquals(sessionLog.getLastSessionCheck().toString(), dateNow.toString());
        assertEquals(sessionLog.getSessionTimeout(), user.getSessionTimeout());
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

        Session session = new Session(sessionId, user, dateNow);
        SessionLog sessionLogDummy = new SessionLog(session);
        SessionLog sessionLog = new SessionLog(sessionLogDummy.getEntity());

        assertEquals(sessionLog.getKey().getName(), sessionId);
        assertEquals(sessionLog.getSessionId(), sessionId);
        assertEquals(sessionLog.getUserKey(), user.getKey());
        assertEquals(sessionLog.getDateSignedIn().toString(), dateNow.toString());
        assertEquals(sessionLog.getLastSessionCheck().toString(), dateNow.toString());
        assertEquals(sessionLog.getSessionTimeout(), user.getSessionTimeout());
    }
}
