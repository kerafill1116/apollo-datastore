package apollo.datastore;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;

@SuppressWarnings("serial")
public class ChangeEmailAddressRequestBean implements Serializable {

    private Key key;
    private String requestId;
    private Key userKey;
    private String emailAddress;
    private Date dateRequested;

    public ChangeEmailAddressRequestBean(ChangeEmailAddressRequest changeEmailAddressRequest) {
        this.key = changeEmailAddressRequest.getKey();
        this.requestId = changeEmailAddressRequest.getRequestId();
        this.userKey = changeEmailAddressRequest.getUserKey();
        this.emailAddress = changeEmailAddressRequest.getEmailAddress();
        this.dateRequested = changeEmailAddressRequest.getDateRequested();
    }

    public ChangeEmailAddressRequestBean() {
        this.key = null;
        this.requestId = "";
        this.userKey = null;
        this.emailAddress = "";
        this.dateRequested = new Date();
    }

    public Key getKey() {
        return this.key;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public Key getUserKey() {
        return this.userKey;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public Date getDateRequested() {
        return this.dateRequested;
    }
}
