package apollo.datastore.utils;

public enum Error {
    NONE(0),

    ALREADY_ACTIVATED_USER(1),

    DISABLED_USER(2),

    ERROR_IN_ACTIVATION(3),
    ERROR_IN_REGISTER_NEW_USER(4),
    ERROR_IN_RESET_PASSWORD(5),
    ERROR_IN_RESET_PASSWORD_REQUEST(6),
    ERROR_IN_SESSION_CHECK(7),
    ERROR_IN_SIGN_IN(8),
    ERROR_IN_SIGN_OUT(9),

    EXCLUSIVE_SESSION(10),
    EXPIRED_REQUEST(11),

    INCORRECT_PASSWORD(12),

    INVALID_ACTIVATION_KEY(13),
    INVALID_EMAIL_ADDRESS(14),
    INVALID_USER_ID(15),

    MAXED_FAILED_ATTEMPTS(16),

    NON_EXISTENT_REQUEST(17),
    NON_EXISTENT_SESSION(18),
    NON_EXISTENT_USER(19),

    NOT_ACTIVATED_USER(20),
    NOT_AVAILABLE_USER_ID(21),

    REACHED_MAX_SESSIONS(22),

    REQUIRED_USER_ID(23),
    REQUIRED_REQUEST_ID(24),
    REQUIRED_EMAIL_ADDRESS(25),
    REQUIRED_ACTIVATION_KEY(26),

    NO_PERMISSIONS(27),
    ERROR_IN_SETTINGS(28),
    INVALID_EXCLUSIVE_SESSION(29),
    INVALID_SESSION_TIMEOUT(30),
    INVALID_MAX_SESSIONS(31),
    INVALID_MAX_FAILED_ATTEMPTS(32),
    ERROR_IN_CHANGE_PASSWORD(33),

    ALREADY_EXISTS_REQUEST(34),
    ERROR_IN_CHANGE_EMAIL_ADDRESS(35),
    ERROR_IN_USER_PERMISSIONS(36),

    INVALID_ERROR(37);

    private static final Error[] errorValues = Error.values();

    public static Error fromInteger(int x) {
        if(x < 0 || x > 28)
            return INVALID_ERROR;
        return errorValues[x];
    }

    private final int code;

    private Error(int code) {
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
