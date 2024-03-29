package com.reelme.reelmespringboot.repository;

import com.reelme.reelmespringboot.model.Pelicula;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeliculaRepository extends CrudRepository<Pelicula, String> {
    Optional<Pelicula> findById(String id);

}
