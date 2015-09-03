package apollo.datastore.utils.tests;

import static org.junit.Assert.*;

import apollo.datastore.AdminPermissions;
import apollo.datastore.AdminPermissionsBean;
import apollo.datastore.utils.admin.*;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.*;

public class AdminPermissionsTest {

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
        AdminPermissions adminPermissions = new AdminPermissions(userId, UsersPermissions.ALL_PERMISSIONS.getCode(), SessionsPermissions.ALL_PERMISSIONS.getCode(), SessionLogsPermissions.ALL_PERMISSIONS.getCode());
        assertEquals(adminPermissions.getKey().getName(), userId);
        assertEquals(UsersPermissions.ALL_PERMISSIONS.getCode(), adminPermissions.getUsersPermissions());
        assertEquals(SessionsPermissions.ALL_PERMISSIONS.getCode(), adminPermissions.getSessionsPermissions());
        assertEquals(SessionLogsPermissions.ALL_PERMISSIONS.getCode(), adminPermissions.getSessionLogsPermissions());
    }

    @Test
    public void testConstructorWithDefaultPermissions() {
        String userId = "admin";
        AdminPermissions adminPermissions = new AdminPermissions(userId);
        assertEquals(adminPermissions.getKey().getName(), userId);
        assertEquals(UsersPermissions.DEFAULT_PERMISSIONS.getCode(), adminPermissions.getUsersPermissions());
        assertEquals(SessionsPermissions.DEFAULT_PERMISSIONS.getCode(), adminPermissions.getSessionsPermissions());
        assertEquals(SessionLogsPermissions.DEFAULT_PERMISSIONS.getCode(), adminPermissions.getSessionLogsPermissions());
    }

    @Test
    public void testConstructorFromEntity() {
        String userId = "admin";
        AdminPermissions adminPermissionsDummy = new AdminPermissions(userId);
        AdminPermissions adminPermissions = new AdminPermissions(adminPermissionsDummy.getEntity());
        assertEquals(adminPermissions.getKey().getName(), userId);
        assertEquals(UsersPermissions.DEFAULT_PERMISSIONS.getCode(), adminPermissions.getUsersPermissions());
        assertEquals(SessionsPermissions.DEFAULT_PERMISSIONS.getCode(), adminPermissions.getSessionsPermissions());
        assertEquals(SessionLogsPermissions.DEFAULT_PERMISSIONS.getCode(), adminPermissions.getSessionLogsPermissions());
    }

    @Test
    public void testBean() {
        String userId = "admin";
        AdminPermissions adminPermissions = new AdminPermissions(userId, UsersPermissions.ALL_PERMISSIONS.getCode(), SessionsPermissions.ALL_PERMISSIONS.getCode(), SessionLogsPermissions.ALL_PERMISSIONS.getCode());
        AdminPermissionsBean adminPermissionsBean = new AdminPermissionsBean(adminPermissions);

        assertEquals(adminPermissionsBean.getViewUsers(), (adminPermissions.getUsersPermissions() & UsersPermissions.VIEW_USERS.getCode()) == UsersPermissions.VIEW_USERS.getCode());
        assertEquals(adminPermissionsBean.getViewUsersPermissions(), (adminPermissions.getUsersPermissions() & UsersPermissions.VIEW_USERS_PERMISSIONS.getCode()) == UsersPermissions.VIEW_USERS_PERMISSIONS.getCode());

        assertEquals(adminPermissionsBean.getViewSessions(), (adminPermissions.getSessionsPermissions() & SessionsPermissions.VIEW_SESSIONS.getCode()) == SessionsPermissions.VIEW_SESSIONS.getCode());
        assertEquals(adminPermissionsBean.getViewSessionsPermissions(), (adminPermissions.getSessionsPermissions() & SessionsPermissions.VIEW_SESSIONS_PERMISSIONS.getCode()) == SessionsPermissions.VIEW_SESSIONS_PERMISSIONS.getCode());

        assertEquals(adminPermissionsBean.getViewSessionLogs(), (adminPermissions.getSessionLogsPermissions() & SessionLogsPermissions.VIEW_SESSION_LOGS.getCode()) == SessionLogsPermissions.VIEW_SESSION_LOGS.getCode());
        assertEquals(adminPermissionsBean.getViewSessionLogsPermissions(), (adminPermissions.getSessionLogsPermissions() & SessionLogsPermissions.VIEW_SESSION_LOGS_PERMISSIONS.getCode()) == SessionLogsPermissions.VIEW_SESSION_LOGS_PERMISSIONS.getCode());
    }
}
