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
    private boolean viewTimeZone;
    private boolean changeTimeZone;
    private boolean viewSessionTimeout;
    private boolean changeSessionTimeout;
    private boolean viewMaxSessions;
    private boolean changeMaxSessions;
    private boolean viewExclusiveSession;
    private boolean changeExclusiveSession;
    private boolean viewMaxFailedAttempts;
    private boolean changeMaxFailedAttempts;
    private boolean viewDisabledStatus;
    private boolean viewActivatedStatus;
    private boolean viewUserPermissions;

    private boolean viewSessions;
    private boolean viewSessionPermissions;

    private boolean viewSessionLogs;
    private boolean viewSessionLogPermissions;

    public UserPermissionsBean(UserPermissions userPermissions) {
        this.userPermissions = userPermissions.getUserPermissions();
        this.sessionPermissions = userPermissions.getSessionPermissions();
        this.sessionLogPermissions = userPermissions.getSessionLogPermissions();
        this.evaluatePermissions();
    }

    public UserPermissionsBean() {
        this.userPermissions = UserPermissions2.DEFAULT_PERMISSIONS.getCode();
        this.sessionPermissions = SessionPermissions.DEFAULT_PERMISSIONS.getCode();
        this.sessionLogPermissions = SessionLogPermissions.DEFAULT_PERMISSIONS.getCode();
        this.evaluatePermissions();
    }

    private void evaluatePermissions() {
        this.changePassword = (this.userPermissions & UserPermissions2.CHANGE_PASSWORD.getCode()) == UserPermissions2.CHANGE_PASSWORD.getCode();
        this.viewEmailAddress = (this.userPermissions & UserPermissions2.VIEW_EMAIL_ADDRESS.getCode()) == UserPermissions2.VIEW_EMAIL_ADDRESS.getCode();
        this.changeEmailAddress = (this.userPermissions & UserPermissions2.CHANGE_EMAIL_ADDRESS.getCode()) == UserPermissions2.CHANGE_EMAIL_ADDRESS.getCode();
        this.viewTimeZone = (this.userPermissions & UserPermissions2.VIEW_TIME_ZONE.getCode()) == UserPermissions2.VIEW_TIME_ZONE.getCode();
        this.changeTimeZone = (this.userPermissions & UserPermissions2.CHANGE_TIME_ZONE.getCode()) == UserPermissions2.CHANGE_TIME_ZONE.getCode();
        this.viewSessionTimeout = (this.userPermissions & UserPermissions2.VIEW_SESSION_TIMEOUT.getCode()) == UserPermissions2.VIEW_SESSION_TIMEOUT.getCode();
        this.changeSessionTimeout = (this.userPermissions & UserPermissions2.CHANGE_SESSION_TIMEOUT.getCode()) == UserPermissions2.CHANGE_SESSION_TIMEOUT.getCode();
        this.viewMaxSessions = (this.userPermissions & UserPermissions2.VIEW_MAX_SESSIONS.getCode()) == UserPermissions2.VIEW_MAX_SESSIONS.getCode();
        this.changeMaxSessions = (this.userPermissions & UserPermissions2.CHANGE_MAX_SESSIONS.getCode()) == UserPermissions2.CHANGE_MAX_SESSIONS.getCode();
        this.viewExclusiveSession = (this.userPermissions & UserPermissions2.VIEW_EXCLUSIVE_SESSION.getCode()) == UserPermissions2.VIEW_EXCLUSIVE_SESSION.getCode();
        this.changeExclusiveSession = (this.userPermissions & UserPermissions2.CHANGE_EXCLUSIVE_SESSION.getCode()) == UserPermissions2.CHANGE_EXCLUSIVE_SESSION.getCode();
        this.viewMaxFailedAttempts = (this.userPermissions & UserPermissions2.VIEW_MAX_FAILED_ATTEMPTS.getCode()) == UserPermissions2.VIEW_MAX_FAILED_ATTEMPTS.getCode();
        this.changeMaxFailedAttempts = (this.userPermissions & UserPermissions2.CHANGE_MAX_FAILED_ATTEMPTS.getCode()) == UserPermissions2.CHANGE_MAX_FAILED_ATTEMPTS.getCode();
        this.viewDisabledStatus = (this.userPermissions & UserPermissions2.VIEW_DISABLED_STATUS.getCode()) == UserPermissions2.VIEW_DISABLED_STATUS.getCode();
        this.viewActivatedStatus = (this.userPermissions & UserPermissions2.VIEW_ACTIVATED_STATUS.getCode()) == UserPermissions2.VIEW_ACTIVATED_STATUS.getCode();

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

    public boolean getViewTimeZone() {
        return this.viewTimeZone;
    }

    public boolean getChangeTimeZone() {
        return this.changeTimeZone;
    }

    public boolean getViewSessionTimeout() {
        return this.viewSessionTimeout;
    }

    public boolean getChangeSessionTimeout() {
        return this.changeSessionTimeout;
    }

    public boolean getViewMaxSessions() {
        return this.viewMaxSessions;
    }

    public boolean getChangeMaxSessions() {
        return this.changeMaxSessions;
    }

    public boolean getViewExclusiveSession() {
        return this.viewExclusiveSession;
    }

    public boolean getChangeExclusiveSession() {
        return this.changeExclusiveSession;
    }

    public boolean getViewMaxFailedAttempts() {
        return this.viewMaxFailedAttempts;
    }

    public boolean getChangeMaxFailedAttempts() {
        return this.changeMaxFailedAttempts;
    }

    public boolean getViewDisabledStatus() {
        return this.viewDisabledStatus;
    }

    public boolean getViewActivatedStatus() {
        return this.viewActivatedStatus;
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
