package io.crm.app.utils;

import io.crm.app.model.emailsender.SmtpSendResponse;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.HashMap;
import java.util.Properties;



public class SMTPEmailSender {

    private String smtpServerHostName;
    private String smtpServerPort;
    final private String smtpUserName = "noreply@testapp.com";
    final private String smtpUserPassword = "Mob34280";
    public SMTPEmailSender() {
       //this.smtpServerHostName = "smtp.gmail.com";
          this.smtpServerHostName = "smtp.office365.com";
       this.smtpServerPort = "587";
        // this.smtpServerHostName = "newSmtpHost";
        // this.smtpServerPort = "newSmtpPort";
    }

    public SmtpSendResponse sendMail(HashMap<String, String> emailInfo) throws Exception {

        SmtpSendResponse smtpSendResponse=new SmtpSendResponse();
        smtpSendResponse.setMessageStatus(Boolean.FALSE);
        emailInfo.put("Sender", "noreply@testapp.com");

        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.smtpServerHostName);
        properties.put("mail.smtp.port", this.smtpServerPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS
        // Add any additional properties as required

        // Set up authentication if needed
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUserName, smtpUserPassword);
            }
        };

        // Create a session with the properties and authentication
        Session session = Session.getInstance(properties, authenticator);

        try {
            // Create a new message
            Message message = new MimeMessage(session);

            // Set the sender
           // message.setFrom(new InternetAddress("sender@example.com"));
            message.setFrom(new InternetAddress(emailInfo.get("Sender")));

            // Set the recipients
            //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("recipient@example.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailInfo.get("Recipient")));

            // Set the subject
           //message.setSubject("Hello from JavaMail");
            message.setSubject(emailInfo.get("Subject"));

            // Set the content
            //message.setText("This is the message body.");
            message.setText(emailInfo.get("Body"));

            // Send the message
            Transport.send(message);

            smtpSendResponse.setMessageStatus(Boolean.TRUE);
            smtpSendResponse.setMessageResponse("Email sent successfully.");
        } catch (MessagingException e) {
            //e.printStackTrace();
            smtpSendResponse.setMessageResponse(e.getMessage());
        }
        return smtpSendResponse;
    }

    public SmtpSendResponse sendHtmlMail(HashMap<String, String> emailInfo) throws Exception {

        SmtpSendResponse smtpSendResponse=new SmtpSendResponse();
        smtpSendResponse.setMessageStatus(Boolean.FALSE);
        emailInfo.put("Sender", "noreply@testapp.com");

        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.smtpServerHostName);
        properties.put("mail.smtp.port", this.smtpServerPort);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS
        // Add any additional properties as required

        // Set up authentication if needed
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUserName, smtpUserPassword);
            }
        };

        // Create a session with the properties and authentication
        Session session = Session.getInstance(properties, authenticator);

        try {
            // Create a new message
            Message message = new MimeMessage(session);

            // Set the sender
            message.setFrom(new InternetAddress(emailInfo.get("Sender")));

            // Set the recipients
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailInfo.get("Recipient")));

            // Set the subject
            message.setSubject(emailInfo.get("Subject"));

            // Create a multipart message
            Multipart multipart = new MimeMultipart();

            // Create a MimeBodyPart for the HTML content
            MimeBodyPart htmlPart = new MimeBodyPart();
           // String htmlContent = "<html><body><h1>Hello, this is HTML content!</h1></body></html>";

            String html = new StringBuilder()
                    .append("<html>\n")
                    .append("     <body>\n")
                    .append("         $content\n")
                    .append("     </body>\n")
                    .append("</html>")
                    .toString();

            //String htmlContent = emailInfo.get("Body");
            html=html.replace("$content",emailInfo.get("Body"));
            htmlPart.setContent(html, "text/html");


            // Add the HTML part to the multipart message
            multipart.addBodyPart(htmlPart);

            // Set the multipart message as the content of the email
            message.setContent(multipart);

            // Send the message
            Transport.send(message);
            smtpSendResponse.setMessageStatus(Boolean.TRUE);
            smtpSendResponse.setMessageResponse("Email sent successfully.");
        } catch (MessagingException e) {
            //e.printStackTrace();
            smtpSendResponse.setMessageResponse(e.getMessage());
        }
        return smtpSendResponse;
    }


}
