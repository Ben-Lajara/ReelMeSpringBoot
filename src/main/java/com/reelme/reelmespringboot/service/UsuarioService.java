package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

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
}
