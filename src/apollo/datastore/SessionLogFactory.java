package apollo.datastore;

import apollo.datastore.SessionLog.DatastoreProperties;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import java.util.ConcurrentModificationException;

public class SessionLogFactory {

    private SessionLogFactory() { }

    public static Key add(DatastoreService datastore, Transaction txn, SessionLog sessionLog)
            throws ConcurrentModificationException {

        return datastore.put(txn, sessionLog.getEntity());
    }

    public static SessionLog getByKey(DatastoreService datastore, Transaction txn, Key key) {
        try {
            Entity sessionLogEntity = datastore.get(txn, key);
            return new SessionLog(sessionLogEntity);
        }
        catch(EntityNotFoundException e) { }
        return null;
    }

    public static SessionLog getBySessionId(DatastoreService datastore, Transaction txn, String sessionId) {
        return getByKey(datastore, txn, KeyFactory.createKey(DatastoreProperties.KIND.getName(), sessionId));
    }
}
