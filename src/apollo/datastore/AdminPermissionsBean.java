package apollo.datastore;

import apollo.datastore.utils.admin.*;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AdminPermissionsBean implements Serializable {

    private long usersPermissions;
    private long sessionsPermissions;
    private long sessionLogsPermissions;

    private boolean viewUsers;
    private boolean removeUser;
    private boolean viewUsersPermissions;

    private boolean viewSessions;
    private boolean viewSessionsPermissions;

    private boolean viewSessionLogs;
    private boolean viewSessionLogsPermissions;

    public AdminPermissionsBean(AdminPermissions adminPermissions) {
        this.usersPermissions = adminPermissions.getUsersPermissions();
        this.sessionsPermissions = adminPermissions.getSessionsPermissions();
        this.sessionLogsPermissions = adminPermissions.getSessionLogsPermissions();
        this.evaluatePermissions();
    }

    public AdminPermissionsBean() {
        this.usersPermissions = UsersPermissions.DEFAULT_PERMISSIONS.getCode();
        this.sessionsPermissions = SessionsPermissions.DEFAULT_PERMISSIONS.getCode();
        this.sessionLogsPermissions = SessionLogsPermissions.DEFAULT_PERMISSIONS.getCode();
        this.evaluatePermissions();
    }

    private void evaluatePermissions() {
        this.viewUsers = (this.usersPermissions & UsersPermissions.VIEW_USERS.getCode()) == UsersPermissions.VIEW_USERS.getCode();
        this.removeUser = (this.usersPermissions & UsersPermissions.REMOVE_USER.getCode()) == UsersPermissions.REMOVE_USER.getCode();
        this.viewUsersPermissions = (this.usersPermissions & UsersPermissions.VIEW_USERS_PERMISSIONS.getCode()) == UsersPermissions.VIEW_USERS_PERMISSIONS.getCode();

        this.viewSessions = (this.sessionsPermissions & SessionsPermissions.VIEW_SESSIONS.getCode()) == SessionsPermissions.VIEW_SESSIONS.getCode();
        this.viewSessionsPermissions = (this.sessionsPermissions & SessionsPermissions.VIEW_SESSIONS_PERMISSIONS.getCode()) == SessionsPermissions.VIEW_SESSIONS_PERMISSIONS.getCode();

        this.viewSessionLogs = (this.sessionLogsPermissions & SessionLogsPermissions.VIEW_SESSION_LOGS.getCode()) == SessionLogsPermissions.VIEW_SESSION_LOGS.getCode();
        this.viewSessionLogsPermissions = (this.sessionLogsPermissions & SessionLogsPermissions.VIEW_SESSION_LOGS_PERMISSIONS.getCode()) == SessionLogsPermissions.VIEW_SESSION_LOGS_PERMISSIONS.getCode();
    }

    public boolean getViewUsers() {
        return this.viewUsers;
    }

    public boolean getRemoveUser() {
        return this.removeUser;
    }

    public boolean getViewUsersPermissions() {
        return this.viewUsersPermissions;
    }

    public boolean getViewSessions() {
        return this.viewSessions;
    }

    public boolean getViewSessionsPermissions() {
        return this.viewSessionsPermissions;
    }

    public boolean getViewSessionLogs() {
        return this.viewSessionLogs;
    }

    public boolean getViewSessionLogsPermissions() {
        return this.viewSessionLogsPermissions;
    }
}
