package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Pelicula;
import com.reelme.reelmespringboot.repository.PeliculaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PeliculaService {
    @Autowired
    private PeliculaRepository peliculaRepository;

    @Transactional
    public Pelicula save(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }

    public Optional<Pelicula> findById(String id) {
        System.out.println("id: " + id);
        System.out.println("peliculaRepository.findById(id): " + peliculaRepository.findById(id));
        return peliculaRepository.findById(id);
    }

}
