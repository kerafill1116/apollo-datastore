package apollo.datastore.utils;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HtmlVariableBean implements Serializable {

    public HtmlVariableBean() { }

    private HtmlVariable htmlVariable;
    private String value;

    public String getName() {
        return this.htmlVariable.getName();
    }

    // jsp:Bean setProperty auto .valueOf feature
    public void setVarName(HtmlVariable varName) {
        this.htmlVariable = varName;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
