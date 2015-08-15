package apollo.datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class UserBean implements Serializable {

    private Key key;
    private String userId;
    private String emailAddress;
    private boolean activated;
    private boolean disabled;
    private int maxSessions;
    private boolean exclusiveSession;
    private int sessionTimeout;
    private int failedAttempts;
    private int maxFailedAttempts;
    private boolean useTimeSlots;
    private Key timeZoneKey;
    private Date dateCreated;

    public UserBean(User user) {
        this.key = user.getKey();
        this.userId = user.getUserId();
        this.emailAddress = user.getEmailAddress();
        this.activated = user.getActivated();
        this.disabled = user.getDisabled();
        this.maxSessions = user.getMaxSessions();
        this.exclusiveSession = user.getExclusiveSession();
        this.sessionTimeout = user.getSessionTimeout();
        this.failedAttempts = user.getFailedAttempts();
        this.maxFailedAttempts = user.getMaxFailedAttempts();
        this.useTimeSlots = user.getUseTimeSlots();
        this.timeZoneKey = user.getTimeZoneKey();
        this.dateCreated = user.getDateCreated();
    }

    public Key getKey() {
        return this.key;
    }

    public String getKeyString() {
        return KeyFactory.keyToString(this.key);
    }

    public String getUserId() {
        return this.userId;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public boolean getActivated() {
        return this.activated;
    }

    public boolean getDisabled() {
        return this.disabled;
    }

    public int getMaxSessions() {
        return this.maxSessions;
    }

    public boolean getExclusiveSession() {
        return this.exclusiveSession;
    }

    public int getSessionTimeout() {
        return this.sessionTimeout;
    }

    public int getFailedAttempts() {
        return this.failedAttempts;
    }

    public int getMaxFailedAttempts() {
        return this.maxFailedAttempts;
    }

    public boolean getUseTimeSlots() {
        return this.useTimeSlots;
    }

    public Key getTimeZoneKey() {
        return this.timeZoneKey;
    }

    public String getTimeZoneKeyString() {
        String timeZoneKeyString = null;
        try {
            timeZoneKeyString = KeyFactory.keyToString(this.timeZoneKey);
        }
        catch(IllegalArgumentException e) { }
        catch(NullPointerException e) { }
        return timeZoneKeyString;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }
}
