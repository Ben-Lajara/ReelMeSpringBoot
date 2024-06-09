package com.reelme.reelmespringboot.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(String email, String token) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(email);
        mensaje.setFrom("reelmestaff@outlook.com");
        mensaje.setSubject("Restablece tu contraseña de ReelMe");
        mensaje.setText("Para restablecer tu contraseña, haz clic en el siguiente enlace: http://localhost:4200/reset/" + token);
        this.javaMailSender.send(mensaje);
    }

    public void sendResetPasswordEmail(String to, String token) {
        String subject = "Restablece tu contraseña de ReelMe";
        String body = "<html>" +
                "<body>" +
                "<h2>Restablece tu contraseña de ReelMe</h2>" +
                "<p>Hola,</p>" +
                "<p>Has solicitado restablecer tu contraseña. Haz clic en el siguiente enlace para restablecer tu contraseña:</p>" +
                "<p><a href=\"http://localhost:8080/restablecerPword?token=" + token + "\">Restablecer contraseña</a></p>" +
                "<p>Este enlace expirará en 1 hora.</p>" +
                "<p>Si no solicitaste este cambio, puedes ignorar este correo electrónico.</p>" +
                "<p>Saludos,<br/>El equipo de ReelMe</p>" +
                "</body>" +
                "</html>";

        sendHtmlEmail(to, subject, body);
    }

    private void sendHtmlEmail(String to, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Set to true for HTML

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error while sending email", e);
        }
    }

}
