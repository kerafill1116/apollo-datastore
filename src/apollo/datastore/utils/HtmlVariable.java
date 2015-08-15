package apollo.datastore.utils;

public enum HtmlVariable {
    ERROR("error"),
    USER_ID("user-id"),
    PASSWORD("password"),
    REMEMBER_ME("remember-me"),
    CAUSE_OF_DISCONNECT("cause-of-disconnect"),
    RESET_PASSWORD_REQUEST_ID("request-id"),
    EMAIL_ADDRESS("email-address"),
    TIME_ZONE_ID("time-zone-id"),
    AVAILABLE("available"),
    ACTIVATION_KEY("activation-key"),
    SESSION_TIMEOUT("session-timeout"),
    MAX_SESSIONS("max-sessions"),
    EXCLUSIVE_SESSION("exclusive-session"),
    MAX_FAILED_ATTEMPTS("max-failed-attempts");

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
