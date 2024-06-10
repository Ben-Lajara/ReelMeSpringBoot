package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Rango;
import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ResenaService resenaService;

    public Usuario findByName(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> findByNombreLike(String nombre) {
        return usuarioRepository.findByNombreLike(nombre);
    }

    public List<Usuario> findByNombreContaining(String nombre) {
        return usuarioRepository.findByNombreContaining(nombre);
    }

    public void updateRango(Usuario usuario){
        int numResenas = resenaService.countByUsuario(usuario);
        Rango rango;

        if (numResenas >= 50) {
            rango = Rango.ORO;
        } else if (numResenas >= 25) {
            rango = Rango.PLATA;
        } else if (numResenas >= 10) {
            rango = Rango.BRONCE;
        } else {
            rango = null;
        }

        if (usuario.getRango() != rango) {
            usuario.setRango(rango);
            usuarioRepository.save(usuario);
        }
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void delete(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }

    public void vetar(String nombre, Date duracionVeto) {
        Usuario usuario = usuarioRepository.findByNombre(nombre);
        usuario.setVeto(duracionVeto);
        usuarioRepository.save(usuario);
    }

    /*@Scheduled(fixedRate = 600000)
    public void quitarVetos() {
        List<Usuario> usuarios = usuarioRepository.findAllByVetoIsNotNull();
        Date ahora = new Date();
        for (Usuario usuario : usuarios) {
            if (usuario.getVeto().before(ahora)) {
                usuario.setVeto(null);
                usuarioRepository.save(usuario);
            }
        }
    }*/
}
