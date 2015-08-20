package apollo.datastore;

import apollo.datastore.ResetPasswordRequest.DatastoreProperties;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import java.util.ConcurrentModificationException;

public class ResetPasswordRequestFactory {

    private ResetPasswordRequestFactory() { }

    public static Key add(DatastoreService datastore, Transaction txn, ResetPasswordRequest resetPasswordRequest)
            throws ConcurrentModificationException {

        return datastore.put(txn, resetPasswordRequest.getEntity());
    }

    public static ResetPasswordRequest getByKey(DatastoreService datastore, Transaction txn, Key key) {
        try {
            Entity resetPasswordRequestEntity = datastore.get(txn, key);
            return new ResetPasswordRequest(resetPasswordRequestEntity);
        }
        catch(EntityNotFoundException e) { }
        return null;
    }

    public static ResetPasswordRequest getByUserId(DatastoreService datastore, Transaction txn, String userId) {
        return getByKey(datastore, txn, KeyFactory.createKey(DatastoreProperties.KIND.getName(), userId));
    }

    public static void remove(DatastoreService datastore, Transaction txn, ResetPasswordRequest resetPasswordRequest)
            throws ConcurrentModificationException {

        datastore.delete(txn, resetPasswordRequest.getKey());
    }
}
