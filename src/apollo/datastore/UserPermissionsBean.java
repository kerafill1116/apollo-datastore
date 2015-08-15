package apollo.datastore;

import apollo.datastore.utils.user.*;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserPermissionsBean implements Serializable {

    private int userPermissions;
    private int sessionPermissions;
    private int sessionLogPermissions;

    private boolean changePassword;
    private boolean viewEmailAddress;
    private boolean changeEmailAddress;
    private boolean viewUserPermissions;

    private boolean viewSessions;
    private boolean viewSessionPermissions;

    private boolean viewSessionLogs;
    private boolean viewSessionLogPermissions;

    public UserPermissionsBean(UserPermissions userPermissions) {
        this.userPermissions = userPermissions.getUserPermissions();
        this.sessionPermissions = userPermissions.getSessionPermissions();
        this.sessionLogPermissions = userPermissions.getSessionLogPermissions();

        this.changePassword = (this.userPermissions & UserPermissions2.CHANGE_PASSWORD.getCode()) == UserPermissions2.CHANGE_PASSWORD.getCode();
        this.viewEmailAddress = (this.userPermissions & UserPermissions2.VIEW_EMAIL_ADDRESS.getCode()) == UserPermissions2.VIEW_EMAIL_ADDRESS.getCode();
        this.changeEmailAddress = (this.userPermissions & UserPermissions2.CHANGE_EMAIL_ADDRESS.getCode()) == UserPermissions2.CHANGE_EMAIL_ADDRESS.getCode();
        this.viewUserPermissions = (this.userPermissions & UserPermissions2.VIEW_USER_PERMISSIONS.getCode()) == UserPermissions2.VIEW_USER_PERMISSIONS.getCode();

        this.viewSessions = (this.sessionPermissions & SessionPermissions.VIEW_SESSIONS.getCode()) == SessionPermissions.VIEW_SESSIONS.getCode();
        this.viewSessionPermissions = (this.sessionPermissions & SessionPermissions.VIEW_SESSION_PERMISSIONS.getCode()) == SessionPermissions.VIEW_SESSION_PERMISSIONS.getCode();

        this.viewSessionLogs = (this.sessionLogPermissions & SessionLogPermissions.VIEW_SESSION_LOGS.getCode()) == SessionLogPermissions.VIEW_SESSION_LOGS.getCode();
        this.viewSessionLogPermissions = (this.sessionLogPermissions & SessionLogPermissions.VIEW_SESSION_LOG_PERMISSIONS.getCode()) == SessionLogPermissions.VIEW_SESSION_LOG_PERMISSIONS.getCode();
    }

    public boolean getChangePassword() {
        return this.changePassword;
    }

    public boolean getViewEmailAddress() {
        return this.viewEmailAddress;
    }

    public boolean getChangeEmailAddress() {
        return this.changeEmailAddress;
    }

    public boolean getViewUserPermissions() {
        return this.viewUserPermissions;
    }

    public boolean getViewSessions() {
        return this.viewSessions;
    }

    public boolean getViewSessionPermissions() {
        return this.viewSessionPermissions;
    }

    public boolean getViewSessionLogs() {
        return this.viewSessionLogs;
    }

    public boolean getViewSessionLogPermissions() {
        return this.viewSessionLogPermissions;
    }

}
