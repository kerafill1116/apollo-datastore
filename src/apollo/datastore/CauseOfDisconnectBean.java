package apollo.datastore;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CauseOfDisconnectBean implements Serializable {

    public CauseOfDisconnectBean() { }

    private CauseOfDisconnect causeOfDisconnect;

    public long getCode() {
        return this.causeOfDisconnect.getCode();
    }

    // jsp:Bean setProperty auto .valueOf feature
    public void setConstant(CauseOfDisconnect causeOfDisconnect) {
        this.causeOfDisconnect = causeOfDisconnect;
    }
}
