package com.reelme.reelmespringboot.repository;

import com.reelme.reelmespringboot.model.Pelicula;
import com.reelme.reelmespringboot.model.Resena;
import com.reelme.reelmespringboot.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResenaRepository extends CrudRepository<Resena, Integer> {

    Resena findByNomUsuarioAndIdPelicula(Usuario usuario, Optional<Pelicula> idPelicula);

    @Query("SELECT r FROM Resena r WHERE r.nomUsuario = :usuario")
    List<Resena> findByNomUsuario(@Param("usuario") Usuario usuario);

    List<Resena> findByIdPelicula(Optional<Pelicula> idPelicula);

    int countByNomUsuario(Usuario usuario);

    List<Resena> findTop4ByNomUsuarioOrderByFechaDesc(Usuario usuario);


}
