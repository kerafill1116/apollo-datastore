package apollo.datastore.utils;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ErrorBean implements Serializable {

    public ErrorBean() { }

    private Error error;

    public int getCode() {
        return this.error.getCode();
    }

    // jsp:Bean setProperty auto .valueOf feature
    public void setConstant(Error error) {
        this.error = error;
    }
}
