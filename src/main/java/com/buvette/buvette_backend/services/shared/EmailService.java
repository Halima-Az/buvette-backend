package com.buvette.buvette_backend.services.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetPasswordEmail(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset your password");
        message.setText(
            "Click the link below to reset your password:\n" + link
        );

        mailSender.send(message);
    }
       public void sendVerifiedPasswordEmail(String to, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify of your SnackUp account");
        message.setText(
            "Please click the link below to verify your account:\n" + link
        );

        mailSender.send(message);
    }
}
