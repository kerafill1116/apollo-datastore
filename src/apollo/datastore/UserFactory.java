package apollo.datastore;

import apollo.datastore.MiscFunctions.HashAlgorithms;
import apollo.datastore.User.DatastoreProperties;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import java.util.ConcurrentModificationException;
import java.util.Date;

public class UserFactory {

    private UserFactory() { }

    public static Key add(DatastoreService datastore, Transaction txn, User user)
            throws ConcurrentModificationException {

        return datastore.put(txn, user.getEntity());
    }

    public static User getByKey(DatastoreService datastore, Transaction txn, Key key) {
        try {
            Entity userEntity = datastore.get(txn, key);
            return new User(userEntity);
        }
        catch(EntityNotFoundException e) { }
        return null;
    }

    public static User getByUserId(DatastoreService datastore, Transaction txn, String userId) {
        return getByKey(datastore, txn, KeyFactory.createKey(DatastoreProperties.KIND.getName(), userId));
    }

    public static boolean updateFailedAttempts(DatastoreService datastore, Transaction txn, User user)
            throws ConcurrentModificationException {

        int failedAttempts = user.getFailedAttempts();
        int maxFailedAttempts = user.getMaxFailedAttempts();
        if(maxFailedAttempts >= 0 && failedAttempts >= maxFailedAttempts)
            user.setDisabled(true);
        else
            user.setFailedAttempts(++failedAttempts);
        datastore.put(txn, user.getEntity());
        return user.getDisabled();
    }

    public static void resetFailedAttempts(DatastoreService datastore, Transaction txn, User user)
            throws ConcurrentModificationException {

        user.setFailedAttempts(0);
        datastore.put(txn, user.getEntity());
    }

    public static void activate(DatastoreService datastore, Transaction txn, User user)
            throws ConcurrentModificationException {

        user.setActivated(true);
        datastore.put(txn, user.getEntity());
    }

    public static String resetPassword(DatastoreService datastore, Transaction txn, User user)
            throws ConcurrentModificationException {

        String newPassword = MiscFunctions.getEncryptedHash(MiscFunctions.toUTCDateString(new Date()), HashAlgorithms.MD5).substring(0, 8);
        user.setPassword(newPassword);
        datastore.put(txn, user.getEntity());
        return newPassword;
    }

    public static void update(DatastoreService datastore, Transaction txn, User user)
            throws ConcurrentModificationException {

        datastore.put(txn, user.getEntity());
    }
}
