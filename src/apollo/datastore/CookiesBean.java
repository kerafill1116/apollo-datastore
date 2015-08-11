package apollo.datastore;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CookiesBean implements Serializable {

    public CookiesBean() { }

    private Cookies cookie;
    private String value;

    public String getName() {
        return this.cookie.getName();
    }

    // jsp:Bean setProperty auto .valueOf feature
    public void setVarName(Cookies varName) {
        this.cookie = varName;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
