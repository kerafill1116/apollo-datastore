package apollo.datastore.utils.user;

import apollo.datastore.AuthRequestAttribute;
import apollo.datastore.UserPermissionsBean;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ChangeEmailAddressServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getChangeEmailAddress())
            req.getRequestDispatcher("/WEB-INF/auth/change-email-address.jsp").forward(req, resp);
        else
            resp.sendRedirect("/auth/settings");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getChangeEmailAddress()) {

            req.getRequestDispatcher("/WEB-INF/auth/change-email-address.jsp").forward(req, resp);
        }
        else
            resp.sendRedirect("/auth/settings");
    }
}
