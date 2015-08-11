package apollo.datastore.utils;

import apollo.datastore.CauseOfDisconnect;
import apollo.datastore.Cookies;
import apollo.datastore.MiscFunctions;
import apollo.datastore.MiscFunctions.HashAlgorithms;
import apollo.datastore.Session;
import apollo.datastore.SessionFactory;
import apollo.datastore.User;
import apollo.datastore.UserFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SignInServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Error error = Error.NONE;

        String userId = req.getParameter(HtmlVariable.USER_ID.getName());

        if(userId == null || userId.length() == 0)
            error = Error.REQUIRED_USER_ID;
        else {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            ArrayList<Session> sessions = SessionFactory.getSessionsByUserId(datastore, userId);

            Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
            User user = UserFactory.getByUserId(datastore, txn, userId);

            if(user == null)
                error = Error.NON_EXISTENT_USER;
            else
                try {
                    String password = req.getParameter(HtmlVariable.PASSWORD.getName());
                    if(user.getPassword().compareTo(MiscFunctions.getEncryptedHash(password, HashAlgorithms.SHA_256)) != 0) {
                        error = Error.INCORRECT_PASSWORD;
                        if(UserFactory.updateFailedAttempts(datastore, txn, user))
                            error = Error.MAXED_FAILED_ATTEMPTS;
                        txn.commit();
                    }
                    else if(!user.getActivated())
                        error = Error.NOT_ACTIVATED_USER;
                    else if(user.getDisabled())
                        error = Error.DISABLED_USER;
                    else {
                        Date dateNow = new Date();
                        int sessionsCount = sessions.size();
                        int maxSessions = user.getMaxSessions();
                        if(maxSessions == 1) {
                            if(sessionsCount >= 1) {
                                if(user.getExclusiveSession())
                                    error = Error.EXCLUSIVE_SESSION;
                                else // disconnect other sessions
                                    SessionFactory.disconnect(datastore, txn, sessions, dateNow, CauseOfDisconnect.EXCLUSIVE_SESSION);
                            }
                        }
                        else if(maxSessions > 1 && sessionsCount == maxSessions)
                            error = Error.REACHED_MAX_SESSIONS;

                        if(error == Error.NONE) {
                            if(user.getUseTimeSlots()) {
                                // TBD
                            }

                            if(error == Error.NONE) {
                                String sessionId = MiscFunctions.getEncryptedHash(MiscFunctions.toUTCDateString(dateNow) + user.getKeyString(), HashAlgorithms.SHA_256);
                                Session session = new Session(sessionId, user, dateNow);
                                SessionFactory.add(datastore, txn, session);
                                UserFactory.resetFailedAttempts(datastore, txn, user);
                                txn.commit();

                                // set cookies
                                Cookie sessionIdCookie = new Cookie(Cookies.SESSION_ID.getName(), sessionId);
                                sessionIdCookie.setMaxAge(Cookies.MAX_AGE);
                                sessionIdCookie.setPath(Cookies.SESSION_ID_PATH);
                                resp.addCookie(sessionIdCookie);

                                if(req.getParameter(HtmlVariable.REMEMBER_ME.getName()) != null) {
                                    Cookie rememberMeCookie = new Cookie(Cookies.REMEMBER_ME.getName(), "1");
                                    rememberMeCookie.setMaxAge(Cookies.MAX_AGE);
                                    rememberMeCookie.setPath(Cookies.UTILS_PATH);
                                    resp.addCookie(rememberMeCookie);
                                    Cookie userIdCookie = new Cookie(Cookies.USER_ID.getName(), userId);
                                    userIdCookie.setMaxAge(Cookies.MAX_AGE);
                                    userIdCookie.setPath(Cookies.UTILS_PATH);
                                    resp.addCookie(userIdCookie);
                                }
                                else { // delete cookies
                                    Cookie rememberMeCookie = new Cookie(Cookies.REMEMBER_ME.getName(), "");
                                    rememberMeCookie.setMaxAge(0);
                                    rememberMeCookie.setPath(Cookies.UTILS_PATH);
                                    resp.addCookie(rememberMeCookie);
                                    Cookie userIdCookie = new Cookie(Cookies.USER_ID.getName(), "");
                                    userIdCookie.setMaxAge(0);
                                    userIdCookie.setPath(Cookies.UTILS_PATH);
                                    resp.addCookie(userIdCookie);
                                }

                                resp.sendRedirect("/auth/index.jsp");
                            }
                        }
                    }
                }
                catch(ConcurrentModificationException e) {
                    error = Error.ERROR_IN_SIGN_IN;
                }

            if(error != Error.NONE && txn.isActive())
                txn.rollback();
        }

        if(error != Error.NONE) {
            StringBuilder urlParams = new StringBuilder("/utils/sign-in.jsp?");
            urlParams.append(HtmlVariable.ERROR.getName());
            urlParams.append("=");
            urlParams.append(error.toString());
            if(userId != null) {
                urlParams.append("&");
                urlParams.append(HtmlVariable.USER_ID.getName());
                urlParams.append("=");
                urlParams.append(userId);
            }
            resp.sendRedirect(resp.encodeRedirectURL(urlParams.toString()));
        }
    }
}
