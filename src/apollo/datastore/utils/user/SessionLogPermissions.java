package apollo.datastore.utils.user;

public enum SessionLogPermissions {
    VIEW_SESSION_LOGS             (1 << 0),
    REMOVE_SESSION_LOGS           (1 << 1),
    VIEW_SESSION_LOG_PERMISSIONS  (1 << 2),
    CHANGE_SESSION_LOG_PERMISSIONS(1 << 3),
    ALL_PERMISSIONS               ((1 << 4) - 1),
    DEFAULT_PERMISSIONS           (0);

    private final long code;

    private SessionLogPermissions(long code) {
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
