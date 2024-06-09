package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.model.TokenPword;
import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.repository.TokenPwordRepository;
import com.reelme.reelmespringboot.service.EmailService;
import com.reelme.reelmespringboot.service.UsuarioService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TokenPwordController {
    @Autowired
    private TokenPwordRepository tokenPwordRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/usuario/restablecerPword")
    public ResponseEntity<?> restablecerPword(@RequestParam("email") String email) {
        String token = UUID.randomUUID().toString();
        TokenPword tokenPword = new TokenPword();
        tokenPword.setToken(token);
        tokenPword.setEmail(email);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);
        tokenPword.setFechaExpiracion(cal.getTime());
        tokenPwordRepository.save(tokenPword);
        emailService.sendSimpleMessage(email, token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Se ha enviado un correo con las instrucciones para restablecer la contraseña");
        return ResponseEntity.ok(response);
    }


    private void sendEmail(String email, String token) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(email);
        mensaje.setSubject("Restablece tu contraseña de ReelMe");
        mensaje.setText("Para restablecer tu contraseña, haz clic en el siguiente enlace: http://localhost:8080/restablecerPword?token=" + token);
        this.javaMailSender.send(mensaje);
    }



    @GetMapping("/usuario/tokenPword")
    private ResponseEntity<Usuario> getUsuario(@RequestParam("token") String token){
        TokenPword tokenPword = tokenPwordRepository.findByToken(token);
        if (tokenPword == null) {
            return ResponseEntity.badRequest().build();
        }else{
            Calendar cal = Calendar.getInstance();
            if (cal.getTime().after(tokenPword.getFechaExpiracion())) {
                return ResponseEntity.badRequest().build();
            }
            Usuario usuario = usuarioService.findByEmail(tokenPword.getEmail());
            usuario.setEmail(tokenPword.getEmail());
            return ResponseEntity.ok(usuario);
        }

    }
}
