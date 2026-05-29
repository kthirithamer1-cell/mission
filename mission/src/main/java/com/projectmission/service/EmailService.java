package com.projectmission.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendVerificationEmail(String to, String token, String prenom) {
        String link = frontendUrl + "/verify-email?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("SWIFT - Vérification de votre email");
        message.setText(
                "Bonjour " + prenom + ",\n\n"
                        + "Merci de vous être inscrit sur SWIFT.\n"
                        + "Cliquez sur le lien suivant pour activer votre compte :\n"
                        + link + "\n\n"
                        + "Ce lien expire dans 24 heures.\n\n"
                        + "Si vous n'avez pas créé de compte, ignorez cet email."
        );
        log.info("Sending verification email from {} to {}", from, to);
        mailSender.send(message);
        log.info("Verification email sent to {}", to);
    }
}
