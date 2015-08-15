package apollo.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Session implements Serializable {

    private Entity entity;

    public Session(String sessionId, User user, Date dateSignedIn) {
        this.entity = new Entity(DatastoreProperties.KIND.getName(), sessionId);

        this.entity.setProperty(DatastoreProperties.SESSION_ID.getName(), sessionId);
        this.entity.setProperty(DatastoreProperties.USER_KEY.getName(), user.getKey());
        this.entity.setProperty(DatastoreProperties.DATE_SIGNED_IN.getName(), MiscFunctions.toUTCDateString(dateSignedIn));
        this.entity.setProperty(DatastoreProperties.LAST_SESSION_CHECK.getName(), MiscFunctions.toUTCDateString(dateSignedIn));
        this.entity.setProperty(DatastoreProperties.SESSION_TIMEOUT.getName(), user.getSessionTimeout());

        this.entity.setProperty(DatastoreProperties.DATE_SIGNED_OUT.getName(), null);
        this.entity.setProperty(DatastoreProperties.CAUSE_OF_DISCONNECT.getName(), null);
    }

    public Session(Entity sessionEntity) {
        this.entity = sessionEntity;
    }

    public String getSessionId() {
        return (String)this.entity.getProperty(DatastoreProperties.SESSION_ID.getName());
    }

    public Key getUserKey() {
        return (Key)this.entity.getProperty(DatastoreProperties.USER_KEY.getName());
    }

    public Date getDateSignedIn() {
        return MiscFunctions.toUTCDate((String)this.entity.getProperty(DatastoreProperties.DATE_SIGNED_IN.getName()));
    }

    public Date getLastSessionCheck() {
        return MiscFunctions.toUTCDate((String)this.entity.getProperty(DatastoreProperties.LAST_SESSION_CHECK.getName()));
    }

    public void setLastSessionCheck(Date lastSessionCheck) {
        this.entity.setProperty(DatastoreProperties.LAST_SESSION_CHECK.getName(), MiscFunctions.toUTCDateString(lastSessionCheck));
    }

    public int getSessionTimeout() {
        return (int)(long)this.entity.getProperty(DatastoreProperties.SESSION_TIMEOUT.getName());
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
        KIND("Session"),
        SESSION_ID("sessionId"),
        USER_KEY("userKey"),
        DATE_SIGNED_IN("dateSignedIn"),
        DATE_SIGNED_OUT("dateSignedOut"),
        LAST_SESSION_CHECK("lastSessionCheck"),
        SESSION_TIMEOUT("sessionTimeout"),
        CAUSE_OF_DISCONNECT("causeOfDisconnect");

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
