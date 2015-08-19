package apollo.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class ChangeEmailAddressRequest implements Serializable {

    private Entity entity;

    public ChangeEmailAddressRequest(String requestId, Key userKey, String emailAddress, Date dateRequested) {
        this.entity = new Entity(DatastoreProperties.KIND.getName(), userKey.getName());

        this.entity.setProperty(DatastoreProperties.REQUEST_ID.getName(), requestId);
        this.entity.setProperty(DatastoreProperties.USER_KEY.getName(), userKey);
        this.entity.setProperty(DatastoreProperties.EMAIL_ADDRESS.getName(), emailAddress);
        this.entity.setProperty(DatastoreProperties.DATE_REQUESTED.getName(), MiscFunctions.toUTCDateString(dateRequested));
    }

    public ChangeEmailAddressRequest(Entity entity) {
        this.entity = entity;
    }

    public String getRequestId() {
        return (String)this.entity.getProperty(DatastoreProperties.REQUEST_ID.getName());
    }

    public Key getUserKey() {
        return (Key)this.entity.getProperty(DatastoreProperties.USER_KEY.getName());
    }

    public String getEmailAddress() {
        return (String)this.entity.getProperty(DatastoreProperties.EMAIL_ADDRESS.getName());
    }

    public Date getDateRequested() {
        return MiscFunctions.toUTCDate((String)this.entity.getProperty(DatastoreProperties.DATE_REQUESTED.getName()));
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
        KIND("ChangeEmailAddressRequest"),
        REQUEST_ID("requestId"),
        EMAIL_ADDRESS("emailAddress"),
        USER_KEY("userKey"),
        DATE_REQUESTED("dateRequested");

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
