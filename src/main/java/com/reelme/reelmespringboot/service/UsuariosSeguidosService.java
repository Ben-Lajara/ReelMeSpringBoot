package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.model.UsuariosSeguidos;
import com.reelme.reelmespringboot.repository.UsuariosSeguidosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuariosSeguidosService {
    @Autowired
    private UsuariosSeguidosRepository usuariosSeguidosRepository;

    @Query("SELECT us.usuarioSeguido FROM usuarios_seguidos us WHERE us.nombreUsuario = :usuario")
    public List<UsuariosSeguidos> findByNombreUsuario(@Param("usuario") Usuario usuario){
        return usuariosSeguidosRepository.findByNombreUsuario(usuario);
    };

    public List<Object> getUsuariosSeguidosByNombreUsuario(Usuario nombreUsuario){
        return usuariosSeguidosRepository.getUsuariosSeguidosByNombreUsuario(nombreUsuario);
    }

    public List<UsuariosSeguidos> findByUsuarioSeguido(Usuario usuarioSeguido){
        return usuariosSeguidosRepository.findByUsuarioSeguido(usuarioSeguido);
    }

    public void save(UsuariosSeguidos usuariosSeguidos){
        usuariosSeguidosRepository.save(usuariosSeguidos);
    }


    public void delete(UsuariosSeguidos usuariosSeguidos) {
        usuariosSeguidosRepository.delete(usuariosSeguidos);
    }

    public int countByNombreUsuario(Usuario nombreUsuario){
        return usuariosSeguidosRepository.countByNombreUsuario(nombreUsuario);
    }

    public int countByUsuarioSeguido(Usuario usuarioSeguido){
        return usuariosSeguidosRepository.countByUsuarioSeguido(usuarioSeguido);
    }
}
