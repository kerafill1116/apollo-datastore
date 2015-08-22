package apollo.datastore;

import apollo.datastore.utils.user.*;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserPermissions implements Serializable {

    private Entity entity;

    public UserPermissions(String userId, long userPermissions, long sessionPermissions, long sessionLogPermissions) {
        this.entity = new Entity(DatastoreProperties.KIND.getName(), userId);

        this.entity.setProperty(DatastoreProperties.USER_PERMISSIONS.getName(), userPermissions);
        this.entity.setProperty(DatastoreProperties.SESSION_PERMISSIONS.getName(), sessionPermissions);
        this.entity.setProperty(DatastoreProperties.SESSION_LOG_PERMISSIONS.getName(), sessionLogPermissions);
    }

    public UserPermissions(User user, long userPermissions, long sessionPermissions, long sessionLogPermissions) {
        this(user.getUserId(), userPermissions, sessionPermissions, sessionLogPermissions);
    }

    public UserPermissions(String userId) {
        this.entity = new Entity(DatastoreProperties.KIND.getName(), userId);

        this.entity.setProperty(DatastoreProperties.USER_PERMISSIONS.getName(), UserPermissions2.DEFAULT_PERMISSIONS.getCode());
        this.entity.setProperty(DatastoreProperties.SESSION_PERMISSIONS.getName(), SessionPermissions.DEFAULT_PERMISSIONS.getCode());
        this.entity.setProperty(DatastoreProperties.SESSION_LOG_PERMISSIONS.getName(), SessionLogPermissions.DEFAULT_PERMISSIONS.getCode());
    }

    public UserPermissions(User user) {
        this(user.getUserId());
    }

    public UserPermissions(Entity userPermissionsEntity) {
        this.entity = userPermissionsEntity;
    }

    public long getUserPermissions() {
        return (long)this.entity.getProperty(DatastoreProperties.USER_PERMISSIONS.getName());
    }

    public void setUserPermissions(long userPermissions) {
        this.entity.setProperty(DatastoreProperties.USER_PERMISSIONS.getName(), userPermissions);
    }

    public long getSessionPermissions() {
        return (long)this.entity.getProperty(DatastoreProperties.SESSION_PERMISSIONS.getName());
    }

    public void setSessionPermissions(long sessionPermissions) {
        this.entity.setProperty(DatastoreProperties.SESSION_PERMISSIONS.getName(), sessionPermissions);
    }

    public long getSessionLogPermissions() {
        return (long)this.entity.getProperty(DatastoreProperties.SESSION_LOG_PERMISSIONS.getName());
    }

    public void setSessionLogPermissions(long sessionLogPermissions) {
        this.entity.setProperty(DatastoreProperties.SESSION_LOG_PERMISSIONS.getName(), sessionLogPermissions);
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
        KIND("UserPermission"),
        USER_PERMISSIONS("userPermissions"),
        SESSION_PERMISSIONS("sessionPermissions"),
        SESSION_LOG_PERMISSIONS("sessionLogPermissions");

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
