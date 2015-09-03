package apollo.datastore.utils;

import apollo.datastore.MiscFunctions.HashAlgorithms;
import apollo.datastore.MiscFunctions;
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

    public final static String SEND_MAIL_TASK_QUEUE = "sendMailQueue";
    public final static String SEND_MAIL_TASK_URL = "/tasks/reset-password-request-send-mail";

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
            Date dateNow = new Date();
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Queue queue = QueueFactory.getQueue(SEND_MAIL_TASK_QUEUE);
            Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
            ResetPasswordRequest resetPasswordRequest = ResetPasswordRequestFactory.getByUserId(datastore, txn, userId);
            if(resetPasswordRequest != null) {
                if(dateNow.before(resetPasswordRequest.getDateOfExpiration()))
                    error = Error.ALREADY_EXISTS_REQUEST;
                else
                    ResetPasswordRequestFactory.remove(datastore, txn, resetPasswordRequest);
            }
            if(error == Error.NONE) {
                User user = UserFactory.getByUserId(datastore, txn, userId);
                if(user == null)
                    error = Error.NON_EXISTENT_USER;
                else if(!user.getActivated())
                    error = Error.NOT_ACTIVATED_USER;
                else if(user.getDisabled())
                    error = Error.DISABLED_USER;
                else
                    try {
                        String requestId = MiscFunctions.getEncryptedHash(MiscFunctions.toUTCDateString(dateNow) + userId, HashAlgorithms.MD5);
                        resetPasswordRequest = new ResetPasswordRequest(requestId, user.getKey(), dateNow);
                        ResetPasswordRequestFactory.add(datastore, txn, resetPasswordRequest);
                        queue.add(TaskOptions.Builder.withUrl(SEND_MAIL_TASK_URL).param(HtmlVariable.USER_ID.getName(), user.getUserId()).param(HtmlVariable.REQUEST_ID.getName(), requestId).param(Cookies.LANG.getName(), (String)req.getAttribute(Cookies.LANG.getName())));
                        txn.commit();
                    }
                    catch(ConcurrentModificationException e) {
                        error = Error.ERROR_IN_RESET_PASSWORD_REQUEST;
                    }
            }

            if(error != Error.NONE && txn.isActive())
                txn.rollback();
        }

        if(error == Error.NONE)
            req.getRequestDispatcher("/WEB-INF/utils/reset-password-notice.jsp").forward(req, resp);
        else {
            req.setAttribute(HtmlVariable.ERROR.getName(), error.toString());
            req.setAttribute(HtmlVariable.USER_ID.getName(), userId);
            req.getRequestDispatcher("/WEB-INF/utils/reset-password.jsp").forward(req, resp);
        }
    }
}
