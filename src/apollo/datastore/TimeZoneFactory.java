package apollo.datastore;

import apollo.datastore.TimeZone.DatastoreProperties;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import java.util.ConcurrentModificationException;

public class TimeZoneFactory {

    private TimeZoneFactory() { }

    public static Key add(DatastoreService datastore, Transaction txn, TimeZone timeZone)
            throws ConcurrentModificationException {

        return datastore.put(txn, timeZone.getEntity());
    }

    public static TimeZone getByKey(DatastoreService datastore, Transaction txn, Key key) {
        try {
            Entity timeZoneEntity = datastore.get(txn, key);
            return new TimeZone(timeZoneEntity);
        }
        catch(EntityNotFoundException e) { }
        return null;
    }

    public static TimeZone getByTimeZoneId(DatastoreService datastore, Transaction txn, String timeZoneId) {
        return getByKey(datastore, txn, KeyFactory.createKey(DatastoreProperties.KIND.getName(), timeZoneId));
    }
}
