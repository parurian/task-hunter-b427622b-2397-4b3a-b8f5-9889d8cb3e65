package dev.mher.taskhunter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * User: MheR
 * Date: 12/2/19.
 * Time: 2:10 PM.
 * Project: taskhunter.
 * Package: dev.mher.taskhunter.services.
 */
@Service
public class EmailService {

    @Value("${spring.mail.from}")
    private String from;

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void sendMail(String toEmail, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom(from);
        javaMailSender.send(mailMessage);
    }


    void sendSignUpMail(String email, String fullName, String link, String date, String ip) {
        String subject = "Please verify your email address";
        String message = String.format("Hello, %s\n" +
                "\n" +
                "We have received the request to sign up to Task Hunter.\n" +
                "\n" +
                "Here are the details of the sign in attempt: Date: %s, Ip address: %s" +
                "\n" +
                "If this was you, please follow up to verification link below:\n" +
                "\n" +
                "%s", fullName, date, ip, link);
        sendMail(email, subject, message);
    }


}
