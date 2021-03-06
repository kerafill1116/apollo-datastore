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
    private long maxSessions;
    private boolean exclusiveSession;
    private long sessionTimeout;
    private long failedAttempts;
    private long maxFailedAttempts;
    private boolean useTimeSlots;
    private Key timeZoneKey;
    private String timeZoneLocaleId;
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

    public UserBean() {
        this.key = null;
        this.userId = "";
        this.emailAddress = "";
        this.activated = false;
        this.disabled = false;
        this.maxSessions = User.DEFAULT_MAX_SESSIONS;
        this.exclusiveSession = User.DEFAULT_EXCLUSIVE_SESSION;
        this.sessionTimeout = User.DEFAULT_SESSION_TIMEOUT;
        this.failedAttempts = 0;
        this.maxFailedAttempts = User.DEFAULT_MAX_FAILED_ATTEMPTS;
        this.useTimeSlots = User.DEFAULT_USE_TIME_SLOTS;
        this.timeZoneKey = null;
        this.dateCreated = new Date();
    }

    public Key getKey() {
        return this.key;
    }

    public String getKeyString() {
        String keyString = null;
        try {
            keyString = KeyFactory.keyToString(this.key);
        }
        catch(IllegalArgumentException e) { }
        catch(NullPointerException e) { }
        return keyString;
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

    public long getMaxSessions() {
        return this.maxSessions;
    }

    public boolean getExclusiveSession() {
        return this.exclusiveSession;
    }

    public long getSessionTimeout() {
        return this.sessionTimeout;
    }

    public long getFailedAttempts() {
        return this.failedAttempts;
    }

    public long getMaxFailedAttempts() {
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

    public String getTimeZoneLocaleId() {
        return this.timeZoneLocaleId;
    }

    public void setTimeZoneLocaleId(String timeZoneLocaleId) {
        this.timeZoneLocaleId = timeZoneLocaleId;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }
}
