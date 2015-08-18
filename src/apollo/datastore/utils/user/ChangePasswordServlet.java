package apollo.datastore.utils.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import apollo.datastore.AuthRequestAttribute;
import apollo.datastore.UserPermissionsBean;

@SuppressWarnings("serial")
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        UserPermissionsBean userPermissionsBean = (UserPermissionsBean)req.getAttribute(AuthRequestAttribute.USER_PERMISSIONS.getName());

        if(userPermissionsBean.getChangePassword())
            req.getRequestDispatcher("/WEB-INF/auth/change-password.jsp").forward(req, resp);
        else
            resp.sendRedirect("/auth/settings");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }
}
