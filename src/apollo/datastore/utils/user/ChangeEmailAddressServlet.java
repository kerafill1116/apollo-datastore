package apollo.datastore.utils.user;

import apollo.datastore.AuthRequestAttribute;
import apollo.datastore.ChangeEmailAddressRequest;
import apollo.datastore.ChangeEmailAddressRequestBean;
import apollo.datastore.ChangeEmailAddressRequestFactory;
import apollo.datastore.Cookies;
import apollo.datastore.MiscFunctions;
import apollo.datastore.User;
import apollo.datastore.UserBean;
import apollo.datastore.UserFactory;
import apollo.datastore.UserPermissionsBean;
import apollo.datastore.MiscFunctions.HashAlgorithms;
import apollo.datastore.utils.Error;
import apollo.datastore.utils.HtmlVariable;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Date;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@SuppressWarnings("serial")
public class ChangeEmailAddressServlet extends HttpServlet {

    public final static String SEND_MAIL_TASK_QUEUE = "sendMailQueue";
    public final static String SEND_MAIL_TASK_URL = "/tasks/change-email-address-send-mail";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewEmailAddress() && userPermissionsBean.getChangeEmailAddress()) {
            Error error = Error.NONE;
            UserBean userBean = (UserBean)req.getAttribute(AuthRequestAttribute.USER.getName());
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            if(req.getParameter(HtmlVariable.CANCEL.getName()) != null) {
                Transaction txn = datastore.beginTransaction();
                ChangeEmailAddressRequest changeEmailAddressRequest = ChangeEmailAddressRequestFactory.getByUserId(datastore, txn, userBean.getUserId());
                if(changeEmailAddressRequest == null)
                    error = Error.NON_EXISTENT_REQUEST;
                else
                    try {
                        ChangeEmailAddressRequestFactory.remove(datastore, txn, changeEmailAddressRequest);
                        txn.commit();
                    }
                    catch(ConcurrentModificationException e) {
                        error = Error.ERROR_IN_CHANGE_EMAIL_ADDRESS;
                    }

                if(error != Error.NONE && txn.isActive())
                    txn.rollback();

                req.setAttribute(HtmlVariable.ERROR.getName(), error.toString());
                req.setAttribute(HtmlVariable.CANCEL.getName(), true);
            }
            else if(req.getParameter(HtmlVariable.RESEND.getName()) != null) {
                ChangeEmailAddressRequest changeEmailAddressRequest = ChangeEmailAddressRequestFactory.getByUserId(datastore, null, userBean.getUserId());
                if(changeEmailAddressRequest == null)
                    error = Error.NON_EXISTENT_REQUEST;
                else {
                    Queue queue = QueueFactory.getQueue(SEND_MAIL_TASK_QUEUE);
                    queue.add(TaskOptions.Builder.withUrl(SEND_MAIL_TASK_URL).param(HtmlVariable.USER_ID.getName(), userBean.getUserId()).param(HtmlVariable.REQUEST_ID.getName(), changeEmailAddressRequest.getRequestId()).param(Cookies.LANG.getName(), (String)req.getAttribute(Cookies.LANG.getName())));
                    req.setAttribute(AuthRequestAttribute.CHANGE_EMAIL_ADDRESS_REQUEST.getName(), new ChangeEmailAddressRequestBean(changeEmailAddressRequest));
                }
                req.setAttribute(HtmlVariable.ERROR.getName(), error.toString());
                req.setAttribute(HtmlVariable.RESEND.getName(), true);
            }
            else {
                ChangeEmailAddressRequest changeEmailAddressRequest = ChangeEmailAddressRequestFactory.getByUserId(datastore, null, userBean.getUserId());
                if(changeEmailAddressRequest != null)
                    req.setAttribute(AuthRequestAttribute.CHANGE_EMAIL_ADDRESS_REQUEST.getName(), new ChangeEmailAddressRequestBean(changeEmailAddressRequest));
            }

            req.getRequestDispatcher("/WEB-INF/auth/change-email-address.jsp").forward(req, resp);
        }
        else
            resp.sendRedirect("/auth/settings");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewEmailAddress() && userPermissionsBean.getChangeEmailAddress()) {
            Error error = Error.NONE;
            UserBean userBean = (UserBean)req.getAttribute(AuthRequestAttribute.USER.getName());
            String currentPassword = req.getParameter(HtmlVariable.CURRENT_PASSWORD.getName());
            String newEmailAddress = req.getParameter(HtmlVariable.NEW_EMAIL_ADDRESS.getName());

            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Queue queue = QueueFactory.getQueue(SEND_MAIL_TASK_QUEUE);
            Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
            User user = UserFactory.getByKey(datastore, txn, userBean.getKey());
            if(user == null)
                error = Error.NON_EXISTENT_USER;
            else if(user.getPassword().compareTo(MiscFunctions.getEncryptedHash(currentPassword, HashAlgorithms.SHA_256)) != 0)
                error = Error.INCORRECT_PASSWORD;
            else if(newEmailAddress == null || newEmailAddress.length() == 0)
                error = Error.REQUIRED_EMAIL_ADDRESS;
            else try {
                new InternetAddress(newEmailAddress);
            }
            catch(AddressException e) {
                error = Error.INVALID_EMAIL_ADDRESS;
            }

            if(error == Error.NONE) {
                ChangeEmailAddressRequest changeEmailAddressRequest = ChangeEmailAddressRequestFactory.getByUserId(datastore, txn, user.getUserId());
                if(changeEmailAddressRequest != null)
                    error = Error.ALREADY_EXISTS_REQUEST;
                else
                    try {
                        Date dateNow = new Date();
                        String requestId = MiscFunctions.getEncryptedHash(MiscFunctions.toUTCDateString(dateNow) + user.getUserId(), HashAlgorithms.MD5);
                        changeEmailAddressRequest = new ChangeEmailAddressRequest(requestId, user.getKey(), newEmailAddress, dateNow);
                        ChangeEmailAddressRequestFactory.add(datastore, txn, changeEmailAddressRequest);
                        queue.add(TaskOptions.Builder.withUrl(SEND_MAIL_TASK_URL).param(HtmlVariable.USER_ID.getName(), user.getUserId()).param(HtmlVariable.REQUEST_ID.getName(), requestId).param(Cookies.LANG.getName(), (String)req.getAttribute(Cookies.LANG.getName())));
                        txn.commit();
                    }
                    catch(ConcurrentModificationException e) {
                        error = Error.ERROR_IN_CHANGE_EMAIL_ADDRESS;
                    }

                // add request bean to auth attributes for jsp display
                req.setAttribute(AuthRequestAttribute.CHANGE_EMAIL_ADDRESS_REQUEST.getName(), new ChangeEmailAddressRequestBean(changeEmailAddressRequest));
            }

            if(error != Error.NONE && txn.isActive())
                txn.rollback();

            req.setAttribute(HtmlVariable.ERROR.getName(), error.toString());
            req.setAttribute(HtmlVariable.NEW_EMAIL_ADDRESS.getName(), newEmailAddress);
            req.getRequestDispatcher("/WEB-INF/auth/change-email-address.jsp").forward(req, resp);
        }
        else
            resp.sendRedirect("/auth/settings");
    }
}
