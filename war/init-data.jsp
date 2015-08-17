<%@ page session="false"%><%@ page contentType="text/plain;charset=UTF-8" language="java" %>
<%@ page import="apollo.datastore.AdminPermissions" %>
<%@ page import="apollo.datastore.MiscFunctions"%>
<%@ page import="apollo.datastore.PermissionsFactory" %>
<%@ page import="apollo.datastore.TimeZone" %>
<%@ page import="apollo.datastore.TimeZoneFactory" %>
<%@ page import="apollo.datastore.User" %>
<%@ page import="apollo.datastore.UserFactory" %>
<%@ page import="apollo.datastore.UserPermissions" %>
<%@ page import="apollo.datastore.utils.admin.*" %>
<%@ page import="apollo.datastore.utils.user.*" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.EntityNotFoundException"%>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.Transaction"%>
<%@ page import="com.google.appengine.api.datastore.TransactionOptions" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileNotFoundException" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.ConcurrentModificationException" %>
<%
DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
Transaction txn = null;

String timeZoneEntriesFile = "WEB-INF/time-zone-entries.txt";
String delimiter = ";";
BufferedReader br = null;
String line = null;

try {
    br = new BufferedReader(new FileReader(timeZoneEntriesFile));
    while((line = br.readLine()) != null) {
        String[] timeZoneEntry = line.split(delimiter);
        if(timeZoneEntry.length >= 2) {
            txn = datastore.beginTransaction();
            if(TimeZoneFactory.getByTimeZoneId(datastore, txn, timeZoneEntry[1]) == null) {
                TimeZone timeZone = new TimeZone(timeZoneEntry[1], Integer.parseInt(timeZoneEntry[0]));
                TimeZoneFactory.add(datastore, txn, timeZone);
                out.print("added - ");
            }
            txn.commit();
            out.print(timeZoneEntry[0] + ";" + timeZoneEntry[1] + "\n");
        }
    }
    out.print("\n\n");
}
catch(FileNotFoundException e) {
    e.printStackTrace();
}
catch(IOException e) {
    e.printStackTrace();
}
catch(ConcurrentModificationException e) {
    out.print("Multiple timezones found!\n\n");
}
finally {
    if(br != null) {
        try {
            br.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    if(txn != null && txn.isActive())
        txn.rollback();
}

String userId = "admin", password = "admin", emailAddress = "kerafill1116@gmail.com";

try {
    txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));

    if(UserFactory.getByUserId(datastore, txn, userId) == null) {
        User user = new User(userId, password, emailAddress, null);
        user.setActivated(true);
        // user.setSessionTimeout(30);
        UserFactory.add(datastore, txn, user);
        out.print("added - ");
    }
    out.print(userId + ";" + password + ";" + emailAddress + "\n");

    if(PermissionsFactory.getAdminPermissionsByUserId(datastore, txn, userId) == null) {
        AdminPermissions adminPermissions = new AdminPermissions(userId);
        adminPermissions.setUsersPermissions(UsersPermissions.ALL_PERMISSIONS.getCode());
        adminPermissions.setSessionsPermissions(SessionsPermissions.ALL_PERMISSIONS.getCode());
        adminPermissions.setSessionLogsPermissions(SessionLogsPermissions.ALL_PERMISSIONS.getCode());
        PermissionsFactory.addAdminPermissions(datastore, txn, adminPermissions);
        out.print("added - ");
    }
    out.print("admin permissions\n");

    if(PermissionsFactory.getUserPermissionsByUserId(datastore, txn, userId) == null) {
        UserPermissions userPermissions = new UserPermissions(userId);
        userPermissions.setUserPermissions(UserPermissions2.ALL_PERMISSIONS.getCode());
        userPermissions.setSessionPermissions(SessionPermissions.ALL_PERMISSIONS.getCode());
        userPermissions.setSessionLogPermissions(SessionLogPermissions.ALL_PERMISSIONS.getCode());
        PermissionsFactory.addUserPermissions(datastore, txn, userPermissions);
        out.print("added - ");
    }
    out.print("user permissions\n");

    txn.commit();
}
catch(ConcurrentModificationException e) {
    out.print("Multiple accounts found!\n\n");
}
finally {
    if(txn != null && txn.isActive())
        txn.rollback();
}
%>