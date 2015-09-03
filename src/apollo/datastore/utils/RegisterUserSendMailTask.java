package apollo.datastore.utils;

import apollo.datastore.User;
import apollo.datastore.UserFactory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RegisterUserSendMailTask extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String queueName = req.getHeader("X-AppEngine-QueueName");
        if(queueName != null && queueName.compareTo(RegisterUserServlet.SEND_MAIL_TASK_QUEUE) == 0) {

            String userId = req.getParameter(HtmlVariable.USER_ID.getName());
            String password = req.getParameter(HtmlVariable.PASSWORD.getName());
            if(userId != null && userId.length() != 0) {
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                User user = UserFactory.getByUserId(datastore, null, userId);
                if(user != null)
                    try {
                        Locale locale = new Locale(req.getParameter(Cookies.LANG.getName()));
                        // get i18n ResourceBundle
                        ResourceBundle utilitiesMailBundle = ResourceBundle.getBundle("apollo.datastore.utils.i18n.UtilitiesMailBundle", locale);

                        Properties props = new Properties();
                        Session session = Session.getDefaultInstance(props, null);

                        MimeMessage msg = new MimeMessage(session);
                        msg.setFrom(new InternetAddress("kerafill1116@gmail.com", "Kera Fill"));
                        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmailAddress()));
                        msg.setSubject(utilitiesMailBundle.getString("subject_activation"), "UTF-8");

                        StringBuilder sbText1 = new StringBuilder();
                        sbText1.append(utilitiesMailBundle.getString("message_register"));
                        sbText1.append("<h3>");
                        sbText1.append(utilitiesMailBundle.getString("user_account_details"));
                        sbText1.append("</h3>");
                        sbText1.append(utilitiesMailBundle.getString("user_id_label"));
                        sbText1.append(" ");
                        sbText1.append(userId);
                        sbText1.append("<br />");
                        sbText1.append(utilitiesMailBundle.getString("password_label"));
                        sbText1.append(" ");
                        sbText1.append(password);
                        sbText1.append("<br /><br />");
                        sbText1.append(utilitiesMailBundle.getString("click_link_activation"));
                        sbText1.append("<br />");
                        URL activateUserURL = new URL(req.getScheme(), req.getServerName(), req.getServerPort(), "/utils/activate-user?" + HtmlVariable.ACTIVATION_KEY.getName() + "=" + user.getKeyString());
                        sbText1.append("<a href=\"");
                        sbText1.append(activateUserURL.toString());
                        sbText1.append("\">");
                        sbText1.append(utilitiesMailBundle.getString("activate_link"));
                        sbText1.append("</a>");
                        sbText1.append("<br /><br />");
                        sbText1.append(utilitiesMailBundle.getString("copy_paste"));
                        sbText1.append("<br />");
                        sbText1.append(activateUserURL.toString());

                        msg.setContent(sbText1.toString(), "text/html; charset=utf-8");
                        Transport.send(msg);
                    }
                    catch(MessagingException e) { } // ignore messaging exception
            }
        }
    }
}
