package com.reelme.reelmespringboot.repository;

import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.model.UsuariosSeguidos;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsuariosSeguidosRepository extends CrudRepository<UsuariosSeguidos, String> {
    List<UsuariosSeguidos> findByNombreUsuario(Usuario nombreUsuario);

    List<Object> getUsuariosSeguidosByNombreUsuario(Usuario nombreUsuario);

    List<UsuariosSeguidos> findByUsuarioSeguido(Usuario usuarioSeguido);

    int countByNombreUsuario(Usuario nombreUsuario);
    int countByUsuarioSeguido(Usuario usuarioSeguido);

}
