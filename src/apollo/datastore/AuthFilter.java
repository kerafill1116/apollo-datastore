package apollo.datastore;

import apollo.datastore.utils.Error;
import apollo.datastore.utils.HtmlVariable;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.ConcurrentModificationException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthFilter implements Filter {

    FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {

        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        Error error = Error.NONE;
        Session session = null;
        CauseOfDisconnect causeOfDisconnect = CauseOfDisconnect.NONE;
        boolean fromSessionLog = false;

        String sessionId = null;
        Cookie cookies[] = request.getCookies();
        if(cookies != null) for(Cookie cookie : cookies)
            if(cookie.getName().equalsIgnoreCase(Cookies.SESSION_ID.getName())) {
                sessionId = cookie.getValue();
                break;
            }

        if(sessionId == null || sessionId.length() == 0)
            error = Error.NON_EXISTENT_SESSION;
        else {
            // requires withXG for disconnectSession
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
            session = SessionFactory.getBySessionId(datastore, txn, sessionId);
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
                    if(causeOfDisconnect == CauseOfDisconnect.NONE)
                        SessionFactory.updateLastSessionCheck(datastore, txn, session, dateNow.getTime());
                    else
                        SessionFactory.disconnect(datastore, txn, session, dateNow.getTime(), causeOfDisconnect);
                    txn.commit();
                }
                catch(ConcurrentModificationException e) {
                    error = Error.ERROR_IN_SESSION_CHECK;
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

            if((error != Error.NONE || fromSessionLog) && txn.isActive())
                txn.rollback();
        }

        if(error == Error.NONE && causeOfDisconnect == CauseOfDisconnect.NONE && !fromSessionLog) {
            Key userKey = session.getUserKey();
            request.setAttribute(AuthRequestAttribute.USER_KEY.getName(), KeyFactory.keyToString(userKey));

            Cookie sessionIdCookie = new Cookie(Cookies.SESSION_ID.getName(), sessionId);
            sessionIdCookie.setMaxAge(Cookies.MAX_AGE);
            sessionIdCookie.setPath(Cookies.SESSION_ID_PATH);
            response.addCookie(sessionIdCookie);

            chain.doFilter(request, response);
        }
        else {
            Cookie sessionIdCookie = new Cookie(Cookies.SESSION_ID.getName(), "");
            sessionIdCookie.setMaxAge(0);
            sessionIdCookie.setPath(Cookies.SESSION_ID_PATH);
            response.addCookie(sessionIdCookie);

            StringBuilder urlParams = new StringBuilder("/utils/sign-in.jsp?");
            urlParams.append(HtmlVariable.ERROR.getName());
            urlParams.append("=");
            urlParams.append(error.toString());
            urlParams.append("&");
            urlParams.append(HtmlVariable.CAUSE_OF_DISCONNECT.getName());
            urlParams.append("=");
            urlParams.append(causeOfDisconnect.toString());

            response.sendRedirect(response.encodeRedirectURL(urlParams.toString()));
        }
    }

    @Override
    public void destroy() { }
}
