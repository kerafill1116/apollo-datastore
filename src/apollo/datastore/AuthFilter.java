package apollo.datastore;

import apollo.datastore.utils.Error;
import apollo.datastore.utils.HtmlVariable;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
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

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        if(sessionId == null || sessionId.length() == 0)
            error = Error.NON_EXISTENT_SESSION;
        else {
            // requires withXG for disconnectSession
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
            User user = UserFactory.getByKey(datastore, null, session.getUserKey());
            if(user == null)
                error = Error.NON_EXISTENT_USER;
            else {
                UserBean userBean = new UserBean(user);
                Key timeZoneKey = userBean.getTimeZoneKey();
                TimeZone timeZone = null;
                if(timeZoneKey != null && (timeZone = TimeZoneFactory.getByKey(datastore, null, timeZoneKey)) != null)
                    userBean.setTimeZoneLocaleId(timeZone.getLocaleId());
                else
                    userBean.setTimeZoneLocaleId(TimeZone.GMT_LOCALE_ID);
                request.setAttribute(AuthRequestAttribute.USER.getName(), userBean);
                AdminPermissions adminPermissions = PermissionsFactory.getAdminPermissionsByUserId(datastore, null, user.getUserId());
                request.setAttribute(AuthRequestAttribute.ADMIN_PERMISSIONS.getName(), (adminPermissions != null) ? new AdminPermissionsBean(adminPermissions) : new AdminPermissionsBean());
                UserPermissions userPermissions = PermissionsFactory.getUserPermissionsByUserId(datastore, null, user.getUserId());
                request.setAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName(), (userPermissions != null) ? new UserPermissionsBean(userPermissions) : new UserPermissionsBean());

                Cookie sessionIdCookie = new Cookie(Cookies.SESSION_ID.getName(), sessionId);
                sessionIdCookie.setMaxAge(Cookies.MAX_AGE);
                sessionIdCookie.setPath(Cookies.SESSION_ID_PATH);
                response.addCookie(sessionIdCookie);

                chain.doFilter(request, response);
            }
        }

        if(error != Error.NONE || causeOfDisconnect != CauseOfDisconnect.NONE || fromSessionLog) {
            Cookie sessionIdCookie = new Cookie(Cookies.SESSION_ID.getName(), "");
            sessionIdCookie.setMaxAge(0);
            sessionIdCookie.setPath(Cookies.SESSION_ID_PATH);
            response.addCookie(sessionIdCookie);

            StringBuilder urlParams = new StringBuilder("/utils/sign-in?");
            urlParams.append(HtmlVariable.ERROR.getName());
            urlParams.append("=");
            urlParams.append(error.toString());
            urlParams.append("&");
            urlParams.append(HtmlVariable.CAUSE_OF_DISCONNECT.getName());
            urlParams.append("=");
            urlParams.append(causeOfDisconnect.toString());

            response.sendRedirect(urlParams.toString());
        }
    }

    @Override
    public void destroy() { }
}
