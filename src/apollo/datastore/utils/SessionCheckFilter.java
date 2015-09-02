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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionCheckFilter implements Filter {

    FilterConfig filterConfig = null;
    ArrayList<String> excludeURLs = new ArrayList<String>();

    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {

        this.filterConfig = filterConfig;
        for(Enumeration<?> e = this.filterConfig.getInitParameterNames(); e.hasMoreElements(); )
            this.excludeURLs.add(this.filterConfig.getInitParameter((String)e.nextElement()));
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // exclude certain urls from init param
        String requestUri = request.getRequestURI();
        for(String URL : this.excludeURLs)
            if(requestUri.startsWith(URL)) {
                chain.doFilter(request, response);
                return;
            }

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
                long sessionTimeout = session.getSessionTimeout();
                if(sessionTimeout > 0) {
                    Calendar lastSessionCheck = MiscFunctions.toCalendar(session.getLastSessionCheck());
                    lastSessionCheck.add(Calendar.SECOND, (int)sessionTimeout);
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
            Cookie sessionIdCookie = new Cookie(Cookies.SESSION_ID.getName(), sessionId);
            sessionIdCookie.setMaxAge(Cookies.MAX_AGE);
            sessionIdCookie.setPath(Cookies.SESSION_ID_PATH);
            response.addCookie(sessionIdCookie);

            response.sendRedirect("/auth/");
        }
        else {
            Cookie sessionIdCookie = new Cookie(Cookies.SESSION_ID.getName(), "");
            sessionIdCookie.setMaxAge(0);
            sessionIdCookie.setPath(Cookies.SESSION_ID_PATH);
            response.addCookie(sessionIdCookie);

            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() { }
}
