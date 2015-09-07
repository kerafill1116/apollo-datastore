package apollo.datastore.utils.admin;

import apollo.datastore.AdminPermissionsBean;
import apollo.datastore.MiscFunctions;
import apollo.datastore.User;
import apollo.datastore.UserBean;
import apollo.datastore.User.DatastoreProperties;
import apollo.datastore.utils.AuthRequestAttribute;
import apollo.datastore.utils.HtmlVariable;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class UsersServlet extends HttpServlet {

    public static final int PAGE_SIZE = 20;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        AdminPermissionsBean adminPermissionsBean = (AdminPermissionsBean)req.getAttribute(AuthRequestAttribute.ADMIN_PERMISSIONS.getName());

        if(adminPermissionsBean.getViewUsers()) {
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

            Query q = new Query(DatastoreProperties.KIND.getName());
            q.addProjection(new PropertyProjection(DatastoreProperties.USER_ID.getName(), String.class));
            q.addProjection(new PropertyProjection(DatastoreProperties.DATE_CREATED.getName(), String.class));
            q.addSort(DatastoreProperties.USER_ID.getName(), SortDirection.ASCENDING);
            PreparedQuery pq = datastore.prepare(q);
            ArrayList<User> users = new ArrayList<User>();
            QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
            for(Entity entity : results) {
                users.add(new User(entity));
            }
            String urlEncodedCursor = null;
            try {
                urlEncodedCursor = URLEncoder.encode(MiscFunctions.encryptAES(userBean.getKeyString(), results.getCursor().toWebSafeString()), MiscFunctions.UTF_CHARSET);
            }
            catch(Exception e) { }

            if(nextCursor != null || prevCursor != null) {
                StringBuilder responseJson = new StringBuilder("{ \"");
                responseJson.append(HtmlVariable.USERS.getName());
                responseJson.append("\" : [ ");
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z");
                df.setTimeZone(java.util.TimeZone.getTimeZone(userBean.getTimeZoneLocaleId()));
                for(User user : users) {
                    responseJson.append("[ \"");
                    responseJson.append(user.getUserId());
                    responseJson.append("\", \"");
                    responseJson.append(df.format(user.getDateCreated()));
                    responseJson.append("\" ], ");
                }
                if(users.size() > 0)
                    responseJson.delete(responseJson.length() - 2, responseJson.length());
                responseJson.append(" ], \"");
                responseJson.append(HtmlVariable.NEXT_CURSOR.getName());
                responseJson.append("\" : ");
                if(users.size() < PAGE_SIZE)
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
                if(users.size() < PAGE_SIZE) {
                    cursorListJson.append("null, ");
                    cursorListJson.append("null ]");
                }
                else {
                    cursorListJson.append("\"\", ");
                    cursorListJson.append("\"");
                    cursorListJson.append(urlEncodedCursor);
                    cursorListJson.append("\" ]");
                }
                req.setAttribute(AuthRequestAttribute.USERS.getName(), users);
                req.setAttribute(AuthRequestAttribute.PAGE_SIZE.getName(), PAGE_SIZE);
                req.setAttribute(AuthRequestAttribute.CURSOR_LIST.getName(), cursorListJson);
                req.getRequestDispatcher("/WEB-INF/auth/admin-users.jsp").forward(req, resp);
            }
        }
        else
            resp.sendRedirect("/auth/settings");
    }
}
