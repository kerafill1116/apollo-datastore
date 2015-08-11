package apollo.datastore;

public enum CauseOfDisconnect {
    NONE(0),
    EXCLUSIVE_SESSION(1),
    TIMED_OUT_SESSION(2),
    INVALID_CAUSE_OF_DISCONNECT(3);

    private static final CauseOfDisconnect[] causeOfDisconnectValues = CauseOfDisconnect.values();

    public static CauseOfDisconnect fromInteger(int x) {
        if(x < 0 || x > 3)
            return INVALID_CAUSE_OF_DISCONNECT;
        return causeOfDisconnectValues[x];
    }

    private final int code;

    private CauseOfDisconnect(int code) {
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
