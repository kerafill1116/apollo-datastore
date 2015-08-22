package apollo.datastore;

import apollo.datastore.utils.admin.*;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AdminPermissions implements Serializable {

    private Entity entity;

    public AdminPermissions(String userId, long usersPermissions, long sessionsPermissions, long sessionLogsPermissions) {
        this.entity = new Entity(DatastoreProperties.KIND.getName(), userId);

        this.entity.setProperty(DatastoreProperties.USERS_PERMISSIONS.getName(), usersPermissions);
        this.entity.setProperty(DatastoreProperties.SESSIONS_PERMISSIONS.getName(), sessionsPermissions);
        this.entity.setProperty(DatastoreProperties.SESSION_LOGS_PERMISSIONS.getName(), sessionLogsPermissions);
    }

    public AdminPermissions(User user, long usersPermissions, long sessionsPermissions, long sessionLogsPermissions) {
        this(user.getUserId(), usersPermissions, sessionsPermissions, sessionLogsPermissions);
    }

    public AdminPermissions(String userId) {
        this.entity = new Entity(DatastoreProperties.KIND.getName(), userId);

        this.entity.setProperty(DatastoreProperties.USERS_PERMISSIONS.getName(), UsersPermissions.DEFAULT_PERMISSIONS.getCode());
        this.entity.setProperty(DatastoreProperties.SESSIONS_PERMISSIONS.getName(), SessionsPermissions.DEFAULT_PERMISSIONS.getCode());
        this.entity.setProperty(DatastoreProperties.SESSION_LOGS_PERMISSIONS.getName(), SessionLogsPermissions.DEFAULT_PERMISSIONS.getCode());
    }

    public AdminPermissions(User user) {
        this(user.getUserId());
    }

    public AdminPermissions(Entity adminPermissionsEntity) {
        this.entity = adminPermissionsEntity;
    }

    public long getUsersPermissions() {
        return (long)this.entity.getProperty(DatastoreProperties.USERS_PERMISSIONS.getName());
    }

    public void setUsersPermissions(long usersPermissions) {
        this.entity.setProperty(DatastoreProperties.USERS_PERMISSIONS.getName(), usersPermissions);
    }

    public long getSessionsPermissions() {
        return (long)this.entity.getProperty(DatastoreProperties.SESSIONS_PERMISSIONS.getName());
    }

    public void setSessionsPermissions(long sessionsPermissions) {
        this.entity.setProperty(DatastoreProperties.SESSIONS_PERMISSIONS.getName(), sessionsPermissions);
    }

    public long getSessionLogsPermissions() {
        return (long)this.entity.getProperty(DatastoreProperties.SESSION_LOGS_PERMISSIONS.getName());
    }

    public void setSessionLogsPermissions(long sessionLogsPermissions) {
        this.entity.setProperty(DatastoreProperties.SESSION_LOGS_PERMISSIONS.getName(), sessionLogsPermissions);
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
        KIND("AdminPermission"),
        USERS_PERMISSIONS("usersPermissions"),
        SESSIONS_PERMISSIONS("sessionsPermissions"),
        SESSION_LOGS_PERMISSIONS("sessionLogsPermissions");

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
