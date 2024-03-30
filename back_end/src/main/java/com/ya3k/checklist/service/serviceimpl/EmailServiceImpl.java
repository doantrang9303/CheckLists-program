package com.ya3k.checklist.service.serviceimpl;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender emailSender;
    public void sendEmail(String[] to, String subject, String body) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            // Set recipients
            InternetAddress[] recipientAddresses = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                recipientAddresses[i] = new InternetAddress(to[i]);
            }
            message.setRecipients(MimeMessage.RecipientType.TO, recipientAddresses);
            helper.setSubject(subject);
            helper.setText(body, true); // true indicates html

            emailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}