package apollo.datastore;

import apollo.datastore.ChangeEmailAddressRequest.DatastoreProperties;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import java.util.ConcurrentModificationException;

public class ChangeEmailAddressRequestFactory {

    private ChangeEmailAddressRequestFactory() { }

    public static Key add(DatastoreService datastore, Transaction txn, ChangeEmailAddressRequest changeEmailAddressRequest)
            throws ConcurrentModificationException {

        return datastore.put(txn, changeEmailAddressRequest.getEntity());
    }

    public static ChangeEmailAddressRequest getByKey(DatastoreService datastore, Transaction txn, Key key) {
        try {
            Entity changeEmailAddressRequestEntity = datastore.get(txn, key);
            return new ChangeEmailAddressRequest(changeEmailAddressRequestEntity);
        }
        catch(EntityNotFoundException e) { }
        return null;
    }

    public static ChangeEmailAddressRequest getByUserId(DatastoreService datastore, Transaction txn, String userId) {
        return getByKey(datastore, txn, KeyFactory.createKey(DatastoreProperties.KIND.getName(), userId));
    }

    public static void remove(DatastoreService datastore, Transaction txn, ChangeEmailAddressRequest changeEmailAddressRequest)
            throws ConcurrentModificationException {

        datastore.delete(txn, changeEmailAddressRequest.getKey());
    }
}
