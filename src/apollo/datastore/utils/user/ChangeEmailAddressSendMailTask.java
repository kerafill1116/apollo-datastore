package apollo.datastore.utils.user;

import apollo.datastore.ChangeEmailAddressRequest;
import apollo.datastore.ChangeEmailAddressRequestFactory;
import apollo.datastore.Cookies;
import apollo.datastore.utils.HtmlVariable;

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
public class ChangeEmailAddressSendMailTask extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String queueName = req.getHeader("X-AppEngine-QueueName");
        if(queueName != null && queueName.compareTo(ChangeEmailAddressServlet.SEND_MAIL_TASK_QUEUE) == 0) {

            String userId = req.getParameter(HtmlVariable.USER_ID.getName());
            String requestId = req.getParameter(HtmlVariable.REQUEST_ID.getName());
            if(userId != null && userId.length() != 0) {
                DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
                ChangeEmailAddressRequest changeEmailAddressRequest = ChangeEmailAddressRequestFactory.getByUserId(datastore, null, userId);
                if(changeEmailAddressRequest != null && changeEmailAddressRequest.getRequestId().compareTo(requestId) == 0)
                    try {
                        Locale locale = new Locale(req.getParameter(Cookies.LANG.getName()));
                        // get i18n ResourceBundle
                        ResourceBundle changeEmailAddressMailBundle = ResourceBundle.getBundle("apollo.datastore.i18n.ChangeEmailAddressMailBundle", locale);

                        Properties props = new Properties();
                        Session session = Session.getDefaultInstance(props, null);

                        MimeMessage msg = new MimeMessage(session);
                        msg.setFrom(new InternetAddress("kerafill1116@gmail.com", "Kera Fill"));
                        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(changeEmailAddressRequest.getEmailAddress()));
                        msg.setSubject(changeEmailAddressMailBundle.getString("subject_change_email_address"), "UTF-8");

                        StringBuilder sbText1 = new StringBuilder();
                        sbText1.append(changeEmailAddressMailBundle.getString("message_change_email_address"));
                        sbText1.append("<h3>");
                        sbText1.append(changeEmailAddressMailBundle.getString("user_account_details"));
                        sbText1.append("</h3>");
                        sbText1.append(changeEmailAddressMailBundle.getString("user_id_label"));
                        sbText1.append(" ");
                        sbText1.append(userId);
                        sbText1.append("<br />");
                        sbText1.append(changeEmailAddressMailBundle.getString("new_email_address_label"));
                        sbText1.append(" ");
                        sbText1.append(changeEmailAddressRequest.getEmailAddress());
                        sbText1.append("<br /><br />");
                        sbText1.append(changeEmailAddressMailBundle.getString("click_link_change_email_address"));
                        sbText1.append("<br />");
                        URL verifyEmailAddressURL = new URL(req.getScheme(), req.getServerName(), req.getServerPort(), "/auth/change-email-address?" + HtmlVariable.REQUEST_ID.getName() + "=" + requestId);
                        sbText1.append("<a href=\"");
                        sbText1.append(verifyEmailAddressURL.toString());
                        sbText1.append("\">");
                        sbText1.append(changeEmailAddressMailBundle.getString("verify_email_address_link"));
                        sbText1.append("</a>");
                        sbText1.append("<br /><br />");
                        sbText1.append(changeEmailAddressMailBundle.getString("copy_paste"));
                        sbText1.append("<br />");
                        sbText1.append(verifyEmailAddressURL.toString());

                        msg.setContent(sbText1.toString(), "text/html; charset=utf-8");
                        Transport.send(msg);
                    }
                    catch(MessagingException e) { } // ignore messaging exception
            }
        }
    }
}
