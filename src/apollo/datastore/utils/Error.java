package apollo.datastore.utils;

public enum Error {
    NONE(0),

    ALREADY_ACTIVATED_USER(1),
    ALREADY_APPROVED_REQUEST(2),

    DISABLED_USER(3),

    ERROR_IN_ACTIVATION(4),
    ERROR_IN_REGISTER_NEW_USER(5),
    ERROR_IN_RESET_PASSWORD(6),
    ERROR_IN_RESET_PASSWORD_REQUEST(7),
    ERROR_IN_SESSION_CHECK(8),
    ERROR_IN_SIGN_IN(9),
    ERROR_IN_SIGN_OUT(10),

    EXCLUSIVE_SESSION(11),
    EXPIRED_REQUEST(12),

    INCORRECT_PASSWORD(13),

    INVALID_ACTIVATION_KEY(14),
    INVALID_EMAIL_ADDRESS(15),
    INVALID_USER_ID(16),

    MAXED_FAILED_ATTEMPTS(17),

    NON_EXISTENT_REQUEST(18),
    NON_EXISTENT_SESSION(19),
    NON_EXISTENT_USER(20),

    NOT_ACTIVATED_USER(21),
    NOT_AVAILABLE_USER_ID(22),

    REACHED_MAX_SESSIONS(23),

    REQUIRED_USER_ID(24),
    REQUIRED_REQUEST_ID(25),
    REQUIRED_EMAIL_ADDRESS(26),
    REQUIRED_ACTIVATION_KEY(27),

    INVALID_ERROR(28);

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
