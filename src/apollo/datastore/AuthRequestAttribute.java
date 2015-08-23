package apollo.datastore;

public enum AuthRequestAttribute {
    USER("user"),
    ADMIN_PERMISSIONS("adminPermissions"),
    USER_PERMISSIONS("userPermissions"),
    CHANGE_EMAIL_ADDRESS_REQUEST("changeEmailAddressRequest"),
    SESSIONS("sessions"),
    CURSOR_LIST("cursorList");

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
