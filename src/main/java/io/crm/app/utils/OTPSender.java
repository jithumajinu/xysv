package io.crm.app.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class OTPSender {
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmailOtp(String otpCode,String recipent)
    {
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo(recipent);
//
//        msg.setSubject("text appAW FIRM -OTP" );
//        msg.setText("YOUR OTP is "+otpCode);
//
//        mailSender.send(msg);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setSubject("text appAW FIRM -OTP");
            mimeMessageHelper.setFrom(new InternetAddress("noreply@testapp.com"));
            mimeMessageHelper.setTo(recipent);
            mimeMessageHelper.setText("YOUR OTP is "+otpCode);
            mailSender.send(mimeMessageHelper.getMimeMessage());
        }
        catch (MessagingException e) {
          System.out.println(e.getMessage());
        }
    }
}
