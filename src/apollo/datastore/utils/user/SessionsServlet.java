package apollo.datastore.utils.user;

import apollo.datastore.AuthRequestAttribute;
import apollo.datastore.Session;
import apollo.datastore.Session.DatastoreProperties;
import apollo.datastore.CauseOfDisconnect;
import apollo.datastore.MiscFunctions;
import apollo.datastore.SessionFactory;
import apollo.datastore.UserBean;
import apollo.datastore.UserPermissionsBean;
import apollo.datastore.utils.Error;
import apollo.datastore.utils.HtmlVariable;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SessionsServlet extends HttpServlet {

    public static final int PAGE_SIZE = 20;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewSessions()) {
            UserBean userBean = (UserBean)req.getAttribute(AuthRequestAttribute.USER.getName());

            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

            FetchOptions fetchOptions = FetchOptions.Builder.withLimit(PAGE_SIZE);

            String nextCursor = req.getParameter(HtmlVariable.NEXT_CURSOR.getName());
            String prevCursor = req.getParameter(HtmlVariable.PREV_CURSOR.getName());
            String cursor = null;

            try {
                if(nextCursor != null)
                    cursor = MiscFunctions.decryptAES(userBean.getKeyString(), nextCursor);
                else if(prevCursor != null && prevCursor.length() != 0)
                    cursor = MiscFunctions.decryptAES(userBean.getKeyString(), prevCursor);
                if(cursor != null)
                    fetchOptions.startCursor(Cursor.fromWebSafeString(cursor));
            }
            catch(Exception e) { }

            Filter userFilter = new FilterPredicate(DatastoreProperties.USER_KEY.getName(), FilterOperator.EQUAL, userBean.getKey());
            Query q = new Query(DatastoreProperties.KIND.getName());
            q.setFilter(userFilter);
            PreparedQuery pq = datastore.prepare(q);
            ArrayList<Session> sessions = new ArrayList<Session>();
            QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
            for(Entity entity : results) {
                sessions.add(new Session(entity));
            }
            String urlEncodedCursor = null;
            try {
                urlEncodedCursor = URLEncoder.encode(MiscFunctions.encryptAES(userBean.getKeyString(), results.getCursor().toWebSafeString()), MiscFunctions.UTF_CHARSET);
            }
            catch(Exception e) { }

            if(nextCursor != null || prevCursor != null) {
                StringBuilder responseJson = new StringBuilder("{ \"");
                responseJson.append(HtmlVariable.SESSIONS.getName());
                responseJson.append("\" : [ ");
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
                df.setTimeZone(java.util.TimeZone.getTimeZone(userBean.getDateFormatId()));
                for(Session session : sessions) {
                    responseJson.append("[ \"");
                    responseJson.append(session.getSessionId().substring(0, 32));
                    responseJson.append("...\", \"");
                    responseJson.append(df.format(session.getDateSignedIn()));
                    responseJson.append("\", \"");
                    responseJson.append(df.format(session.getLastSessionCheck()));
                    responseJson.append("\", ");
                    responseJson.append(session.getSessionTimeout());
                    responseJson.append(", \"");
                    responseJson.append(session.getSessionId());
                    responseJson.append("\" ], ");
                }
                if(sessions.size() > 0)
                    responseJson.delete(responseJson.length() - 2, responseJson.length());
                responseJson.append(" ], \"");
                responseJson.append(HtmlVariable.NEXT_CURSOR.getName());
                responseJson.append("\" : ");
                if(sessions.size() < PAGE_SIZE)
                    responseJson.append("null");
                else {
                    responseJson.append("\"");
                    responseJson.append(urlEncodedCursor);
                    responseJson.append("\"");
                }
                responseJson.append(" }");
                resp.setContentType("application/json; charset=UTF-8");
                resp.getWriter().print(responseJson.toString());
            }
            else {
                StringBuilder cursorListJson = new StringBuilder("[ ");
                cursorListJson.append("null, ");
                cursorListJson.append("null, ");
                if(sessions.size() < PAGE_SIZE) {
                    cursorListJson.append("null, ");
                    cursorListJson.append("null");
                }
                else {
                    cursorListJson.append("\"\", ");
                    cursorListJson.append("\"");
                    cursorListJson.append(urlEncodedCursor);
                    cursorListJson.append("\"");
                }
                cursorListJson.append(" ]");
                req.setAttribute(AuthRequestAttribute.SESSIONS.getName(), sessions);
                req.setAttribute(AuthRequestAttribute.PAGE_SIZE.getName(), PAGE_SIZE);
                req.setAttribute(AuthRequestAttribute.CURSOR_LIST.getName(), cursorListJson);
                req.getRequestDispatcher("/WEB-INF/auth/sessions.jsp").forward(req, resp);
            }
        }
        else
            resp.sendRedirect("/auth/settings");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewSessions() && userPermissionsBean.getDisconnectSessions()) {
            String[] sessionIds = req.getParameterValues(HtmlVariable.SESSION_ID.getName() + "[]");
            Map<String, Boolean> disconnectResults = new HashMap<String, Boolean>();
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Calendar dateNow = Calendar.getInstance(java.util.TimeZone.getTimeZone(MiscFunctions.UTC_STRING));
            for(int i = 0; i < sessionIds.length; ++i) {
                Error error = Error.NONE;
                CauseOfDisconnect causeOfDisconnect = CauseOfDisconnect.DISCONNECTED_SESSION;
                Transaction txn = datastore.beginTransaction(TransactionOptions.Builder.withXG(true));
                Session session = SessionFactory.getBySessionId(datastore, txn, sessionIds[i]);
                if(session != null) {
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
                        disconnectResults.put(sessionIds[i], true);
                    }
                    catch(ConcurrentModificationException e) {
                        error = Error.ERROR_IN_SESSIONS;
                    }
                }
                else {
                    error = Error.NON_EXISTENT_SESSION;
                    disconnectResults.put(sessionIds[i], false);
                }

                if(error != Error.NONE && txn.isActive())
                    txn.rollback();
            }

            StringBuilder responseJson = new StringBuilder("{ ");
            for (Map.Entry<String, Boolean> entry : disconnectResults.entrySet()) {
                responseJson.append("\"");
                responseJson.append(entry.getKey());
                responseJson.append("\" : ");
                responseJson.append(entry.getValue().toString());
                responseJson.append(", ");
            }
            if(disconnectResults.size() > 0)
                responseJson.delete(responseJson.length() - 2, responseJson.length());
            responseJson.append(" }");
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().print(responseJson.toString());
        }
        else
            resp.sendRedirect("/auth/settings");
    }
}
