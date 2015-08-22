package apollo.datastore.utils;

public enum HtmlVariable {
    ACTIVATION_KEY("activation-key"),
    AVAILABLE("available"),
    CANCEL("cancel"),
    CAUSE_OF_DISCONNECT("cause-of-disconnect"),
    CURRENT_PASSWORD("current-password"),
    EMAIL_ADDRESS("email-address"),
    ERROR("error"),
    EXCLUSIVE_SESSION("exclusive-session"),
    MAX_FAILED_ATTEMPTS("max-failed-attempts"),
    MAX_SESSIONS("max-sessions"),
    NEW_EMAIL_ADDRESS("new-email-address"),
    NEW_PASSWORD("new-password"),
    PASSWORD("password"),
    REMEMBER_ME("remember-me"),
    REQUEST_ID("request-id"),
    RESEND("resend"),
    SESSION_TIMEOUT("session-timeout"),
    TIME_ZONE_ID("time-zone-id"),
    USER_ID("user-id"),

    CHANGE_PASSWORD("change-password"),
    VIEW_EMAIL_ADDRESS("view-email-address"),
    CHANGE_EMAIL_ADDRESS("change-email-address"),
    VIEW_MAX_SESSIONS("view-max-sessions"),
    CHANGE_MAX_SESSIONS("change-max-sessions"),
    VIEW_EXCLUSIVE_SESSION("view-exclusive-session"),
    CHANGE_EXCLUSIVE_SESSION("change-exclusive-session"),
    VIEW_SESSION_TIMEOUT("view-session-timeout"),
    CHANGE_SESSION_TIMEOUT("change-session-timeout"),
    VIEW_MAX_FAILED_ATTEMPTS("view-max-failed-attempts"),
    CHANGE_MAX_FAILED_ATTEMPTS("change-max-failed-attempts"),
    VIEW_TIME_ZONE("view-time-zone"),
    CHANGE_TIME_ZONE("change-time-zone"),
    VIEW_DISABLED_STATUS("view-disabled-status"),
    VIEW_ACTIVATED_STATUS("view-activated-status"),
    VIEW_USER_PERMISSIONS("view-user-permissions"),
    CHANGE_USER_PERMISSIONS("change-user-permissions");

    private final String name;

    private HtmlVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override public String toString() {
        return this.name;
    }
}
