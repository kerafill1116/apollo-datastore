package apollo.datastore;

public enum CauseOfDisconnect {
    NONE(0),
    EXCLUSIVE_SESSION(1),
    TIMED_OUT_SESSION(2),
    DISCONNECTED_SESSION(3),
    INVALID_CAUSE_OF_DISCONNECT(4);

    private static final CauseOfDisconnect[] causeOfDisconnectValues = CauseOfDisconnect.values();

    public static CauseOfDisconnect fromInteger(int x) {
        if(x < 0 || x > 3)
            return INVALID_CAUSE_OF_DISCONNECT;
        return causeOfDisconnectValues[x];
    }

    private final long code;

    private CauseOfDisconnect(long code) {
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
