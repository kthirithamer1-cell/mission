package com.projectmission.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${app.mail.from}")
    private String mailFrom;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    // ── Verification email ────────────────────────────────────────────
    public void sendVerificationEmail(String toEmail, String verificationToken) {
        String link = frontendUrl + "/verify-email?token=" + verificationToken;
        String subject = "Vérifiez votre adresse e-mail – Aquapulse";
        String body = buildEmail(
                "Bienvenue sur Aquapulse ! 🏊",
                "Merci de vous être inscrit. Pour activer votre compte, veuillez confirmer votre adresse e-mail en cliquant sur le bouton ci-dessous.",
                link,
                "Vérifier mon e-mail",
                "Si vous n'avez pas créé de compte, ignorez simplement cet e-mail."
        );
        sendHtmlEmail(toEmail, subject, body);
    }

    // ── Reset-password email ──────────────────────────────────────────
    public void sendResetPasswordEmail(String toEmail, String resetToken) {
        String link = frontendUrl + "/reset-password?token=" + resetToken;
        String subject = "Réinitialisation de votre mot de passe – Aquapulse";
        String body = buildEmail(
                "Réinitialisation du mot de passe 🔒",
                "Nous avons reçu une demande de réinitialisation du mot de passe de votre compte Aquapulse. Cliquez sur le bouton ci-dessous pour définir un nouveau mot de passe. Ce lien expire dans <strong>30 minutes</strong>.",
                link,
                "Réinitialiser le mot de passe",
                "Si vous n'avez pas demandé cette réinitialisation, ignorez cet e-mail. Votre mot de passe restera inchangé."
        );
        sendHtmlEmail(toEmail, subject, body);
    }

    // ── HTML template ─────────────────────────────────────────────────
    private String buildEmail(String heading, String message, String ctaLink, String ctaLabel, String footer) {
        String logoUrl = frontendUrl + "/logo.png";

        return "<!DOCTYPE html>"
            + "<html lang=\"fr\"><head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\">"
            + "<title>Aquapulse</title></head>"
            + "<body style=\"margin:0;padding:0;background-color:#f0f4f8;font-family:'Segoe UI',Roboto,Helvetica,Arial,sans-serif;\">"
            + "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color:#f0f4f8;padding:40px 0;\">"
            + "<tr><td align=\"center\">"
            // card
            + "<table role=\"presentation\" width=\"560\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color:#ffffff;border-radius:16px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,0.08);\">"
            // header
            + "<tr><td style=\"background:linear-gradient(135deg,#0891b2 0%,#06b6d4 50%,#22d3ee 100%);padding:36px 40px;text-align:center;\">"
            + "<img src=\"" + logoUrl + "\" alt=\"Aquapulse\" width=\"140\" style=\"display:block;margin:0 auto 12px auto;\" />"
            + "<p style=\"margin:0;font-size:14px;color:rgba(255,255,255,0.85);letter-spacing:0.5px;\">Swimming Club Management</p>"
            + "</td></tr>"
            // body
            + "<tr><td style=\"padding:36px 40px;\">"
            + "<h1 style=\"margin:0 0 16px;font-size:22px;color:#0f172a;font-weight:700;\">" + heading + "</h1>"
            + "<p style=\"margin:0 0 28px;font-size:15px;line-height:1.7;color:#475569;\">" + message + "</p>"
            // CTA button
            + "<table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 auto;\"><tr>"
            + "<td style=\"border-radius:10px;background:linear-gradient(135deg,#0891b2,#06b6d4);\">"
            + "<a href=\"" + ctaLink + "\" target=\"_blank\" style=\"display:inline-block;padding:14px 36px;font-size:15px;font-weight:600;color:#ffffff;text-decoration:none;letter-spacing:0.3px;\">"
            + ctaLabel
            + "</a></td></tr></table>"
            // fallback link
            + "<p style=\"margin:28px 0 0;font-size:12px;color:#94a3b8;line-height:1.6;word-break:break-all;\">"
            + "Si le bouton ne fonctionne pas, copiez et collez ce lien dans votre navigateur :<br/>"
            + "<a href=\"" + ctaLink + "\" style=\"color:#0891b2;\">" + ctaLink + "</a></p>"
            + "</td></tr>"
            // footer
            + "<tr><td style=\"padding:24px 40px;background-color:#f8fafc;border-top:1px solid #e2e8f0;\">"
            + "<p style=\"margin:0 0 6px;font-size:12px;color:#94a3b8;text-align:center;\">" + footer + "</p>"
            + "<p style=\"margin:0;font-size:12px;color:#cbd5e1;text-align:center;\">© 2026 Aquapulse — Tous droits réservés.</p>"
            + "</td></tr>"
            + "</table>"
            + "</td></tr></table></body></html>";
    }

    // ── Sender helper ─────────────────────────────────────────────────
    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
