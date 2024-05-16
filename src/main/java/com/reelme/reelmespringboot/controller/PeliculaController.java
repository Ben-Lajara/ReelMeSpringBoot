package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.model.Pelicula;
import com.reelme.reelmespringboot.repository.PeliculaRepository;
import com.reelme.reelmespringboot.service.PeliculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PeliculaController {
    @Autowired
    private PeliculaService peliculaService;

    @GetMapping("/pelicula/{id}")
    public ResponseEntity<Pelicula> getPeliculaById(@PathVariable String id) {
        Optional<Pelicula> pelicula = peliculaService.findById(id);
        if (pelicula.isPresent()) {
            return ResponseEntity.ok(pelicula.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
