package apollo.datastore.utils.admin;

public enum UsersPermissions {
    VIEW_USERS                (1 << 0),
    ADD_NEW_USER              (1 << 1),
    // CHANGE_USER_ID            (1 << 2),
    CHANGE_PASSWORD           (1 << 3),
    VIEW_EMAIL_ADDRESS        (1 << 4),
    CHANGE_EMAIL_ADDRESS      (1 << 5),
    VIEW_MAX_SESSIONS         (1 << 6),
    CHANGE_MAX_SESSIONS       (1 << 7),
    VIEW_EXCLUSIVE_SESSION    (1 << 8),
    CHANGE_EXCLUSIVE_SESSION  (1 << 9),
    VIEW_SESSION_TIMEOUT      (1 << 10),
    CHANGE_SESSION_TIMEOUT    (1 << 11),
    VIEW_MAX_FAILED_ATTEMPTS  (1 << 12),
    CHANGE_MAX_FAILED_ATTEMPTS(1 << 13),
    VIEW_TIME_ZONE            (1 << 14),
    CHANGE_TIME_ZONE          (1 << 15),
    VIEW_DISABLED_STATUS      (1 << 16),
    CHANGE_DISABLED_STATUS    (1 << 17),
    VIEW_ACTIVATED_STATUS     (1 << 18),
    CHANGE_ACTIVATED_STATUS   (1 << 19),
    REMOVE_USER               (1 << 20),
    VIEW_USERS_PERMISSIONS    (1 << 21),
    CHANGE_USERS_PERMISSIONS  (1 << 22),
    ALL_PERMISSIONS           ((1 << 23) - 1),
    DEFAULT_PERMISSIONS       (0);

    private final long code;

    private UsersPermissions(long code) {
        this.code = code;
    }

    public long getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return String.valueOf(this.code);
    }
}
