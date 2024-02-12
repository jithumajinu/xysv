package io.crm.app.utils;

import com.google.common.io.ByteSource;
import io.crm.app.model.emailsender.SmtpSendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

@Component
public class SMTPEmailSender2 {
    @Autowired
    private JavaMailSender mailSender;


//@Autowired
//public SMTPEmailSender2(JavaMailSender javaMailSender) {
//    this.mailSender = javaMailSender;
//}

//    @Async
//    public void sendEmail()
//    {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo("mahe78611@gmail.com");
//
//        msg.setSubject("Testing from Spring Boot");
//        msg.setText("Hello World \n Spring Boot Email");
//
//        mailSender.send(msg);
//
//        try {
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
//            mimeMessageHelper.setSubject("mail.getMailSubject()");
//            mimeMessageHelper.setFrom(new InternetAddress("noreply@testapp.com"));
//            mimeMessageHelper.setTo("mahe78611@gmail.com");
//            mimeMessageHelper.setText("mail.getMailContent()");
//            mailSender.send(mimeMessageHelper.getMimeMessage());
//        }
//        catch (MessagingException e) {
//          System.out.println(e.getMessage());
//        }
//    }

    @Async
    public SmtpSendResponse sendHtmlMail(HashMap<String, String> emailInfo) throws Exception {

        SmtpSendResponse smtpSendResponse=new SmtpSendResponse();
        smtpSendResponse.setMessageStatus(Boolean.FALSE);
        emailInfo.put("Sender", "noreply@testapp.com");
        //emailInfo.put("Sender", "mahe78611@gmail.com");

        System.out.println("-------- emailInfo2-- request-------:"+  smtpSendResponse.toString());
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //helper.setText("<b>Dear friend</b>,<br><i>Please find the book attached.</i>", true);
            String htmlContent = new StringBuilder()
                    .append("<html>\n")
                    .append("     <body>\n")
                    .append("         $content\n")
                    .append("<img src='cid:identifier1234'>")
                    .append("     </body>\n")
                    .append("</html>")
                    .toString();

            //String htmlContent = emailInfo.get("Body");

String htmlBody="<p>Hi $Name sir</p><p><img src=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII=" +
        "></p><p><br></p><p><br></p><p><br></p><p>Regards</p>";
            //htmlContent=htmlContent.replace("$content",htmlBody);
            //            htmlContent=htmlContent.replace("$content",emailInfo.get("Body"));
            helper.setSubject(emailInfo.get("Subject"));
            helper.setFrom(emailInfo.get("Sender"),"text appAW FIRM CONTACT");
            helper.setTo(emailInfo.get("Recipient"));
            //helper.setTo("mahe78611@gmail.com");
            helper.setText(htmlContent,true);
            String data ="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAApgAAAKYB3X3/OAAAABl0RVh0U29mdHdhcmUAd3d3Lmlua3NjYXBlLm9yZ5vuPBoAAANCSURBVEiJtZZPbBtFFMZ/M7ubXdtdb1xSFyeilBapySVU8h8OoFaooFSqiihIVIpQBKci6KEg9Q6H9kovIHoCIVQJJCKE1ENFjnAgcaSGC6rEnxBwA04Tx43t2FnvDAfjkNibxgHxnWb2e/u992bee7tCa00YFsffekFY+nUzFtjW0LrvjRXrCDIAaPLlW0nHL0SsZtVoaF98mLrx3pdhOqLtYPHChahZcYYO7KvPFxvRl5XPp1sN3adWiD1ZAqD6XYK1b/dvE5IWryTt2udLFedwc1+9kLp+vbbpoDh+6TklxBeAi9TL0taeWpdmZzQDry0AcO+jQ12RyohqqoYoo8RDwJrU+qXkjWtfi8Xxt58BdQuwQs9qC/afLwCw8tnQbqYAPsgxE1S6F3EAIXux2oQFKm0ihMsOF71dHYx+f3NND68ghCu1YIoePPQN1pGRABkJ6Bus96CutRZMydTl+TvuiRW1m3n0eDl0vRPcEysqdXn+jsQPsrHMquGeXEaY4Yk4wxWcY5V/9scqOMOVUFthatyTy8QyqwZ+kDURKoMWxNKr2EeqVKcTNOajqKoBgOE28U4tdQl5p5bwCw7BWquaZSzAPlwjlithJtp3pTImSqQRrb2Z8PHGigD4RZuNX6JYj6wj7O4TFLbCO/Mn/m8R+h6rYSUb3ekokRY6f/YukArN979jcW+V/S8g0eT/N3VN3kTqWbQ428m9/8k0P/1aIhF36PccEl6EhOcAUCrXKZXXWS3XKd2vc/TRBG9O5ELC17MmWubD2nKhUKZa26Ba2+D3P+4/MNCFwg59oWVeYhkzgN/JDR8deKBoD7Y+ljEjGZ0sosXVTvbc6RHirr2reNy1OXd6pJsQ+gqjk8VWFYmHrwBzW/n+uMPFiRwHB2I7ih8ciHFxIkd/3Omk5tCDV1t+2nNu5sxxpDFNx+huNhVT3/zMDz8usXC3ddaHBj1GHj/As08fwTS7Kt1HBTmyN29vdwAw+/wbwLVOJ3uAD1wi/dUH7Qei66PfyuRj4Ik9is+hglfbkbfR3cnZm7chlUWLdwmprtCohX4HUtlOcQjLYCu+fzGJH2QRKvP3UNz8bWk1qMxjGTOMThZ3kvgLI5AzFfo379UAAAAASUVORK5CYII=";
            String base64Image = data.split(",")[1];
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            //ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            InputStream targetStream = ByteSource.wrap(imageBytes).openStream();
            //final ByteArrayOutputStream stream = createInMemoryDocument("body");
            final InputStreamSource attachment = new ByteArrayResource(imageBytes);
            helper.addInline("identifier1234",attachment,"image/png");


            //File attachments
            //FileSystemResource file = new FileSystemResource(new File("Book.pdf"));
            //helper.addAttachment("FreelanceSuccess.pdf", file);

            mailSender.send(message);

            smtpSendResponse.setMessageStatus(Boolean.TRUE);
            smtpSendResponse.setMessageResponse("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            smtpSendResponse.setMessageResponse(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            smtpSendResponse.setMessageResponse(e.getMessage());
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            smtpSendResponse.setMessageResponse(e.getMessage());
        }
        System.out.println("-------- emailInfo3-- request-------:"+  smtpSendResponse.toString());
        return smtpSendResponse;
    }
}
