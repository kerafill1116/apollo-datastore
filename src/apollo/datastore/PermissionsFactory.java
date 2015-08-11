package apollo.datastore;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import java.util.ConcurrentModificationException;

public class PermissionsFactory {

    private PermissionsFactory() { }

    public static Key addAdminPermissions(DatastoreService datastore, Transaction txn, AdminPermissions adminPermissions)
            throws ConcurrentModificationException {

        return datastore.put(txn, adminPermissions.getEntity());
    }

    public static Key addUserPermissions(DatastoreService datastore, Transaction txn, UserPermissions userPermissions)
            throws ConcurrentModificationException {

        return datastore.put(txn, userPermissions.getEntity());
    }

    public static AdminPermissions getAdminPermissionsByKey(DatastoreService datastore, Transaction txn, Key key) {
        try {
            Entity adminPermissionsEntity = datastore.get(txn, key);
            return new AdminPermissions(adminPermissionsEntity);
        }
        catch(EntityNotFoundException e) { }
        return null;
    }

    public static AdminPermissions getAdminPermissionsByUserId(DatastoreService datastore, Transaction txn, String userId) {
        return getAdminPermissionsByKey(datastore, txn, KeyFactory.createKey(AdminPermissions.DatastoreProperties.KIND.getName(), userId));
    }

    public static UserPermissions getUserPermissionsByKey(DatastoreService datastore, Transaction txn, Key key) {
        try {
            Entity userPermissionsEntity = datastore.get(txn, key);
            return new UserPermissions(userPermissionsEntity);
        }
        catch(EntityNotFoundException e) { }
        return null;
    }

    public static UserPermissions getUserPermissionsByUserId(DatastoreService datastore, Transaction txn, String userId) {
        return getUserPermissionsByKey(datastore, txn, KeyFactory.createKey(UserPermissions.DatastoreProperties.KIND.getName(), userId));
    }
}
