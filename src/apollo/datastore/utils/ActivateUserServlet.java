package apollo.datastore.utils;

import apollo.datastore.User;
import apollo.datastore.UserFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import java.io.IOException;
import java.util.ConcurrentModificationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ActivateUserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Error error = Error.NONE;

        String activationKey = req.getParameter(HtmlVariable.ACTIVATION_KEY.getName());
        Key userKey = null;

        if(activationKey == null || activationKey.length() == 0)
            error = Error.REQUIRED_ACTIVATION_KEY;
        else try {
            userKey = KeyFactory.stringToKey(activationKey);
        }
        catch(IllegalArgumentException e) {
            error = Error.INVALID_ACTIVATION_KEY;
        }

        if(error == Error.NONE) {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Transaction txn = datastore.beginTransaction();
            User user = UserFactory.getByKey(datastore, txn, userKey);
            if(user == null)
                error = Error.NON_EXISTENT_USER;
            else if(user.getActivated())
                error = Error.ALREADY_ACTIVATED_USER;
            else
                try {
                    UserFactory.activate(datastore, txn, user);
                    txn.commit();
                }
                catch(ConcurrentModificationException e) {
                    error = Error.ERROR_IN_ACTIVATION;
                }

            if(error != Error.NONE && txn.isActive())
                txn.rollback();
        }

        resp.sendRedirect("/utils/activate-user-report.jsp?" + HtmlVariable.ERROR.getName() + "=" + error.toString());
    }
}
