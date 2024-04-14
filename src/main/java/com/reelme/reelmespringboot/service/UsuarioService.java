package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Rango;
import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
