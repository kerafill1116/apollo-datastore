package apollo.datastore.utils.admin;

public enum SessionsPermissions {
    VIEW_SESSIONS              (1 << 0),
    DISCONNECT_SESSIONS        (1 << 1),
    VIEW_SESSIONS_PERMISSIONS  (1 << 2),
    CHANGE_SESSIONS_PERMISSIONS(1 << 3),
    ALL_PERMISSIONS            ((1 << 4) - 1),
    DEFAULT_PERMISSIONS        (0);

    private final long code;

    private SessionsPermissions(long code) {
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
