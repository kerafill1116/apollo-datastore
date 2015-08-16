package apollo.datastore.utils;

import apollo.datastore.AdminPermissions;
import apollo.datastore.PermissionsFactory;
import apollo.datastore.TimeZone;
import apollo.datastore.User;
import apollo.datastore.UserFactory;
import apollo.datastore.UserPermissions;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RegisterUserServlet extends HttpServlet {

    public final static String SEND_MAIL_TASK_QUEUE = "utilsQueue";
    public final static String SEND_MAIL_TASK_URL = "/utils/register-user-send-mail";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/utils/register-user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Error error = Error.NONE;

        String userId = req.getParameter(HtmlVariable.USER_ID.getName());
        String password = req.getParameter(HtmlVariable.PASSWORD.getName());
        String emailAddress = req.getParameter(HtmlVariable.EMAIL_ADDRESS.getName());
        String timeZoneId = req.getParameter(HtmlVariable.TIME_ZONE_ID.getName());

        if(userId == null || userId.length() == 0)
            error = Error.REQUIRED_USER_ID;
        else if(!Pattern.matches("^[a-zA-Z][a-zA-Z0-9_]{4,31}$", userId))
            error = Error.INVALID_USER_ID;
        else if(emailAddress == null || emailAddress.length() == 0)
            error = Error.REQUIRED_EMAIL_ADDRESS;
        else try {
            new InternetAddress(emailAddress);
        }
        catch(AddressException e) {
            error = Error.INVALID_EMAIL_ADDRESS;
        }

        if(error == Error.NONE) {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Queue queue = QueueFactory.getQueue(SEND_MAIL_TASK_QUEUE);
            Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
            User user = UserFactory.getByUserId(datastore, txn, userId);
            if(user != null)
                error = Error.NOT_AVAILABLE_USER_ID;
            else
                try {
                    Key timeZoneKey = (timeZoneId == null || timeZoneId.length() == 0) ? null : KeyFactory.createKey(TimeZone.DatastoreProperties.KIND.getName(), timeZoneId);
                    user = new User(userId, password, emailAddress, timeZoneKey);
                    UserFactory.add(datastore, txn, user);
                    AdminPermissions adminPermissions = new AdminPermissions(userId);
                    PermissionsFactory.addAdminPermissions(datastore, txn, adminPermissions);
                    UserPermissions userPermissions = new UserPermissions(userId);
                    PermissionsFactory.addUserPermissions(datastore, txn, userPermissions);
                    queue.add(TaskOptions.Builder.withUrl(SEND_MAIL_TASK_URL).param(HtmlVariable.USER_ID.getName(), userId).param(HtmlVariable.PASSWORD.getName(), password));
                    txn.commit();
                }
                catch(ConcurrentModificationException e) {
                    error = Error.ERROR_IN_REGISTER_NEW_USER;
                }

            if(error != Error.NONE && txn.isActive())
                txn.rollback();
        }

        if(error == Error.NONE)
            req.getRequestDispatcher("/WEB-INF/utils/register-user-thank-you.jsp").forward(req, resp);
        else {
            StringBuilder urlParams = new StringBuilder("/WEB-INF/utils/register-user.jsp?");
            urlParams.append(HtmlVariable.ERROR.getName());
            urlParams.append("=");
            urlParams.append(error.toString());
            if(userId != null) {
                urlParams.append("&");
                urlParams.append(HtmlVariable.USER_ID.getName());
                urlParams.append("=");
                urlParams.append(userId);
            }
            if(emailAddress != null) {
                urlParams.append("&");
                urlParams.append(HtmlVariable.EMAIL_ADDRESS.getName());
                urlParams.append("=");
                urlParams.append(emailAddress);
            }
            if(timeZoneId != null) {
                urlParams.append("&");
                urlParams.append(HtmlVariable.TIME_ZONE_ID.getName());
                urlParams.append("=");
                urlParams.append(timeZoneId);
            }
            req.getRequestDispatcher(resp.encodeRedirectURL(urlParams.toString())).forward(req, resp);
        }
    }
}
