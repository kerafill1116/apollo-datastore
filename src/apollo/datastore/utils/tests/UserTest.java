package apollo.datastore.utils.tests;

import static org.junit.Assert.*;
import apollo.datastore.MiscFunctions.HashAlgorithms;
import apollo.datastore.MiscFunctions;
import apollo.datastore.User;
import apollo.datastore.UserBean;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.*;

public class UserTest {

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
        assertEquals(user.getKey().getName(), userId);
        assertEquals(user.getUserId(), userId);
        assertEquals(user.getPassword(), MiscFunctions.getEncryptedHash(password, HashAlgorithms.SHA_256));
        assertEquals(user.getEmailAddress(), emailAddress);
        assertFalse(user.getActivated());
        assertFalse(user.getDisabled());
        assertEquals(user.getMaxSessions(), User.DEFAULT_MAX_SESSIONS);
        assertEquals(user.getExclusiveSession(), User.DEFAULT_EXCLUSIVE_SESSION);
        assertEquals(user.getSessionTimeout(), User.DEFAULT_SESSION_TIMEOUT);
        assertEquals(user.getFailedAttempts(), 0L);
        assertEquals(user.getMaxFailedAttempts(), User.DEFAULT_MAX_FAILED_ATTEMPTS);
        assertEquals(user.getUseTimeSlots(), User.DEFAULT_USE_TIME_SLOTS);
        assertEquals(user.getTimeZoneKey(), timeZoneKey);
    }

    @Test
    public void testConstructorFromEntity() {
        String userId = "admin";
        String password = "admin";
        String emailAddress = "kerafill1116@gmail.com";
        Key timeZoneKey = null;
        User userDummy = new User(userId, password, emailAddress, timeZoneKey);
        User user = new User(userDummy.getEntity());
        assertEquals(user.getKey().getName(), userId);
        assertEquals(user.getUserId(), userId);
        assertEquals(user.getPassword(), MiscFunctions.getEncryptedHash(password, HashAlgorithms.SHA_256));
        assertEquals(user.getEmailAddress(), emailAddress);
        assertFalse(user.getActivated());
        assertFalse(user.getDisabled());
        assertEquals(user.getMaxSessions(), User.DEFAULT_MAX_SESSIONS);
        assertEquals(user.getExclusiveSession(), User.DEFAULT_EXCLUSIVE_SESSION);
        assertEquals(user.getSessionTimeout(), User.DEFAULT_SESSION_TIMEOUT);
        assertEquals(user.getFailedAttempts(), 0L);
        assertEquals(user.getMaxFailedAttempts(), User.DEFAULT_MAX_FAILED_ATTEMPTS);
        assertEquals(user.getUseTimeSlots(), User.DEFAULT_USE_TIME_SLOTS);
        assertEquals(user.getTimeZoneKey(), timeZoneKey);
    }

    @Test
    public void testBean() {
        String userId = "admin";
        String password = "admin";
        String emailAddress = "kerafill1116@gmail.com";
        Key timeZoneKey = null;
        User user = new User(userId, password, emailAddress, timeZoneKey);
        UserBean userBean = new UserBean(user);
        assertEquals(user.getKey(), userBean.getKey());
        assertEquals(user.getUserId(), userBean.getUserId());
        assertEquals(user.getEmailAddress(), userBean.getEmailAddress());
        assertEquals(user.getActivated(), userBean.getActivated());
        assertEquals(user.getDisabled(), userBean.getDisabled());
        assertEquals(user.getMaxSessions(), userBean.getMaxSessions());
        assertEquals(user.getExclusiveSession(), userBean.getExclusiveSession());
        assertEquals(user.getSessionTimeout(), userBean.getSessionTimeout());
        assertEquals(user.getFailedAttempts(), userBean.getFailedAttempts());
        assertEquals(user.getMaxFailedAttempts(), userBean.getMaxFailedAttempts());
        assertEquals(user.getUseTimeSlots(), userBean.getUseTimeSlots());
        assertEquals(user.getTimeZoneKey(), userBean.getTimeZoneKey());
    }
}
