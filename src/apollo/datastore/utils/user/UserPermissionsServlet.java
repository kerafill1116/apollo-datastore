package apollo.datastore.utils.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import apollo.datastore.AuthRequestAttribute;
import apollo.datastore.UserPermissionsBean;

@SuppressWarnings("serial")
public class UserPermissionsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getViewUserPermissions())
            req.getRequestDispatcher("/WEB-INF/auth/user-permissions.jsp").forward(req, resp);
        else
            resp.sendRedirect("/auth/settings");
    }
}
