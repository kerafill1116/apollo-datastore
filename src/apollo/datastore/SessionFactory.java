package apollo.datastore;

import apollo.datastore.Session.DatastoreProperties;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;

public class SessionFactory {

    private SessionFactory() { }

    public static Key add(DatastoreService datastore, Transaction txn, Session session)
            throws ConcurrentModificationException {

        return datastore.put(txn, session.getEntity());
    }

    public static ArrayList<Session> getSessionsByUserKey(DatastoreService datastore, Key userKey) {
        Filter userFilter = new FilterPredicate(DatastoreProperties.USER_KEY.getName(), FilterOperator.EQUAL, userKey);
        Query q = new Query(DatastoreProperties.KIND.getName());
        q.setFilter(userFilter);
        PreparedQuery pq = datastore.prepare(q);
        ArrayList<Session> sessions = new ArrayList<Session>();
        for(Entity result : pq.asIterable())
            sessions.add(new Session(result));
        return sessions;
    }

    public static ArrayList<Session> getSessionsByUserId(DatastoreService datastore, String userId) {
        return getSessionsByUserKey(datastore, KeyFactory.createKey(User.DatastoreProperties.KIND.getName(), userId));
    }

    public static Session getByKey(DatastoreService datastore, Transaction txn, Key key) {
        try {
            Entity sessionEntity = datastore.get(txn, key);
            return new Session(sessionEntity);
        }
        catch(EntityNotFoundException e) { }
        return null;
    }

    public static Session getBySessionId(DatastoreService datastore, Transaction txn, String sessionId) {
        return getByKey(datastore, txn, KeyFactory.createKey(DatastoreProperties.KIND.getName(), sessionId));
    }

    public static void disconnect(DatastoreService datastore, Transaction txn, Session session, Date dateSignedOut, CauseOfDisconnect causeOfDisconnect)
            throws ConcurrentModificationException {

        SessionLog sessionLog = new SessionLog(session);
        sessionLog.setDateSignedOut(dateSignedOut);
        sessionLog.setCauseOfDisconnect(causeOfDisconnect);
        SessionLogFactory.add(datastore, txn, sessionLog);
        datastore.delete(txn, session.getKey());
    }

    public static void disconnect(DatastoreService datastore, Transaction txn, ArrayList<Session> sessions, Date dateSignedOut, CauseOfDisconnect causeOfDisconnect)
            throws ConcurrentModificationException {

        for(Session session : sessions)
            disconnect(datastore, txn, session, dateSignedOut, causeOfDisconnect);
    }

    public static void updateLastSessionCheck(DatastoreService datastore, Transaction txn, Session session, Date lastSessionCheck)
            throws ConcurrentModificationException {

        session.setLastSessionCheck(lastSessionCheck);
        datastore.put(txn, session.getEntity());
    }
}
