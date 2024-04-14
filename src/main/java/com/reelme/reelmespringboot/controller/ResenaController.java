package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.model.Pelicula;
import com.reelme.reelmespringboot.model.Resena;
import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.service.PeliculaService;
import com.reelme.reelmespringboot.service.ResenaService;
import com.reelme.reelmespringboot.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.reelme.reelmespringboot.repository.ResenaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ResenaController {
    @Autowired
    private ResenaService resenaService;

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/review")
    public ResponseEntity<?> review(@RequestBody Map<String, Object> parametros){
        System.out.println("Se ha accedido al post");
        try {
            String fechaString = (String) parametros.get("fecha");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formatter.parse(fechaString);
            float calificacion = 0.0f;
            if (parametros.get("calificacion") instanceof Integer) {
                int calificacionInt = (Integer) parametros.get("calificacion");
                calificacion = (float) calificacionInt;
            } else if (parametros.get("calificacion") instanceof Double) {
                double calificacionDouble = (Double) parametros.get("calificacion");
                calificacion = (float) calificacionDouble;
            }
            String comentario = (String) parametros.get("comentario");
            boolean gustado = (Boolean) parametros.get("gustado");
            String idPelicula = (String) parametros.get("id_pelicula");
            String usuario = (String) parametros.get("usuario");
            String titulo = (String) parametros.get("titulo");
            String year = (String) parametros.get("year");
            String foto = (String) parametros.get("foto");

            Usuario nomUsuario = usuarioService.findByName(usuario);
            System.out.println("nomUsuario: " + nomUsuario);
            Pelicula pelicula = new Pelicula(idPelicula, titulo, year, foto);
            System.out.println("pelicula: " + pelicula);
            peliculaService.save(pelicula);
            Resena resena = new Resena(fecha, calificacion, comentario, gustado, pelicula, nomUsuario);
            System.out.println("resena: " + resena);

            resenaService.save(resena);

            usuarioService.updateRango(nomUsuario);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/review")
    public ResponseEntity<?> getReview(@RequestParam String  usuario, @RequestParam String idPelicula) {
        System.out.println("Se ha accedido al get");
        Usuario usuarioFound = usuarioService.findByName(usuario);
        System.out.println("usuario: " + usuarioFound);
        Optional<Pelicula> peliculaId = peliculaService.findById(idPelicula);
        System.out.println("idPelicula: " + peliculaId);
        try {
            Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioFound, peliculaId);
            return new ResponseEntity<>(resena, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/review")
    public ResponseEntity<?> editReview(@RequestBody Resena updatedResena) {
        System.out.println("Se ha accedido al put");
        try {
            Resena existingResena = resenaService.findByUsuarioAndIdPelicula(updatedResena.getNomUsuario(), Optional.ofNullable(updatedResena.getIdPelicula()));
            if (existingResena != null) {
                existingResena.setFecha(updatedResena.getFecha());
                existingResena.setCalificacion(updatedResena.getCalificacion());
                existingResena.setComentario(updatedResena.getComentario());
                existingResena.setGustado(updatedResena.isGustado());
                resenaService.save(existingResena);
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Review not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/diario/{usuario}")
    public ResponseEntity<List<Resena>> getResenasByUsuario(@PathVariable String usuario) {
        System.out.println("usuario: " + usuario);
        Usuario usuarioFound = usuarioService.findByName(usuario);
        System.out.println("usuarioFound: " + usuarioFound);
        if (usuarioFound == null) {
            System.out.println("usuarioFound is null");
            return ResponseEntity.notFound().build();
        }else{
            System.out.println("usuarioFound is not null");
            List<Resena> resenas = resenaService.findByUsuario(usuarioFound);
            System.out.println("resenas: " + resenas);
            if(resenas.isEmpty()) {
                return ResponseEntity.notFound().build();
            }else {
                return ResponseEntity.ok(resenas);
            }
        }

    }

    @GetMapping("/reviewed/{usuario}/{idPelicula}")
    public ResponseEntity<Map<String, Object>> getResenaPublica(@PathVariable String usuario, @PathVariable String idPelicula) {
        Usuario usuarioFound = usuarioService.findByName(usuario);
        Optional<Pelicula> peliculaId = peliculaService.findById(idPelicula);
        Map<String, Object> resenaPublica = new HashMap<>();
        if (usuarioFound == null || !peliculaId.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioFound, peliculaId);
            resenaPublica.put("fecha", resena.getFecha());
            resenaPublica.put("calificacion", resena.getCalificacion());
            resenaPublica.put("comentario", resena.getComentario());
            resenaPublica.put("gustado", resena.isGustado());
            resenaPublica.put("usuario", usuarioFound.getNombre());
            resenaPublica.put("idPelicula", peliculaId.get().getId());
            resenaPublica.put("titulo", peliculaId.get().getTitulo());
            resenaPublica.put("year", peliculaId.get().getYear());
            resenaPublica.put("foto", peliculaId.get().getFoto());
            if (resena == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(resenaPublica);
            }
        }
    }

    @GetMapping("/reviewed/{idPelicula}")
    public ResponseEntity<List<Resena>> getResenasPublicas(@PathVariable String idPelicula) {
        Optional<Pelicula> peliculaId = peliculaService.findById(idPelicula);
        if (!peliculaId.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            List<Resena> resenas = resenaService.findByIdPelicula(peliculaId);
            if (resenas.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(resenas);
            }
        }
    }

    @GetMapping("lastactivity/{usuario}")
    public ResponseEntity<List<Resena>> getLastActivity(@PathVariable String usuario) {
        Usuario usuarioFound = usuarioService.findByName(usuario);
        if (usuarioFound == null) {
            return ResponseEntity.notFound().build();
        } else {
            List<Resena> resenas = resenaService.findTop4ByUsuarioOrderByFechaDesc(usuarioFound);
            if (resenas.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(resenas);
            }
        }
    }

}
