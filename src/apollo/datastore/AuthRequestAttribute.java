package apollo.datastore;

public enum AuthRequestAttribute {
    USER("user"),
    ADMIN_PERMISSIONS("adminPermissions"),
    USER_PERMISSIONS("userPermissions");

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
