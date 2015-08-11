package apollo.datastore.utils;

import apollo.datastore.User;
import apollo.datastore.UserFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CheckUserIdAvailabilityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Error error = Error.NONE;

        String jsonEnd = HtmlVariable.AVAILABLE.getName();
        String userId = req.getParameter(HtmlVariable.USER_ID.getName());
        if(userId == null || userId.length() == 0) {
            error = Error.REQUIRED_USER_ID;
            jsonEnd = jsonEnd + "\": false }";
        }
        else {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            User user = UserFactory.getByUserId(datastore, null, userId);
            jsonEnd = (user != null) ? jsonEnd + "\": false }" : jsonEnd + "\": true }";
        }

        String jsonStart = "{ \"" + HtmlVariable.ERROR.getName() + "\": " + error.toString() + ", \"";
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().print(jsonStart + jsonEnd);
    }
}
