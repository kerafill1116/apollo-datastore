package apollo.datastore.utils;

import apollo.datastore.CauseOfDisconnect;
import apollo.datastore.Cookies;
import apollo.datastore.MiscFunctions;
import apollo.datastore.Session;
import apollo.datastore.SessionFactory;
import apollo.datastore.SessionLog;
import apollo.datastore.SessionLogFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.ConcurrentModificationException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SignOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Error error = Error.NONE;
        CauseOfDisconnect causeOfDisconnect = CauseOfDisconnect.NONE;
        boolean fromSessionLog = false;

        String sessionId = null;
        Cookie cookies[] = req.getCookies();
        if(cookies != null) for(Cookie cookie : cookies)
            if(cookie.getName().equalsIgnoreCase(Cookies.SESSION_ID.getName())) {
                sessionId = cookie.getValue();
                break;
            }

        if(sessionId == null || sessionId.length() == 0)
            error = Error.NON_EXISTENT_SESSION;
        else {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
            Session session = SessionFactory.getBySessionId(datastore, txn, sessionId);
            if(session != null) {
                Calendar dateNow = Calendar.getInstance(java.util.TimeZone.getTimeZone(MiscFunctions.UTC_STRING));
                int sessionTimeout = session.getSessionTimeout();
                if(sessionTimeout > 0) {
                    Calendar lastSessionCheck = MiscFunctions.toCalendar(session.getLastSessionCheck());
                    lastSessionCheck.add(Calendar.SECOND, sessionTimeout);
                    if(lastSessionCheck.compareTo(dateNow) < 0)
                        causeOfDisconnect = CauseOfDisconnect.TIMED_OUT_SESSION;
                }
                try {
                    SessionFactory.disconnect(datastore, txn, session, dateNow.getTime(), causeOfDisconnect);
                    txn.commit();
                }
                catch(ConcurrentModificationException e) {
                    error = Error.ERROR_IN_SIGN_OUT;
                }
            }
            else {
                SessionLog sessionLog = SessionLogFactory.getBySessionId(datastore, txn, sessionId);
                if(sessionLog != null) {
                    causeOfDisconnect = sessionLog.getCauseOfDisconnect();
                    fromSessionLog = true;
                }
                else
                    error = Error.NON_EXISTENT_SESSION;
            }

            // delete session id cookie
            Cookie sessionIdCookie = new Cookie(Cookies.SESSION_ID.getName(), "");
            sessionIdCookie.setMaxAge(0);
            sessionIdCookie.setPath(Cookies.SESSION_ID_PATH);
            resp.addCookie(sessionIdCookie);

            if((error != Error.NONE || fromSessionLog) && txn.isActive())
                txn.rollback();
        }

        StringBuilder urlParams = new StringBuilder("/utils/sign-in?");
        urlParams.append(HtmlVariable.ERROR.getName());
        urlParams.append("=");
        urlParams.append(error.toString());
        urlParams.append("&");
        urlParams.append(HtmlVariable.CAUSE_OF_DISCONNECT.getName());
        urlParams.append("=");
        urlParams.append(causeOfDisconnect.toString());

        resp.sendRedirect(resp.encodeRedirectURL(urlParams.toString()));
    }
}
