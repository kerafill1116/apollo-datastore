package apollo.datastore.tests;

import static org.junit.Assert.*;

import apollo.datastore.UserPermissions;
import apollo.datastore.UserPermissionsBean;
import apollo.datastore.utils.user.*;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.*;

public class UserPermissionsTest {

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
    public void testConstructorWithExplicitPermissions() {
        String userId = "admin";
        UserPermissions userPermissions = new UserPermissions(userId, UserPermissions2.ALL_PERMISSIONS.getCode(), SessionPermissions.ALL_PERMISSIONS.getCode(), SessionLogPermissions.ALL_PERMISSIONS.getCode());
        Key key = userPermissions.getKey();
        assertEquals(key.getKind(), UserPermissions.DatastoreProperties.KIND.getName());
        assertEquals(key.getName(), userId);
        assertEquals(UserPermissions2.ALL_PERMISSIONS.getCode(), userPermissions.getUserPermissions());
        assertEquals(SessionPermissions.ALL_PERMISSIONS.getCode(), userPermissions.getSessionPermissions());
        assertEquals(SessionLogPermissions.ALL_PERMISSIONS.getCode(), userPermissions.getSessionLogPermissions());
    }

    @Test
    public void testConstructorWithDefaultPermissions() {
        String userId = "admin";
        UserPermissions userPermissions = new UserPermissions(userId);
        Key key = userPermissions.getKey();
        assertEquals(key.getKind(), UserPermissions.DatastoreProperties.KIND.getName());
        assertEquals(key.getName(), userId);
        assertEquals(UserPermissions2.DEFAULT_PERMISSIONS.getCode(), userPermissions.getUserPermissions());
        assertEquals(SessionPermissions.DEFAULT_PERMISSIONS.getCode(), userPermissions.getSessionPermissions());
        assertEquals(SessionLogPermissions.DEFAULT_PERMISSIONS.getCode(), userPermissions.getSessionLogPermissions());
    }

    @Test
    public void testConstructorFromEntity() {
        String userId = "admin";
        UserPermissions userPermissionsDummy = new UserPermissions(userId);
        UserPermissions userPermissions = new UserPermissions(userPermissionsDummy.getEntity());
        Key key = userPermissions.getKey();
        assertEquals(key.getKind(), UserPermissions.DatastoreProperties.KIND.getName());
        assertEquals(key.getName(), userId);
        assertEquals(UserPermissions2.DEFAULT_PERMISSIONS.getCode(), userPermissions.getUserPermissions());
        assertEquals(SessionPermissions.DEFAULT_PERMISSIONS.getCode(), userPermissions.getSessionPermissions());
        assertEquals(SessionLogPermissions.DEFAULT_PERMISSIONS.getCode(), userPermissions.getSessionLogPermissions());
    }

    @Test
    public void testBean() {
        String userId = "admin";
        UserPermissions userPermissions = new UserPermissions(userId, UserPermissions2.ALL_PERMISSIONS.getCode(), SessionPermissions.ALL_PERMISSIONS.getCode(), SessionLogPermissions.ALL_PERMISSIONS.getCode());
        UserPermissionsBean userPermissionsBean = new UserPermissionsBean(userPermissions);

        assertEquals(userPermissionsBean.getChangePassword(), (userPermissions.getUserPermissions() & UserPermissions2.CHANGE_PASSWORD.getCode()) == UserPermissions2.CHANGE_PASSWORD.getCode());
        assertEquals(userPermissionsBean.getViewEmailAddress(), (userPermissions.getUserPermissions() & UserPermissions2.VIEW_EMAIL_ADDRESS.getCode()) == UserPermissions2.VIEW_EMAIL_ADDRESS.getCode());
        assertEquals(userPermissionsBean.getChangeEmailAddress(), (userPermissions.getUserPermissions() & UserPermissions2.CHANGE_EMAIL_ADDRESS.getCode()) == UserPermissions2.CHANGE_EMAIL_ADDRESS.getCode());
        assertEquals(userPermissionsBean.getViewTimeZone(), (userPermissions.getUserPermissions() & UserPermissions2.VIEW_TIME_ZONE.getCode()) == UserPermissions2.VIEW_TIME_ZONE.getCode());
        assertEquals(userPermissionsBean.getChangeTimeZone(), (userPermissions.getUserPermissions() & UserPermissions2.CHANGE_TIME_ZONE.getCode()) == UserPermissions2.CHANGE_TIME_ZONE.getCode());
        assertEquals(userPermissionsBean.getViewSessionTimeout(), (userPermissions.getUserPermissions() & UserPermissions2.VIEW_SESSION_TIMEOUT.getCode()) == UserPermissions2.VIEW_SESSION_TIMEOUT.getCode());
        assertEquals(userPermissionsBean.getChangeSessionTimeout(), (userPermissions.getUserPermissions() & UserPermissions2.CHANGE_SESSION_TIMEOUT.getCode()) == UserPermissions2.CHANGE_SESSION_TIMEOUT.getCode());
        assertEquals(userPermissionsBean.getViewMaxSessions(), (userPermissions.getUserPermissions() & UserPermissions2.VIEW_MAX_SESSIONS.getCode()) == UserPermissions2.VIEW_MAX_SESSIONS.getCode());
        assertEquals(userPermissionsBean.getChangeMaxSessions(), (userPermissions.getUserPermissions() & UserPermissions2.CHANGE_MAX_SESSIONS.getCode()) == UserPermissions2.CHANGE_MAX_SESSIONS.getCode());
        assertEquals(userPermissionsBean.getViewExclusiveSession(), (userPermissions.getUserPermissions() & UserPermissions2.VIEW_EXCLUSIVE_SESSION.getCode()) == UserPermissions2.VIEW_EXCLUSIVE_SESSION.getCode());
        assertEquals(userPermissionsBean.getChangeExclusiveSession(), (userPermissions.getUserPermissions() & UserPermissions2.CHANGE_EXCLUSIVE_SESSION.getCode()) == UserPermissions2.CHANGE_EXCLUSIVE_SESSION.getCode());
        assertEquals(userPermissionsBean.getViewMaxFailedAttempts(), (userPermissions.getUserPermissions() & UserPermissions2.VIEW_MAX_FAILED_ATTEMPTS.getCode()) == UserPermissions2.VIEW_MAX_FAILED_ATTEMPTS.getCode());
        assertEquals(userPermissionsBean.getChangeMaxFailedAttempts(), (userPermissions.getUserPermissions() & UserPermissions2.CHANGE_MAX_FAILED_ATTEMPTS.getCode()) == UserPermissions2.CHANGE_MAX_FAILED_ATTEMPTS.getCode());
        assertEquals(userPermissionsBean.getViewDisabledStatus(), (userPermissions.getUserPermissions() & UserPermissions2.VIEW_DISABLED_STATUS.getCode()) == UserPermissions2.VIEW_DISABLED_STATUS.getCode());
        assertEquals(userPermissionsBean.getViewActivatedStatus(), (userPermissions.getUserPermissions() & UserPermissions2.VIEW_ACTIVATED_STATUS.getCode()) == UserPermissions2.VIEW_ACTIVATED_STATUS.getCode());
        assertEquals(userPermissionsBean.getViewUserPermissions(), (userPermissions.getUserPermissions() & UserPermissions2.VIEW_USER_PERMISSIONS.getCode()) == UserPermissions2.VIEW_USER_PERMISSIONS.getCode());
        assertEquals(userPermissionsBean.getChangeUserPermissions(), (userPermissions.getUserPermissions() & UserPermissions2.CHANGE_USER_PERMISSIONS.getCode()) == UserPermissions2.CHANGE_USER_PERMISSIONS.getCode());

        assertEquals(userPermissionsBean.getViewSessions(), (userPermissions.getSessionPermissions() & SessionPermissions.VIEW_SESSIONS.getCode()) == SessionPermissions.VIEW_SESSIONS.getCode());
        assertEquals(userPermissionsBean.getDisconnectSessions(), (userPermissions.getSessionPermissions() & SessionPermissions.DISCONNECT_SESSIONS.getCode()) == SessionPermissions.DISCONNECT_SESSIONS.getCode());
        assertEquals(userPermissionsBean.getViewSessionPermissions(), (userPermissions.getSessionPermissions() & SessionPermissions.VIEW_SESSION_PERMISSIONS.getCode()) == SessionPermissions.VIEW_SESSION_PERMISSIONS.getCode());
        assertEquals(userPermissionsBean.getChangeSessionPermissions(), (userPermissions.getSessionPermissions() & SessionPermissions.CHANGE_SESSION_PERMISSIONS.getCode()) == SessionPermissions.CHANGE_SESSION_PERMISSIONS.getCode());

        assertEquals(userPermissionsBean.getViewSessionLogs(), (userPermissions.getSessionLogPermissions() & SessionLogPermissions.VIEW_SESSION_LOGS.getCode()) == SessionLogPermissions.VIEW_SESSION_LOGS.getCode());
        assertEquals(userPermissionsBean.getRemoveSessionLogs(), (userPermissions.getSessionLogPermissions() & SessionLogPermissions.REMOVE_SESSION_LOGS.getCode()) == SessionLogPermissions.REMOVE_SESSION_LOGS.getCode());
        assertEquals(userPermissionsBean.getViewSessionLogPermissions(), (userPermissions.getSessionLogPermissions() & SessionLogPermissions.VIEW_SESSION_LOG_PERMISSIONS.getCode()) == SessionLogPermissions.VIEW_SESSION_LOG_PERMISSIONS.getCode());
        assertEquals(userPermissionsBean.getChangeSessionLogPermissions(), (userPermissions.getSessionLogPermissions() & SessionLogPermissions.CHANGE_SESSION_LOG_PERMISSIONS.getCode()) == SessionLogPermissions.CHANGE_SESSION_LOG_PERMISSIONS.getCode());
    }
}
