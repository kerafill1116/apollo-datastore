package apollo.datastore;

import apollo.datastore.ResetPasswordRequest.DatastoreProperties;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import java.util.ConcurrentModificationException;
import java.util.Date;

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

    public static ResetPasswordRequest getByRequestId(DatastoreService datastore, Transaction txn, String requestId) {
        return getByKey(datastore, txn, KeyFactory.createKey(DatastoreProperties.KIND.getName(), requestId));
    }

    public static void approve(DatastoreService datastore, Transaction txn, ResetPasswordRequest resetPasswordRequest, Date dateApproved)
            throws ConcurrentModificationException {

        resetPasswordRequest.setApproved(true);
        resetPasswordRequest.setDateApproved(dateApproved);
        datastore.put(txn, resetPasswordRequest.getEntity());
    }
}
