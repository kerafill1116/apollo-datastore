package apollo.datastore.utils.user;

public enum SessionPermissions {
    VIEW_SESSIONS             (1 << 0),
    DISCONNECT_SESSIONS       (1 << 1),
    VIEW_SESSION_PERMISSIONS  (1 << 2),
    CHANGE_SESSION_PERMISSIONS(1 << 3),
    ALL_PERMISSIONS           ((1 << 4) - 1),
    DEFAULT_PERMISSIONS       (0);

    private final long code;

    private SessionPermissions(long code) {
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
