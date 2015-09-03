package apollo.datastore.utils.user;

import apollo.datastore.TimeZone;
import apollo.datastore.TimeZoneFactory;
import apollo.datastore.User;
import apollo.datastore.UserBean;
import apollo.datastore.UserFactory;
import apollo.datastore.UserPermissionsBean;
import apollo.datastore.utils.AuthRequestAttribute;
import apollo.datastore.utils.Error;
import apollo.datastore.utils.HtmlVariable;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

import java.io.IOException;
import java.util.ConcurrentModificationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SettingsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Error error = Error.NONE;
        boolean updateSetting = false;
        UserBean userBean = (UserBean)req.getAttribute(AuthRequestAttribute.USER.getName());
        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
        User user = UserFactory.getByKey(datastore, txn, userBean.getKey());

        String paramValue = null;
        if((paramValue = req.getParameter(HtmlVariable.TIME_ZONE_ID.getName())) != null) {
            updateSetting = true;
            if(userPermissionsBean.getViewTimeZone() && userPermissionsBean.getChangeTimeZone()) {
                Key timeZoneKey = null;
                if(paramValue.length() != 0) {
                    TimeZone timeZone = TimeZoneFactory.getByTimeZoneId(datastore, txn, paramValue);
                    if(timeZone != null)
                        timeZoneKey = timeZone.getKey();
                }
                user.setTimeZoneKey(timeZoneKey);
            }
            else
                error = Error.NO_PERMISSIONS;
        }
        else if((paramValue = req.getParameter(HtmlVariable.SESSION_TIMEOUT.getName())) != null) {
            updateSetting = true;
            if(userPermissionsBean.getViewSessionTimeout() && userPermissionsBean.getChangeSessionTimeout()) {
                long sessionTimeout = 0;
                try {
                    sessionTimeout = Long.parseLong(paramValue);
                    /* 0 or negative - infinite session timeout
                    if(sessionTimeout < 0)
                        error = Error.INVALID_SESSION_TIMEOUT;
                    */
                }
                catch(NumberFormatException e) {
                    error = Error.INVALID_SESSION_TIMEOUT;
                }
                if(error == Error.NONE)
                    user.setSessionTimeout(sessionTimeout);
            }
            else
                error = Error.NO_PERMISSIONS;
        }
        else if((paramValue = req.getParameter(HtmlVariable.MAX_SESSIONS.getName())) != null) {
            updateSetting = true;
            if(userPermissionsBean.getViewMaxSessions() && userPermissionsBean.getChangeMaxSessions()) {
                long maxSessions = 0;
                try {
                    maxSessions = Long.parseLong(paramValue);
                    /* 0 or negative - infinite sessions
                    if(maxSessions < 0)
                        error = Error.INVALID_MAX_SESSIONS;
                    */
                }
                catch(NumberFormatException e) {
                    error = Error.INVALID_MAX_SESSIONS;
                }
                if(error == Error.NONE)
                    user.setMaxSessions(maxSessions);
            }
            else
                error = Error.NO_PERMISSIONS;
        }
        else if((paramValue = req.getParameter(HtmlVariable.EXCLUSIVE_SESSION.getName())) != null) {
            updateSetting = true;
            if(userPermissionsBean.getViewExclusiveSession() && userPermissionsBean.getChangeExclusiveSession()) {
                boolean exclusiveSession = true;
                try {
                    if(Integer.parseInt(paramValue) == 0)
                        exclusiveSession = false;
                }
                catch(NumberFormatException e) {
                    error = Error.INVALID_EXCLUSIVE_SESSION;
                }
                if(error == Error.NONE)
                    user.setExclusiveSession(exclusiveSession);
            }
            else
                error = Error.NO_PERMISSIONS;
        }
        else if((paramValue = req.getParameter(HtmlVariable.MAX_FAILED_ATTEMPTS.getName())) != null) {
            updateSetting = true;
            if(userPermissionsBean.getViewMaxFailedAttempts() && userPermissionsBean.getChangeMaxFailedAttempts()) {
                long maxFailedAttempts = 0;
                try {
                    maxFailedAttempts = Long.parseLong(paramValue);
                    /* negative - infinite failed attempts
                    if(maxFailedAttempts < -1)
                        error = Error.INVALID_MAX_FAILED_ATTEMPTS;
                    */
                }
                catch(NumberFormatException e) {
                    error = Error.INVALID_MAX_FAILED_ATTEMPTS;
                }
                if(error == Error.NONE)
                    user.setMaxFailedAttempts(maxFailedAttempts);
            }
            else
                error = Error.NO_PERMISSIONS;
        }

        if(updateSetting) {
            if(error == Error.NONE)
                try {
                    UserFactory.update(datastore, txn, user);
                    txn.commit();
                }
                catch(ConcurrentModificationException e) {
                    error = Error.ERROR_IN_SETTINGS;
                }
            else if(txn.isActive())
                txn.rollback();
            String jsonString = "{ \"" + HtmlVariable.ERROR.getName() + "\": " + error.toString() + " }";
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().print(jsonString);
        }
        else {
            if(txn.isActive())
                txn.rollback();
            req.getRequestDispatcher("/WEB-INF/auth/settings.jsp").forward(req, resp);
        }
    }
}
