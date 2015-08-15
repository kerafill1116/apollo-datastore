package apollo.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("serial")
public class ResetPasswordRequest implements Serializable {

    public static final int DEFAULT_DAYS_OF_EXPIRATION = 3;

    private Entity entity;

    public ResetPasswordRequest(String requestId, Key userKey, Date dateRequested) {
        this.entity = new Entity(DatastoreProperties.KIND.getName(), requestId);

        this.entity.setProperty(DatastoreProperties.REQUEST_ID.getName(), requestId);
        this.entity.setProperty(DatastoreProperties.USER_KEY.getName(), userKey);
        this.entity.setProperty(DatastoreProperties.DATE_REQUESTED.getName(), MiscFunctions.toUTCDateString(dateRequested));

        Calendar dateOfExpiration = MiscFunctions.toCalendar(dateRequested);
        dateOfExpiration.add(Calendar.DATE, DEFAULT_DAYS_OF_EXPIRATION);
        this.entity.setProperty(DatastoreProperties.DATE_OF_EXPIRATION.getName(), MiscFunctions.toUTCDateString(dateOfExpiration.getTime()));

        this.entity.setProperty(DatastoreProperties.APPROVED.getName(), false);
        this.entity.setProperty(DatastoreProperties.DATE_APPROVED.getName(), null);
    }

    public ResetPasswordRequest(Entity entity) {
        this.entity = entity;
    }

    public String getRequestId() {
        return (String)this.entity.getProperty(DatastoreProperties.REQUEST_ID.getName());
    }

    public Key getUserKey() {
        return (Key)this.entity.getProperty(DatastoreProperties.USER_KEY.getName());
    }

    public Date getDateRequested() {
        return MiscFunctions.toUTCDate((String)this.entity.getProperty(DatastoreProperties.DATE_REQUESTED.getName()));
    }

    public Date getDateOfExpiration() {
        return MiscFunctions.toUTCDate((String)this.entity.getProperty(DatastoreProperties.DATE_OF_EXPIRATION.getName()));
    }

    public boolean getApproved() {
        return (boolean)this.entity.getProperty(DatastoreProperties.APPROVED.getName());
    }

    public void setApproved(boolean approved) {
        this.entity.setProperty(DatastoreProperties.APPROVED.getName(), approved);
    }

    public Date getDateApproved() {
        return MiscFunctions.toUTCDate((String)this.entity.getProperty(DatastoreProperties.DATE_APPROVED.getName()));
    }

    public void setDateApproved(Date dateApproved) {
        this.entity.setProperty(DatastoreProperties.DATE_APPROVED.getName(), MiscFunctions.toUTCDateString(dateApproved));
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
        KIND("ResetPasswordRequest"),
        REQUEST_ID("requestId"),
        USER_KEY("userKey"),
        DATE_REQUESTED("dateRequested"),
        DATE_OF_EXPIRATION("dateOfExpiration"),
        APPROVED("approved"),
        DATE_APPROVED("dateApproved");

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
