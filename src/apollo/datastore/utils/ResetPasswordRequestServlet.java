package apollo.datastore.utils;

import apollo.datastore.MiscFunctions;
import apollo.datastore.MiscFunctions.HashAlgorithms;
import apollo.datastore.ResetPasswordRequest;
import apollo.datastore.ResetPasswordRequestFactory;
import apollo.datastore.User;
import apollo.datastore.UserFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ResetPasswordRequestServlet extends HttpServlet {

    public final static String SEND_MAIL_TASK_QUEUE = "utilsQueue";
    public final static String SEND_MAIL_TASK_URL = "/utils/reset-password-request-send-mail";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getRequestDispatcher("/WEB-INF/utils/reset-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Error error = Error.NONE;

        String userId = req.getParameter(HtmlVariable.USER_ID.getName());

        if(userId == null || userId.length() == 0)
            error = Error.REQUIRED_USER_ID;
        else {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Queue queue = QueueFactory.getQueue(SEND_MAIL_TASK_QUEUE);
            Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
            User user = UserFactory.getByUserId(datastore, txn, userId);
            if(user == null)
                error = Error.NON_EXISTENT_USER;
            else if(!user.getActivated())
                error = Error.NOT_ACTIVATED_USER;
            else if(user.getDisabled())
                error = Error.DISABLED_USER;
            else
                try {
                    Date dateNow = new Date();
                    String requestId = MiscFunctions.getEncryptedHash(MiscFunctions.toUTCDateString(dateNow) + userId, HashAlgorithms.MD5);
                    ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(requestId, user.getKey(), dateNow);
                    ResetPasswordRequestFactory.add(datastore, txn, resetPasswordRequest);
                    queue.add(TaskOptions.Builder.withUrl(SEND_MAIL_TASK_URL).param(HtmlVariable.REQUEST_ID.getName(), requestId));
                    txn.commit();
                }
                catch(ConcurrentModificationException e) {
                    error = Error.ERROR_IN_RESET_PASSWORD_REQUEST;
                }

            if(error != Error.NONE && txn.isActive())
                txn.rollback();
        }

        if(error == Error.NONE)
            req.getRequestDispatcher("/WEB-INF/utils/reset-password-notice.jsp").forward(req, resp);
        else {
            StringBuilder urlParams = new StringBuilder("/WEB-INF/utils/reset-password.jsp?");
            urlParams.append(HtmlVariable.ERROR.getName());
            urlParams.append("=");
            urlParams.append(error.toString());
            if(userId != null) {
                urlParams.append("&");
                urlParams.append(HtmlVariable.USER_ID.getName());
                urlParams.append("=");
                urlParams.append(userId);
            }
            req.getRequestDispatcher(resp.encodeRedirectURL(urlParams.toString())).forward(req, resp);
        }
    }
}
