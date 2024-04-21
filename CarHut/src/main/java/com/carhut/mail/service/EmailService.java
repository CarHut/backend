package com.carhut.mail.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailMessage(String to, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hello, please click on the following link to finish the password reset procedure.\n");
        stringBuilder.append("http://localhost:3000/passwordReset?resetPasswordToken=").append(text);
        stringBuilder.append("\n\n");
        stringBuilder.append("CarHut");

        try {
            helper.setFrom("carhutsenderbot@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(stringBuilder.toString());
        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(mimeMessage);
    }

}
