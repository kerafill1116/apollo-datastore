package apollo.datastore;

public enum AuthRequestAttribute {
    USER_KEY("user-key"),
    ADMIN_PERMISSIONS("admin-permissions"),
    USER_PERMISSIONS("user-permissions");

    private final String name;

    private AuthRequestAttribute(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
