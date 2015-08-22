package apollo.datastore.utils.user;

import apollo.datastore.AuthRequestAttribute;
import apollo.datastore.PermissionsFactory;
import apollo.datastore.User;
import apollo.datastore.UserBean;
import apollo.datastore.UserFactory;
import apollo.datastore.UserPermissions;
import apollo.datastore.UserPermissionsBean;
import apollo.datastore.utils.Error;
import apollo.datastore.utils.HtmlVariable;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

import java.io.IOException;
import java.util.ConcurrentModificationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UserPermissionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewUserPermissions())
            req.getRequestDispatcher("/WEB-INF/auth/user-permissions.jsp").forward(req, resp);
        else
            resp.sendRedirect("/auth/settings");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewUserPermissions() && userPermissionsBean.getChangeUserPermissions()) {

            int userPermissionsCode = 0;
            if(req.getParameter(HtmlVariable.CHANGE_PASSWORD.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.CHANGE_PASSWORD.getCode();
            if(req.getParameter(HtmlVariable.VIEW_EMAIL_ADDRESS.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.VIEW_EMAIL_ADDRESS.getCode();
            if(req.getParameter(HtmlVariable.CHANGE_EMAIL_ADDRESS.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.CHANGE_EMAIL_ADDRESS.getCode();
            if(req.getParameter(HtmlVariable.VIEW_TIME_ZONE.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.VIEW_TIME_ZONE.getCode();
            if(req.getParameter(HtmlVariable.CHANGE_TIME_ZONE.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.CHANGE_TIME_ZONE.getCode();
            if(req.getParameter(HtmlVariable.VIEW_SESSION_TIMEOUT.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.VIEW_SESSION_TIMEOUT.getCode();
            if(req.getParameter(HtmlVariable.CHANGE_SESSION_TIMEOUT.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.CHANGE_SESSION_TIMEOUT.getCode();
            if(req.getParameter(HtmlVariable.VIEW_MAX_SESSIONS.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.VIEW_MAX_SESSIONS.getCode();
            if(req.getParameter(HtmlVariable.CHANGE_MAX_SESSIONS.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.CHANGE_MAX_SESSIONS.getCode();
            if(req.getParameter(HtmlVariable.VIEW_EXCLUSIVE_SESSION.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.VIEW_EXCLUSIVE_SESSION.getCode();
            if(req.getParameter(HtmlVariable.CHANGE_EXCLUSIVE_SESSION.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.CHANGE_EXCLUSIVE_SESSION.getCode();
            if(req.getParameter(HtmlVariable.VIEW_MAX_FAILED_ATTEMPTS.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.VIEW_MAX_FAILED_ATTEMPTS.getCode();
            if(req.getParameter(HtmlVariable.CHANGE_MAX_FAILED_ATTEMPTS.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.CHANGE_MAX_FAILED_ATTEMPTS.getCode();
            if(req.getParameter(HtmlVariable.VIEW_DISABLED_STATUS.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.VIEW_DISABLED_STATUS.getCode();
            if(req.getParameter(HtmlVariable.VIEW_ACTIVATED_STATUS.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.VIEW_ACTIVATED_STATUS.getCode();
            if(req.getParameter(HtmlVariable.VIEW_USER_PERMISSIONS.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.VIEW_USER_PERMISSIONS.getCode();
            if(req.getParameter(HtmlVariable.CHANGE_USER_PERMISSIONS.getName()) != null)
                userPermissionsCode = userPermissionsCode + UserPermissions2.CHANGE_USER_PERMISSIONS.getCode();

            Error error = Error.NONE;
            UserBean userBean = (UserBean)req.getAttribute(AuthRequestAttribute.USER.getName());
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
            User user = UserFactory.getByKey(datastore, txn, userBean.getKey());
            UserPermissions userPermissions = null;
            if(user == null)
                error = Error.NON_EXISTENT_USER;
            else if((userPermissions = PermissionsFactory.getUserPermissionsByUserId(datastore, txn, user.getUserId())) == null)
                error = Error.NO_PERMISSIONS;
            else
                try {
                    userPermissions.setUserPermissions(userPermissionsCode);
                    PermissionsFactory.updateUserPermissions(datastore, txn, userPermissions);
                    txn.commit();
                    req.setAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName(), new UserPermissionsBean(userPermissions));
                }
                catch(ConcurrentModificationException e) {
                    error = Error.ERROR_IN_USER_PERMISSIONS;
                }

            if(error != Error.NONE && txn.isActive())
                txn.rollback();

            req.setAttribute(HtmlVariable.ERROR.getName(), error.toString());
            req.getRequestDispatcher("/WEB-INF/auth/user-permissions.jsp").forward(req, resp);
        }
        else
            resp.sendRedirect("/auth/settings");
    }
}
