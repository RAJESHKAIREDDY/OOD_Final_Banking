package emailservice;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailNotificationService {
    private static final String fromEmail = System.getenv("email");
    private static final String password = System.getenv("password");
    private static final Properties properties;

    static {
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
    }

    public static void sendEmail(String toEmail, String subject, String message) throws MessagingException {
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message email = new MimeMessage(session);
        email.setFrom(new InternetAddress(fromEmail));
        email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        email.setSubject(subject);
        email.setText(message);
    }
}
