package apollo.datastore.utils;

import apollo.datastore.Cookies;
import apollo.datastore.MiscFunctions;
import apollo.datastore.User;
import apollo.datastore.UserFactory;
import apollo.datastore.MiscFunctions.HashAlgorithms;

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
public class ResetPasswordSendMailTask extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String queueName = req.getHeader("X-AppEngine-QueueName");
        if(queueName != null && queueName.compareTo(ResetPasswordServlet.SEND_MAIL_TASK_QUEUE) == 0) {

            String userId = req.getParameter(HtmlVariable.USER_ID.getName());
            String newPassword = req.getParameter(HtmlVariable.NEW_PASSWORD.getName());
            if(userId != null && userId.length() != 0) {
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                User user = UserFactory.getByUserId(datastore, null, userId);
                if(user != null && user.getPassword().compareTo(MiscFunctions.getEncryptedHash(newPassword, HashAlgorithms.SHA_256)) == 0)
                    try {
                        Locale locale = new Locale(req.getParameter(Cookies.LANG.getName()));
                        // get i18n ResourceBundle
                        ResourceBundle utilitiesMailBundle = ResourceBundle.getBundle("apollo.datastore.i18n.UtilitiesMailBundle", locale);

                        Properties props = new Properties();
                        Session session = Session.getDefaultInstance(props, null);

                        MimeMessage msg = new MimeMessage(session);
                        msg.setFrom(new InternetAddress("kerafill1116@gmail.com", "Kera Fill"));
                        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmailAddress()));
                        msg.setSubject(utilitiesMailBundle.getString("subject_reset_password"), "UTF-8");

                        StringBuilder sbText1 = new StringBuilder();
                        sbText1.append(utilitiesMailBundle.getString("message_reset_password"));
                        sbText1.append("<h3>");
                        sbText1.append(utilitiesMailBundle.getString("user_account_details"));
                        sbText1.append("</h3>");
                        sbText1.append(utilitiesMailBundle.getString("user_id_label"));
                        sbText1.append(" ");
                        sbText1.append(userId);
                        sbText1.append("<br />");
                        sbText1.append(utilitiesMailBundle.getString("password_label"));
                        sbText1.append(" ");
                        sbText1.append(newPassword);
                        sbText1.append("<br /><br />");
                        sbText1.append(utilitiesMailBundle.getString("click_link_reset_password"));
                        sbText1.append("<br />");
                        URL signInURL = new URL(req.getScheme(), req.getServerName(), req.getServerPort(), "/utils/sign-in");
                        sbText1.append("<a href=\"");
                        sbText1.append(signInURL.toString());
                        sbText1.append("\">");
                        sbText1.append(utilitiesMailBundle.getString("sign_in_link"));
                        sbText1.append("</a>");
                        sbText1.append("<br /><br />");
                        sbText1.append(signInURL.toString());

                        msg.setContent(sbText1.toString(), "text/html; charset=utf-8");
                        Transport.send(msg);
                    }
                    catch(MessagingException e) { } // ignore messaging exception
            }
        }
    }
}
