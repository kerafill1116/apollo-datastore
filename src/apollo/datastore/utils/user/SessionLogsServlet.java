package apollo.datastore.utils.user;

import apollo.datastore.AuthRequestAttribute;
import apollo.datastore.SessionLog;
import apollo.datastore.SessionLog.DatastoreProperties;
import apollo.datastore.Cookies;
import apollo.datastore.MiscFunctions;
import apollo.datastore.SessionLogFactory;
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
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SessionLogsServlet extends HttpServlet {

    public static final int PAGE_SIZE = 20;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewSessionLogs()) {
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
            ArrayList<SessionLog> sessionLogs = new ArrayList<SessionLog>();
            QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
            for(Entity entity : results) {
                sessionLogs.add(new SessionLog(entity));
            }
            String urlEncodedCursor = null;
            try {
                urlEncodedCursor = URLEncoder.encode(MiscFunctions.encryptAES(userBean.getKeyString(), results.getCursor().toWebSafeString()), MiscFunctions.UTF_CHARSET);
            }
            catch(Exception e) { }

            if(nextCursor != null || prevCursor != null) {
                StringBuilder responseJson = new StringBuilder("{ \"");
                responseJson.append(HtmlVariable.SESSION_LOGS.getName());
                responseJson.append("\" : [ ");
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
                df.setTimeZone(java.util.TimeZone.getTimeZone(userBean.getDateFormatId()));
                for(SessionLog sessionLog : sessionLogs) {
                    responseJson.append("[ \"");
                    responseJson.append(sessionLog.getSessionId().substring(0, 32));
                    responseJson.append("...\", \"");
                    responseJson.append(df.format(sessionLog.getDateSignedIn()));
                    responseJson.append("\", ");
                    responseJson.append(sessionLog.getSessionTimeout());
                    responseJson.append(", \"");
                    responseJson.append(df.format(sessionLog.getDateSignedOut()));
                    responseJson.append("\", ");
                    responseJson.append(sessionLog.getCauseOfDisconnect());
                    responseJson.append(", \"");
                    responseJson.append(sessionLog.getSessionId());
                    responseJson.append("\" ], ");
                }
                if(sessionLogs.size() > 0)
                    responseJson.delete(responseJson.length() - 2, responseJson.length());
                responseJson.append(" ], \"");
                responseJson.append(HtmlVariable.NEXT_CURSOR.getName());
                responseJson.append("\" : ");
                if(sessionLogs.size() < PAGE_SIZE)
                    responseJson.append("null }");
                else {
                    responseJson.append("\"");
                    responseJson.append(urlEncodedCursor);
                    responseJson.append("\" }");
                }
                resp.setContentType("application/json; charset=UTF-8");
                resp.getWriter().print(responseJson.toString());
            }
            else {
                StringBuilder cursorListJson = new StringBuilder("[ null, null, ");
                if(sessionLogs.size() < PAGE_SIZE) {
                    cursorListJson.append("null, ");
                    cursorListJson.append("null ]");
                }
                else {
                    cursorListJson.append("\"\", ");
                    cursorListJson.append("\"");
                    cursorListJson.append(urlEncodedCursor);
                    cursorListJson.append("\" ]");
                }

                Locale locale = new Locale((String)req.getAttribute(Cookies.LANG.getName()));
                ResourceBundle causeOfDisconnectMessagesBundle = ResourceBundle.getBundle("apollo.datastore.i18n.CauseOfDisconnectMessagesBundle", locale);
                StringBuilder causeOfDisconnectMessagesJson = new StringBuilder("[ \"");
                causeOfDisconnectMessagesJson.append(causeOfDisconnectMessagesBundle.getString("none").replace("\"", "\\\""));
                causeOfDisconnectMessagesJson.append("\", \"");
                causeOfDisconnectMessagesJson.append(causeOfDisconnectMessagesBundle.getString("exclusive_session").replace("\"", "\\\""));
                causeOfDisconnectMessagesJson.append("\", \"");
                causeOfDisconnectMessagesJson.append(causeOfDisconnectMessagesBundle.getString("timed_out_session").replace("\"", "\\\""));
                causeOfDisconnectMessagesJson.append("\", \"");
                causeOfDisconnectMessagesJson.append(causeOfDisconnectMessagesBundle.getString("disconnected_session").replace("\"", "\\\""));
                causeOfDisconnectMessagesJson.append("\", \"");
                causeOfDisconnectMessagesJson.append(causeOfDisconnectMessagesBundle.getString("invalid_cause_of_disconnect").replace("\"", "\\\""));
                causeOfDisconnectMessagesJson.append("\" ]");

                req.setAttribute(AuthRequestAttribute.SESSION_LOGS.getName(), sessionLogs);
                req.setAttribute(AuthRequestAttribute.PAGE_SIZE.getName(), PAGE_SIZE);
                req.setAttribute(AuthRequestAttribute.CURSOR_LIST.getName(), cursorListJson);
                req.setAttribute(AuthRequestAttribute.CAUSE_OF_DISCONNECT_MESSAGES.getName(), causeOfDisconnectMessagesJson);
                req.getRequestDispatcher("/WEB-INF/auth/session-logs.jsp").forward(req, resp);
            }
        }
        else
            resp.sendRedirect("/auth/settings");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewSessionLogs() && userPermissionsBean.getRemoveSessionLogs()) {
            String[] sessionIds = req.getParameterValues(HtmlVariable.SESSION_ID.getName() + "[]");
            Map<String, Boolean> removeResults = new HashMap<String, Boolean>();
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            for(int i = 0; i < sessionIds.length; ++i) {
                Error error = Error.NONE;
                Transaction txn = datastore.beginTransaction();
                SessionLog sessionLog = SessionLogFactory.getBySessionId(datastore, txn, sessionIds[i]);
                if(sessionLog != null) {
                    try {
                        SessionLogFactory.remove(datastore, txn, sessionLog);
                        txn.commit();
                        removeResults.put(sessionIds[i], true);
                    }
                    catch(ConcurrentModificationException e) {
                        error = Error.ERROR_IN_SESSION_LOGS;
                    }
                }
                else {
                    error = Error.NON_EXISTENT_SESSION_LOG;
                    removeResults.put(sessionIds[i], false);
                }

                if(error != Error.NONE && txn.isActive())
                    txn.rollback();
            }

            StringBuilder responseJson = new StringBuilder("{ ");
            for (Map.Entry<String, Boolean> entry : removeResults.entrySet()) {
                responseJson.append("\"");
                responseJson.append(entry.getKey());
                responseJson.append("\" : ");
                responseJson.append(entry.getValue().toString());
                responseJson.append(", ");
            }
            if(removeResults.size() > 0)
                responseJson.delete(responseJson.length() - 2, responseJson.length());
            responseJson.append(" }");
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().print(responseJson.toString());
        }
        else
            resp.sendRedirect("/auth/settings");
    }
}
