package apollo.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SessionLog implements Serializable {

    private Entity entity;

    public SessionLog(Session session) {
        this.entity = new Entity(DatastoreProperties.KIND.getName(), session.getSessionId());

        this.entity.setProperty(DatastoreProperties.SESSION_ID.getName(), session.getSessionId());
        this.entity.setProperty(DatastoreProperties.USER_KEY.getName(), session.getUserKey());
        this.entity.setProperty(DatastoreProperties.DATE_SIGNED_IN.getName(), MiscFunctions.toUTCDateString(session.getDateSignedIn()));
        this.entity.setProperty(DatastoreProperties.LAST_SESSION_CHECK.getName(), MiscFunctions.toUTCDateString(session.getLastSessionCheck()));
        this.entity.setProperty(DatastoreProperties.SESSION_TIMEOUT.getName(), session.getSessionTimeout());

        this.entity.setProperty(DatastoreProperties.DATE_SIGNED_OUT.getName(), null);
        this.entity.setProperty(DatastoreProperties.CAUSE_OF_DISCONNECT.getName(), null);
    }

    public SessionLog(Entity sessionLogEntity) {
        this.entity = sessionLogEntity;
    }

    public void setDateSignedOut(Date dateSignedOut) {
        this.entity.setProperty(DatastoreProperties.DATE_SIGNED_OUT.getName(), MiscFunctions.toUTCDateString(dateSignedOut));
    }

    public CauseOfDisconnect getCauseOfDisconnect() {
        return CauseOfDisconnect.fromInteger((int)(long)this.entity.getProperty(DatastoreProperties.CAUSE_OF_DISCONNECT.getName()));
    }

    public void setCauseOfDisconnect(CauseOfDisconnect causeOfDisconnect) {
        this.entity.setProperty(DatastoreProperties.CAUSE_OF_DISCONNECT.getName(), causeOfDisconnect.getCode());
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
        KIND("SessionLog"),
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
