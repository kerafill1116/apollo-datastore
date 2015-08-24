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
public class SessionPermissionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewSessions() && userPermissionsBean.getViewSessionPermissions())
            req.getRequestDispatcher("/WEB-INF/auth/session-permissions.jsp").forward(req, resp);
        else
            resp.sendRedirect("/auth/settings");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewSessions() && userPermissionsBean.getViewSessionPermissions()) {

            if(userPermissionsBean.getChangeSessionPermissions()) {
                long sessionPermissionsCode = 0;
                if(req.getParameter(HtmlVariable.VIEW_SESSIONS.getName()) != null)
                    sessionPermissionsCode = sessionPermissionsCode + SessionPermissions.VIEW_SESSIONS.getCode();
                if(req.getParameter(HtmlVariable.DISCONNECT_SESSIONS.getName()) != null)
                    sessionPermissionsCode = sessionPermissionsCode + SessionPermissions.DISCONNECT_SESSIONS.getCode();
                if(req.getParameter(HtmlVariable.VIEW_SESSION_PERMISSIONS.getName()) != null)
                    sessionPermissionsCode = sessionPermissionsCode + SessionPermissions.VIEW_SESSION_PERMISSIONS.getCode();
                if(req.getParameter(HtmlVariable.CHANGE_SESSION_PERMISSIONS.getName()) != null)
                    sessionPermissionsCode = sessionPermissionsCode + SessionPermissions.CHANGE_SESSION_PERMISSIONS.getCode();

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
                        userPermissions.setSessionPermissions(sessionPermissionsCode);
                        PermissionsFactory.updateUserPermissions(datastore, txn, userPermissions);
                        txn.commit();
                        userPermissionsBean = new UserPermissionsBean(userPermissions);
                        req.setAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName(), userPermissionsBean);
                    }
                    catch(ConcurrentModificationException e) {
                        error = Error.ERROR_IN_SESSION_PERMISSIONS;
                    }

                if(error != Error.NONE && txn.isActive())
                    txn.rollback();

                if(userPermissionsBean.getViewSessions() && userPermissionsBean.getViewSessionPermissions()) {
                    req.setAttribute(HtmlVariable.ERROR.getName(), error.toString());
                    req.getRequestDispatcher("/WEB-INF/auth/session-permissions.jsp").forward(req, resp);
                }
                else
                    resp.sendRedirect("/auth/settings");
            }
            else
                req.getRequestDispatcher("/WEB-INF/auth/session-permissions.jsp").forward(req, resp);
        }
        else
            resp.sendRedirect("/auth/settings");
    }
}
