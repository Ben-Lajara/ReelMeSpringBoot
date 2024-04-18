package com.reelme.reelmespringboot.repository;

import com.reelme.reelmespringboot.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsuarioRepository extends CrudRepository<Usuario, String> {
    Usuario findByNombre(String nombre);
    List<Usuario> findAll();

    List<Usuario> findByNombreLike(String nombre);
    List<Usuario> findByNombreContaining(String nombre);

    Usuario findByEmail(String email);
}
