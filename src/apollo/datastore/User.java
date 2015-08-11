package apollo.datastore;

import apollo.datastore.MiscFunctions.HashAlgorithms;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.Date;

public class User {

    public static final int DEFAULT_MAX_SESSIONS = 1;
    public static final boolean DEFAULT_EXCLUSIVE_SESSION = false;
    public static final int DEFAULT_SESSION_TIMEOUT = 1800;
    public static final int DEFAULT_MAX_FAILED_ATTEMPTS = 10;
    public static final boolean DEFAULT_USE_TIME_SLOTS = false;

    private Entity entity;

    /**
     * Class constructor, default activated is false, default disabled is false
     * @param userId
     * @param password - unencrypted password, will be encrypted using SHA 256 encryption
     * @param emailAddress
     * @param timeZoneKey
     */
    public User(String userId, String password, String emailAddress, Key timeZoneKey) {
        this.entity = new Entity(DatastoreProperties.KIND.getName(), userId);

        this.entity.setProperty(DatastoreProperties.USER_ID.getName(), userId);
        this.entity.setProperty(DatastoreProperties.PASSWORD.getName(), MiscFunctions.getEncryptedHash(password, HashAlgorithms.SHA_256));
        this.entity.setProperty(DatastoreProperties.EMAIL_ADDRESS.getName(), emailAddress);

        this.entity.setProperty(DatastoreProperties.ACTIVATED.getName(), false);
        this.entity.setProperty(DatastoreProperties.DISABLED.getName(), false);
        this.entity.setProperty(DatastoreProperties.MAX_SESSIONS.getName(), DEFAULT_MAX_SESSIONS);
        this.entity.setProperty(DatastoreProperties.EXCLUSIVE_SESSION.getName(), DEFAULT_EXCLUSIVE_SESSION);
        this.entity.setProperty(DatastoreProperties.SESSION_TIMEOUT.getName(), DEFAULT_SESSION_TIMEOUT);
        this.entity.setProperty(DatastoreProperties.FAILED_ATTEMPTS.getName(), 0);
        this.entity.setProperty(DatastoreProperties.MAX_FAILED_ATTEMPTS.getName(), DEFAULT_MAX_FAILED_ATTEMPTS);
        this.entity.setProperty(DatastoreProperties.USE_TIME_SLOTS.getName(), DEFAULT_USE_TIME_SLOTS);

        this.entity.setProperty(DatastoreProperties.TIME_ZONE_KEY.getName(), timeZoneKey);

        this.entity.setProperty(DatastoreProperties.DATE_CREATED.getName(), MiscFunctions.toUTCDateString(new Date()));
    }

    public User(Entity entity) {
        this.entity = entity;
    }

    public String getUserId() {
        return (String)this.entity.getProperty(DatastoreProperties.USER_ID.getName());
    }

    /**
     * Gets the password for this {@link User} object
     * @return encrypted password in SHA 256 encryption
     */
    public String getPassword() {
        return (String)this.entity.getProperty(DatastoreProperties.PASSWORD.getName());
    }

    /**
     * Sets the password for this {@link User} object
     * @param password - unencrypted password, will be encrypted using SHA 256 encryption
     */
    public void setPassword(String password) {
        this.entity.setProperty(DatastoreProperties.PASSWORD.getName(), MiscFunctions.getEncryptedHash(password, HashAlgorithms.SHA_256));
    }

    public String getEmailAddress() {
        return (String)this.entity.getProperty(DatastoreProperties.EMAIL_ADDRESS.getName());
    }

    public void setEmailAddress(String emailAddress) {
        this.entity.setProperty(DatastoreProperties.EMAIL_ADDRESS.getName(), emailAddress);
    }

    public boolean getActivated() {
        return (boolean)this.entity.getProperty(DatastoreProperties.ACTIVATED.getName());
    }

    public void setActivated(boolean activated) {
        this.entity.setProperty(DatastoreProperties.ACTIVATED.getName(), activated);
    }

    public boolean getDisabled() {
        return (boolean)this.entity.getProperty(DatastoreProperties.DISABLED.getName());
    }

    public void setDisabled(boolean disabled) {
        this.entity.setProperty(DatastoreProperties.DISABLED.getName(), disabled);
    }

    public int getMaxSessions() {
        return (int)(long)this.entity.getProperty(DatastoreProperties.MAX_SESSIONS.getName());
    }

    public void setMaxSessions(int maxSessions) {
        this.entity.setProperty(DatastoreProperties.MAX_SESSIONS.getName(), maxSessions);
    }

    public boolean getExclusiveSession() {
        return (boolean)this.entity.getProperty(DatastoreProperties.EXCLUSIVE_SESSION.getName());
    }

    public void setExclusiveSession(boolean exclusiveSession) {
        this.entity.setProperty(DatastoreProperties.EXCLUSIVE_SESSION.getName(), exclusiveSession);
    }

    public int getSessionTimeout() {
        return (int)(long)this.entity.getProperty(DatastoreProperties.SESSION_TIMEOUT.getName());
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.entity.setProperty(DatastoreProperties.SESSION_TIMEOUT.getName(), sessionTimeout);
    }

    public int getFailedAttempts() {
        return (int)(long)this.entity.getProperty(DatastoreProperties.FAILED_ATTEMPTS.getName());
    }

    public void setFailedAttempts(int failedAttempts) {
        this.entity.setProperty(DatastoreProperties.FAILED_ATTEMPTS.getName(), failedAttempts);
    }

    public int getMaxFailedAttempts() {
        return (int)(long)this.entity.getProperty(DatastoreProperties.MAX_FAILED_ATTEMPTS.getName());
    }

    public void setMaxFailedAttempts(int maxFailedAttempts) {
        this.entity.setProperty(DatastoreProperties.MAX_FAILED_ATTEMPTS.getName(), maxFailedAttempts);
    }

    public boolean getUseTimeSlots() {
        return (boolean)this.entity.getProperty(DatastoreProperties.USE_TIME_SLOTS.getName());
    }

    public Key getTimeZoneKey() {
        return (Key)this.entity.getProperty(DatastoreProperties.TIME_ZONE_KEY.getName());
    }

    public void setTimeZoneKey(Key timeZoneKey) {
        this.entity.setProperty(DatastoreProperties.TIME_ZONE_KEY.getName(), timeZoneKey);
    }

    public String getTimeZoneKeyString() {
        String timeZoneKeyString = null;
        try {
            timeZoneKeyString = KeyFactory.keyToString(this.getTimeZoneKey());
        }
        catch(IllegalArgumentException e) { }
        catch(NullPointerException e) { }
        return timeZoneKeyString;
    }

    public Date getDateCreated() {
        return MiscFunctions.toUTCDate((String)this.entity.getProperty(DatastoreProperties.DATE_CREATED.getName()));
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Key getKey() {
        return this.entity.getKey();
    }

    public String getKeyString() {
        return KeyFactory.keyToString(this.entity.getKey());
    }

    public enum DatastoreProperties {
        KIND("User"),
        USER_ID("userId"),
        PASSWORD("password"),
        EMAIL_ADDRESS("emailAddress"),
        ACTIVATED("activated"),
        DISABLED("disabled"),
        MAX_SESSIONS("maxSessions"),
        EXCLUSIVE_SESSION("exclusiveSession"),
        SESSION_TIMEOUT("sessionTimeout"),
        FAILED_ATTEMPTS("failedAttempts"),
        MAX_FAILED_ATTEMPTS("maxFailedAttempts"),
        USE_TIME_SLOTS("useTimeSlots"),
        TIME_ZONE_KEY("timeZoneKey"),
        DATE_CREATED("dateCreated");

        private final String name;

        private DatastoreProperties(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
