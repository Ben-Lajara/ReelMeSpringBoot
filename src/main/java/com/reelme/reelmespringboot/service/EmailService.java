package com.reelme.reelmespringboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

}
