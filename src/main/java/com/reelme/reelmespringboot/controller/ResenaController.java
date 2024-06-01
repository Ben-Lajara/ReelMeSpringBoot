package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.dto.EntradaDiarioDTO;
import com.reelme.reelmespringboot.model.Pelicula;
import com.reelme.reelmespringboot.model.Resena;
import com.reelme.reelmespringboot.model.Revisionado;
import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.service.*;
import org.apache.coyote.Response;
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

    @Autowired
    private JwtTokenProviderService jwtTokenProviderService;

    @Autowired
    private RevisionadoService revisionadoService;

    @PostMapping("/review")
    public ResponseEntity<?> review(@RequestBody Map<String, Object> parametros){
        try {
            // Comprueba que los campos requeridos están presentes
            String[] requiredFields = {"fecha", "calificacion", "comentario", "gustado", "id_pelicula", "usuario", "titulo", "year", "foto"};
            for (String field : requiredFields) {
                if (!parametros.containsKey(field)) {
                    return new ResponseEntity<>("Missing field: " + field, HttpStatus.BAD_REQUEST);
                }
            }

            // Parseo y validación de los campos
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formatter.parse((String) parametros.get("fecha"));
            float calificacion;
            if (parametros.get("calificacion") instanceof Integer) {
                calificacion = (float) (int) parametros.get("calificacion");
            } else if (parametros.get("calificacion") instanceof Double) {
                calificacion = (float) (double) parametros.get("calificacion");
            } else {
                return new ResponseEntity<>("Invalid field: calificacion", HttpStatus.BAD_REQUEST);
            }
            if (calificacion < 0 || calificacion > 5) {
                return new ResponseEntity<>("Invalid field: calificacion", HttpStatus.BAD_REQUEST);
            }
            String comentario = (String) parametros.get("comentario");
            boolean gustado = (Boolean) parametros.get("gustado");
            boolean spoiler = (Boolean) parametros.get("spoiler");
            String idPelicula = (String) parametros.get("id_pelicula");
            String usuario = (String) parametros.get("usuario");
            String titulo = (String) parametros.get("titulo");
            String year = (String) parametros.get("year");
            String foto = (String) parametros.get("foto");
            List<Map<String, Object>> revisionadosNuevos = (List<Map<String, Object>>) parametros.get("revisionadosNuevos");

            // Comprueba que el usuario y la película existen
            Usuario nomUsuario = usuarioService.findByName(usuario);
            if (nomUsuario == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            Pelicula pelicula = peliculaService.findById(idPelicula).orElse(null);
            if (pelicula == null) {
                // Si la película no existe, la crea con los datos proporcionados
                pelicula = new Pelicula(idPelicula, titulo, year, foto);
                peliculaService.save(pelicula);
            }

            // Comprueba que la reseña no exista
            Resena existingResena = resenaService.findByUsuarioAndIdPelicula(nomUsuario, Optional.of(pelicula));
            if (existingResena != null) {
                return new ResponseEntity<>("Review already exists", HttpStatus.CONFLICT);
            }

            // Crea y garda la reseña
            Resena resena = new Resena(fecha, calificacion, comentario, gustado, spoiler, pelicula, nomUsuario);
            resenaService.save(resena);
            for(Map<String, Object> revisionadoNuevo : revisionadosNuevos){
                Date fechaRevisionado = formatter.parse((String) revisionadoNuevo.get("fechaRevisionado"));
                String comentarioRevisionado = (String) revisionadoNuevo.get("comentarioRevisionado");
                Revisionado revisionado = new Revisionado(fechaRevisionado,  resena, comentarioRevisionado);
                revisionadoService.save(revisionado);
            }
            usuarioService.updateRango(nomUsuario);

            return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("status", "error", "message", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/review")
    public ResponseEntity<?> getReview(@RequestParam String usuario, @RequestParam String idPelicula, @RequestHeader("Authorization") String token){
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            if(!usuario.equals(jwtTokenProviderService.getUsernameFromJwt(token))){
                return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
            }else{
                Usuario usuarioFound = usuarioService.findByName(usuario);
                Optional<Pelicula> peliculaId = peliculaService.findById(idPelicula);
                if (usuarioFound == null || peliculaId.isEmpty()) {
                    //return new ResponseEntity<>("Usuario or Pelicula not found", HttpStatus.NOT_FOUND);
                }
                Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioFound, peliculaId);
                if (resena == null) {
                    return new ResponseEntity<>("Resena not found", HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(resena, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @PutMapping("/review")
    public ResponseEntity<?> updateReview(@RequestBody Map<String, Object> parametros){
        try {
            // Comprueba los campos obligatorios
            String[] requiredFields = {"fecha", "calificacion", "comentario", "gustado", "id_pelicula", "usuario", "revisionados"};
            for (String field : requiredFields) {
                if (!parametros.containsKey(field)) {
                    return new ResponseEntity<>("Missing field: " + field, HttpStatus.BAD_REQUEST);
                }
            }

            // Parsea y valida los campos
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formatter.parse((String) parametros.get("fecha"));
            float calificacion;
            if (parametros.get("calificacion") instanceof Integer) {
                calificacion = (float) (int) parametros.get("calificacion");
            } else if (parametros.get("calificacion") instanceof Double) {
                calificacion = (float) (double) parametros.get("calificacion");
            } else {
                return new ResponseEntity<>("Invalid field: calificacion", HttpStatus.BAD_REQUEST);
            }
            if (calificacion < 0 || calificacion > 5) {
                return new ResponseEntity<>("Invalid field: calificacion", HttpStatus.BAD_REQUEST);
            }
            String comentario = (String) parametros.get("comentario");
            boolean gustado = (Boolean) parametros.get("gustado");
            boolean spoiler = (Boolean) parametros.get("spoiler");
            String idPelicula = (String) parametros.get("id_pelicula");
            String usuario = (String) parametros.get("usuario");
            List<Map<String, Object>> revisionados = (List<Map<String, Object>>) parametros.get("revisionados");
            List<Map<String, Object>> revisionadosNuevos = (List<Map<String, Object>>) parametros.get("revisionadosNuevos");

            // Comprueba que el usuario y la película existen
            Usuario nomUsuario = usuarioService.findByName(usuario);
            if (nomUsuario == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            Optional<Pelicula> peliculaId = peliculaService.findById(idPelicula);
            if (peliculaId.isEmpty()) {
                return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);
            }

            // Se asegura de que la reseña existe
            Resena existingResena = resenaService.findByUsuarioAndIdPelicula(nomUsuario, peliculaId);
            if (existingResena == null) {
                return new ResponseEntity<>("Review not found", HttpStatus.NOT_FOUND);
            }

            // Actualiza la reseña
            existingResena.setFecha(fecha);
            existingResena.setCalificacion(calificacion);
            existingResena.setComentario(comentario);
            existingResena.setGustado(gustado);
            existingResena.setSpoiler(spoiler);
            existingResena.setIdPelicula(peliculaId.get());
            existingResena.setNomUsuario(nomUsuario);

            for (Map<String, Object> revisionadoMap : revisionados) {
                Integer id = (Integer) revisionadoMap.get("id");
                Revisionado existingRevisionado = revisionadoService.findById(id);
                if (existingRevisionado != null) {
                    Date fechaRevisionado = formatter.parse((String) revisionadoMap.get("fechaRevisionado"));
                    existingRevisionado.setFechaRevisionado(fechaRevisionado);
                    String comentarioRevisionado = (String) revisionadoMap.get("comentarioRevisionado");
                    existingRevisionado.setComentarioRevisionado(comentarioRevisionado);
                    revisionadoService.save(existingRevisionado);
                }
            }

            for(Map<String, Object> revisionadoNuevo : revisionadosNuevos){
                Date fechaRevisionado = formatter.parse((String) revisionadoNuevo.get("fechaRevisionado"));
                String comentarioRevisionado = (String) revisionadoNuevo.get("comentarioRevisionado");
                Revisionado revisionado = new Revisionado(fechaRevisionado,  existingResena, comentarioRevisionado);
                revisionadoService.save(revisionado);
            }

            resenaService.save(existingResena);
            usuarioService.updateRango(nomUsuario);

            return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("status", "error", "message", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/review")
    public ResponseEntity<?> deleteResena(@RequestParam String usuario, @RequestParam String idPelicula, @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if(!usuario.equals(jwtTokenProviderService.getUsernameFromJwt(token))){
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }else{
            Usuario usuarioFound = usuarioService.findByName(usuario);
            if (usuarioFound == null) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            Optional<Pelicula> peliculaId = peliculaService.findById(idPelicula);
            if (peliculaId.isEmpty()) {
                return new ResponseEntity<>("Movie not found", HttpStatus.NOT_FOUND);
            }
            try {
                Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioFound, peliculaId);
                if (resena == null) {
                    return new ResponseEntity<>("Review not found", HttpStatus.NOT_FOUND);
                }
                resenaService.delete(resena);
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
    }

    @GetMapping("/diario/{usuario}")
    public ResponseEntity<List<EntradaDiarioDTO>> getDiarioByUsuario(@PathVariable String usuario) {
        Usuario usuarioFound = usuarioService.findByName(usuario);
        if (usuarioFound == null) {
            return ResponseEntity.notFound().build();
        } else {
            List<Resena> resenas = resenaService.findByUsuario(usuarioFound);

                List<EntradaDiarioDTO> entradasDiario = new ArrayList<>();

                for (Resena resena : resenas) {
                    // Añade las reseñas
                    EntradaDiarioDTO entrada = new EntradaDiarioDTO(
                            resena.getFecha(),
                            resena.getCalificacion(),
                            resena.getComentario(),
                            resena.isGustado(),
                            resena.getIdPelicula(),
                            resena.getNomUsuario(),
                            resena.isDenunciada(),
                            false // esRevisionado
                    );
                    entradasDiario.add(entrada);

                    // Añade los revisionados
                    if (resena.getRevisionados() != null) {
                        for (Revisionado revisionado : resena.getRevisionados()) {
                            EntradaDiarioDTO revisionadoentrada = new EntradaDiarioDTO(
                                    revisionado.getFechaRevisionado(),
                                    resena.getCalificacion(),
                                    revisionado.getComentarioRevisionado(),
                                    resena.isGustado(),
                                    resena.getIdPelicula(),
                                    resena.getNomUsuario(),
                                    resena.isDenunciada(),
                                    true // esRevisionado
                            );
                            entradasDiario.add(revisionadoentrada);
                        }
                    }
                }
                entradasDiario.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));

                return ResponseEntity.ok(entradasDiario);
            
        }

    }

    @GetMapping("/reviewed/{usuario}/{idPelicula}")
    public ResponseEntity<Map<String, Object>> getResenaPublica(@PathVariable String usuario, @PathVariable String idPelicula) {
        Usuario usuarioFound = usuarioService.findByName(usuario);
        if (usuarioFound == null) {
            return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
        }
        Optional<Pelicula> peliculaId = peliculaService.findById(idPelicula);
        if (peliculaId.isEmpty()) {
            //return new ResponseEntity<>(Collections.singletonMap("error", "Movie not found"), HttpStatus.NOT_FOUND);
        }

        Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioFound, peliculaId);
        if(resena == null){
            return new ResponseEntity<>(Collections.singletonMap("error", "Review not found"), HttpStatus.NOT_FOUND);
        }
        Map<String, Object> resenaPublica = new HashMap<>();
        resenaPublica.put("fecha", resena.getFecha());
        resenaPublica.put("calificacion", resena.getCalificacion());
        resenaPublica.put("comentario", resena.getComentario());
        resenaPublica.put("gustado", resena.isGustado());
        resenaPublica.put("spoiler", resena.isSpoiler());
        resenaPublica.put("usuario", usuarioFound.getNombre());
        resenaPublica.put("idPelicula", peliculaId.get().getId());
        resenaPublica.put("titulo", peliculaId.get().getTitulo());
        resenaPublica.put("year", peliculaId.get().getYear());
        resenaPublica.put("foto", peliculaId.get().getFoto());
        resenaPublica.put("revisionados", resena.getRevisionados());
        return ResponseEntity.ok(resenaPublica);
    }

    @GetMapping("/reviewed/{idPelicula}")
    public ResponseEntity<?> getResenasPublicas(@PathVariable String idPelicula) {
        Optional<Pelicula> peliculaId = peliculaService.findById(idPelicula);
        //if (peliculaId.isEmpty()) {
            //return new ResponseEntity<>(Collections.singletonMap("message", "No reviews found"), HttpStatus.NO_CONTENT);
        //} else {
            List<Resena> resenas = resenaService.findByIdPelicula(peliculaId);
            return ResponseEntity.ok(resenas);

        //}
    }

    @GetMapping("/{usuario}/reviewed")
    public ResponseEntity<List<Resena>> getResenasByUsuario(@PathVariable String usuario) {
        Usuario usuarioFound = usuarioService.findByName(usuario);
        if (usuarioFound == null) {
            return ResponseEntity.notFound().build();
        } else {
            List<Resena> resenas = resenaService.findByUsuario(usuarioFound);
            return ResponseEntity.ok(resenas);
        }
    }

    @GetMapping("/reviewed/lastactivity/{usuario}")
    public ResponseEntity<List<Resena>> getLastActivity(@PathVariable String usuario) {
        Usuario usuarioFound = usuarioService.findByName(usuario);
        if (usuarioFound == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            List<Resena> resenas = resenaService.findTop4ByUsuarioOrderByLatestActivityDesc(usuarioFound);
            return ResponseEntity.ok(resenas);
        }
    }

    @GetMapping("/reviewed")
    public ResponseEntity<?> getResena(@RequestParam int id) {
        Resena resena = resenaService.findById(id);
        if (resena == null) {
            return new ResponseEntity<>(Collections.singletonMap("message", "No review found"), HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.ok(resena);
        }
    }

    @GetMapping("/reviewed/top4")
    public ResponseEntity<?> getTop4PeliculasWithMostResenas() {
        List<Pelicula> topPeliculas = resenaService.findTop4PeliculasWithMostResenas();
        if (topPeliculas.isEmpty()){
            return new ResponseEntity<>(Collections.singletonMap("message", "No movies with reviews found"), HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(topPeliculas, HttpStatus.OK);
        }
    }
}
