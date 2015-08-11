package apollo.datastore.utils.admin;

public enum SessionLogsPermissions {
    VIEW_SESSION_LOGS              (1 << 0),
    REMOVE_SESSION_LOGS            (1 << 1),
    VIEW_SESSION_LOGS_PERMISSIONS  (1 << 2),
    CHANGE_SESSION_LOGS_PERMISSIONS(1 << 3),
    ALL_PERMISSIONS                ((1 << 4) - 1),
    DEFAULT_PERMISSIONS            (0);

    private final int code;

    private SessionLogsPermissions(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return String.valueOf(this.code);
    }
}
