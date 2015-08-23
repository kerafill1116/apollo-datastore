package apollo.datastore.utils.user;

import apollo.datastore.AuthRequestAttribute;
import apollo.datastore.Session;
import apollo.datastore.Session.DatastoreProperties;
import apollo.datastore.SessionLog;
import apollo.datastore.UserBean;
import apollo.datastore.UserPermissionsBean;
import apollo.datastore.utils.Error;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SessionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewSessions()) {
            Error error = Error.NONE;
            UserBean userBean = (UserBean)req.getAttribute(AuthRequestAttribute.USER.getName());

            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

            int pageSize = 50;
            FetchOptions fetchOptions = FetchOptions.Builder.withLimit(pageSize);

            Filter userFilter = new FilterPredicate(DatastoreProperties.USER_KEY.getName(), FilterOperator.EQUAL, userBean.getKey());
            Query q = new Query(SessionLog.DatastoreProperties.KIND.getName());
            q.setFilter(userFilter);
            PreparedQuery pq = datastore.prepare(q);

            ArrayList<Session> sessions = new ArrayList<Session>();
            QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
            for(Entity entity : results) {
                sessions.add(new Session(entity));
            }

            ArrayList<String> cursorList = new ArrayList<String>();
            cursorList.add(null);
            cursorList.add(null);
            cursorList.add("");
            cursorList.add(results.getCursor().toWebSafeString());

            req.setAttribute(AuthRequestAttribute.SESSIONS.getName(), sessions);
            req.setAttribute(AuthRequestAttribute.CURSOR_LIST.getName(), cursorList);
            req.getRequestDispatcher("/WEB-INF/auth/sessions.jsp").forward(req, resp);
        }
        else
            resp.sendRedirect("/auth/settings");
    }
}
