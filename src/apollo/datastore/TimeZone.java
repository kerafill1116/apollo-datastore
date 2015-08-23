package apollo.datastore;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TimeZone implements Serializable {

    public static final String GMT_DATE_FORMAT_ID = "GMT";

    private Entity entity;

    public TimeZone(String timeZoneId, int offset, String dateFormatId) {
        this.entity = new Entity(DatastoreProperties.KIND.getName(), timeZoneId);
        this.entity.setProperty(DatastoreProperties.TIME_ZONE_ID.getName(), timeZoneId);
        this.entity.setProperty(DatastoreProperties.OFFSET.getName(), offset);
        this.entity.setProperty(DatastoreProperties.DATE_FORMAT_ID.getName(), dateFormatId);
    }

    public TimeZone(Entity entity) {
        this.entity = entity;
    }

    public String getTimeZoneId() {
        return (String)this.entity.getProperty(DatastoreProperties.TIME_ZONE_ID.getName());
    }

    public int getOffset() {
        return (int)(long)this.entity.getProperty(DatastoreProperties.OFFSET.getName());
    }

    public String getDateFormatId() {
        return (String)this.entity.getProperty(DatastoreProperties.DATE_FORMAT_ID.getName());
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
        KIND("TimeZone"),
        TIME_ZONE_ID("timeZoneId"),
        OFFSET("offset"),
        DATE_FORMAT_ID("dateFormatId");

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
