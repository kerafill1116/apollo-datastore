package apollo.datastore.utils;

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
public class ResetPasswordServlet extends HttpServlet {

    public final static String SEND_MAIL_TASK_QUEUE = "utilsQueue";
    public final static String SEND_MAIL_TASK_URL = "/utils/reset-password-send-mail";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Error error = Error.NONE;

        String requestId = req.getParameter(HtmlVariable.RESET_PASSWORD_REQUEST_ID.getName());

        if(requestId == null || requestId.length() == 0)
            error = Error.REQUIRED_REQUEST_ID;
        else {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Queue queue = QueueFactory.getQueue(SEND_MAIL_TASK_QUEUE);
            Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
            ResetPasswordRequest resetPasswordRequest = ResetPasswordRequestFactory.getByRequestId(datastore, txn, requestId);
            if(resetPasswordRequest == null)
                error = Error.NON_EXISTENT_REQUEST;
            else {
                Date dateNow = new Date();
                Date dateOfExpiration = resetPasswordRequest.getDateOfExpiration();
                if(!dateNow.before(dateOfExpiration))
                    error = Error.EXPIRED_REQUEST;
                else if(resetPasswordRequest.getApproved())
                    error = Error.ALREADY_APPROVED_REQUEST;
                else {
                    User user = UserFactory.getByKey(datastore, txn, resetPasswordRequest.getUserKey());
                    if(user == null)
                        error = Error.NON_EXISTENT_USER;
                    else if(!user.getActivated())
                        error = Error.NOT_ACTIVATED_USER;
                    else if(user.getDisabled())
                        error = Error.DISABLED_USER;
                    else
                        try {
                            String newPassword = UserFactory.resetPassword(datastore, txn, user);
                            ResetPasswordRequestFactory.approve(datastore, txn, resetPasswordRequest, dateNow);
                            queue.add(TaskOptions.Builder.withUrl(SEND_MAIL_TASK_URL).param(HtmlVariable.USER_ID.getName(), user.getUserId()).param(HtmlVariable.PASSWORD.getName(), newPassword));
                            txn.commit();
                        }
                        catch(ConcurrentModificationException e) {
                            error = Error.ERROR_IN_RESET_PASSWORD;
                        }
                }
            }

            if(error != Error.NONE && txn.isActive())
                txn.rollback();
        }

        req.getRequestDispatcher("/WEB-INF/utils/reset-password-report.jsp?" + HtmlVariable.ERROR.getName() + "=" + error.toString()).forward(req, resp);
    }
}
