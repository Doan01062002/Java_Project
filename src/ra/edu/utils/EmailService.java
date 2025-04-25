package ra.edu.utils;


import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {
    public static void sendResetCode(String toEmail, String resetCode) {
        String fromEmail = "doanmit01062002@gmail.com";
        String password = "tvyl vzyj ooyw cjzw";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mã đặt lại mật khẩu");
            message.setText("Mã đặt lại mật khẩu của bạn là: " + resetCode);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}