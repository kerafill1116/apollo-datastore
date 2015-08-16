package apollo.datastore.utils;

public enum HtmlVariable {
    ACTIVATION_KEY("activation-key"),
    AVAILABLE("available"),
    CAUSE_OF_DISCONNECT("cause-of-disconnect"),
    EMAIL_ADDRESS("email-address"),
    ERROR("error"),
    EXCLUSIVE_SESSION("exclusive-session"),
    MAX_FAILED_ATTEMPTS("max-failed-attempts"),
    MAX_SESSIONS("max-sessions"),
    PASSWORD("password"),
    REMEMBER_ME("remember-me"),
    RESET_PASSWORD_REQUEST_ID("request-id"),
    SESSION_TIMEOUT("session-timeout"),
    TIME_ZONE_ID("time-zone-id"),
    USER_ID("user-id");

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
