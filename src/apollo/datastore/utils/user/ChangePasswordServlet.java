package apollo.datastore.utils.user;

import apollo.datastore.AuthRequestAttribute;
import apollo.datastore.MiscFunctions;
import apollo.datastore.User;
import apollo.datastore.UserBean;
import apollo.datastore.UserFactory;
import apollo.datastore.UserPermissionsBean;
import apollo.datastore.MiscFunctions.HashAlgorithms;
import apollo.datastore.utils.Error;
import apollo.datastore.utils.HtmlVariable;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;

import java.io.IOException;
import java.util.ConcurrentModificationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getChangePassword())
            req.getRequestDispatcher("/WEB-INF/auth/change-password.jsp").forward(req, resp);
        else
            resp.sendRedirect("/auth/settings");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getChangePassword()) {
            Error error = Error.NONE;
            UserBean userBean = (UserBean)req.getAttribute(AuthRequestAttribute.USER.getName());
            String currentPassword = req.getParameter(HtmlVariable.CURRENT_PASSWORD.getName());
            String newPassword = req.getParameter(HtmlVariable.NEW_PASSWORD.getName());

            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Transaction txn = datastore.beginTransaction();
            User user = UserFactory.getByKey(datastore, txn, userBean.getKey());

            if(user.getPassword().compareTo(MiscFunctions.getEncryptedHash(currentPassword, HashAlgorithms.SHA_256)) != 0)
                error = Error.INCORRECT_PASSWORD;
            else
                try {
                    user.setPassword(newPassword);
                    UserFactory.update(datastore, txn, user);
                    txn.commit();
                }
                catch(ConcurrentModificationException e) {
                    error = Error.ERROR_IN_CHANGE_PASSWORD;
                }

            if(error != Error.NONE && txn.isActive())
                txn.rollback();

            req.setAttribute(HtmlVariable.ERROR.getName(), error.toString());
            req.getRequestDispatcher("/WEB-INF/auth/change-password.jsp").forward(req, resp);
        }
        else
            resp.sendRedirect("/auth/settings");
    }
}
