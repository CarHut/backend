package com.carhut.services.mail;

import com.carhut.enums.ServiceStatusEntity;
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

    public ServiceStatusEntity sendEmailMessage(String to, String from, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
        }
        catch (MessagingException e) {
            return ServiceStatusEntity.ERROR;
        }

        javaMailSender.send(mimeMessage);

        return ServiceStatusEntity.SUCCESS;
    }

}
