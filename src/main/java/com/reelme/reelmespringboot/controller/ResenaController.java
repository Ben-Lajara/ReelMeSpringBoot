package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.dto.EntradaDiarioDTO;
import com.reelme.reelmespringboot.model.Pelicula;
import com.reelme.reelmespringboot.model.Resena;
import com.reelme.reelmespringboot.model.Revisionado;
import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.service.*;
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
        System.out.println("Se ha accedido al post");
        try {
            String fechaString = (String) parametros.get("fecha");
            System.out.println("fechaString: " + fechaString);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formatter.parse(fechaString);
            System.out.println("fecha: " + fecha);
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
    public ResponseEntity<?> getReview(@RequestParam String usuario, @RequestParam String idPelicula, @RequestHeader("Authorization") String token){
        System.out.println("Se ha accedido al get");
        System.out.println("Header: "+ token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            System.out.println("Token: " + token);
        }

        System.out.println("Usuario Token: " + jwtTokenProviderService.getUsernameFromJwt(token));

        if(!usuario.equals(jwtTokenProviderService.getUsernameFromJwt(token))){
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Unauthorized");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }else{
            Usuario usuarioFound = usuarioService.findByName(usuario);
            System.out.println("usuario: " + usuarioFound);
            Optional<Pelicula> peliculaId = peliculaService.findById(idPelicula);
            System.out.println("idPelicula: " + peliculaId);
            try {
                Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioFound, peliculaId);
                System.out.println("resena: " + resena);
                return new ResponseEntity<>(resena, HttpStatus.OK);
            } catch (Exception e) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }

    /*@PutMapping("/review")
    public ResponseEntity<?> editReview(@RequestBody Resena updatedResena) {
        System.out.println("updatedResena: " + updatedResena);
        System.out.println("Se ha accedido al put");
        try {
            System.out.println("nomUsuario: " + updatedResena.getNomUsuario().getNombre());
            System.out.println("idPelicula: " + updatedResena.getIdPelicula());
            Resena existingResena = resenaService.findByUsuarioAndIdPelicula(updatedResena.getNomUsuario(), Optional.ofNullable(updatedResena.getIdPelicula()));
            System.out.println("existingResena: " + existingResena);
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
    }*/

    @PutMapping("/review")
    public ResponseEntity<?> updateReview(@RequestBody Map<String, Object> parametros){
        System.out.println("Se ha accedido al put");
        try {
            String fechaString = (String) parametros.get("fecha");
            System.out.println("fechaString: " + fechaString);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = formatter.parse(fechaString);
            System.out.println("fecha: " + fecha);
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
            List<Map<String, Object>> revisionados = (List<Map<String, Object>>) parametros.get("revisionados");
            List<Map<String, Object>> revisionadosNuevos = (List<Map<String, Object>>) parametros.get("revisionadosNuevos");

            Usuario nomUsuario = usuarioService.findByName(usuario);
            System.out.println("nomUsuario: " + nomUsuario);
            Optional<Pelicula> peliculaId = peliculaService.findById(idPelicula);
            System.out.println("pelicula: " + peliculaId);
            Resena existingResena = resenaService.findByUsuarioAndIdPelicula(nomUsuario, Optional.ofNullable(peliculaId.get()));
            if (existingResena != null) {
                existingResena.setFecha(fecha);
                existingResena.setCalificacion(calificacion);
                existingResena.setComentario(comentario);
                existingResena.setGustado(gustado);
                existingResena.setIdPelicula(peliculaId.get());
                existingResena.setNomUsuario(nomUsuario);

                for (Map<String, Object> revisionadoMap : revisionados) {
                    Integer id = (Integer) revisionadoMap.get("id");
                    Revisionado existingRevisionado = revisionadoService.findById(id);
                    if (existingRevisionado != null) {
                        String fechaRevisionadoString = (String) revisionadoMap.get("fechaRevisionado");
                        SimpleDateFormat formatterRevisionado = new SimpleDateFormat("yyyy-MM-dd");
                        Date fechaRevisionado = formatterRevisionado.parse(fechaRevisionadoString);
                        existingRevisionado.setFechaRevisionado(fechaRevisionado);

                        String comentarioRevisionado = (String) revisionadoMap.get("comentarioRevisionado");
                        if (comentarioRevisionado != null) {
                            existingRevisionado.setComentarioRevisionado(comentarioRevisionado);
                        }

                        revisionadoService.save(existingRevisionado);
                    }
                }

                for(Map<String, Object> revisionadoNuevo : revisionadosNuevos){
                    String fechaRevisionadoString = (String) revisionadoNuevo.get("fechaRevisionado");
                    SimpleDateFormat formatterRevisionado = new SimpleDateFormat("yyyy-MM-dd");
                    Date fechaRevisionado = formatterRevisionado.parse(fechaRevisionadoString);
                    String comentarioRevisionado = (String) revisionadoNuevo.get("comentarioRevisionado");
                    Revisionado revisionado = new Revisionado(fechaRevisionado,  existingResena, comentarioRevisionado);
                    revisionadoService.save(revisionado);
                }


                resenaService.save(existingResena);
                usuarioService.updateRango(nomUsuario);

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
    public ResponseEntity<List<EntradaDiarioDTO>> getResenasByUsuario(@PathVariable String usuario) {
        Usuario usuarioFound = usuarioService.findByName(usuario);
        if (usuarioFound == null) {
            return ResponseEntity.notFound().build();
        } else {
            List<Resena> resenas = resenaService.findByUsuario(usuarioFound);
            if (resenas.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
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
            resenaPublica.put("revisionados", resena.getRevisionados());
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

    @GetMapping("/reviewed/lastactivity/{usuario}")
    public ResponseEntity<List<Resena>> getLastActivity(@PathVariable String usuario) {
        Usuario usuarioFound = usuarioService.findByName(usuario);
        if (usuarioFound == null) {
            return ResponseEntity.notFound().build();
        } else {
            List<Resena> resenas = resenaService.findTop4ByUsuarioOrderByLatestActivityDesc(usuarioFound);
            if (resenas.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(resenas);
            }
        }
    }

    @GetMapping("/reviewed")
    public ResponseEntity<Resena> getResena(@RequestParam int id) {
        Resena resena = resenaService.findById(id);
        if (resena == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(resena);
        }
    }

    @GetMapping("/reviewed/top4")
    public ResponseEntity<List<Pelicula>> getTop4PeliculasWithMostResenas() {
        List<Pelicula> topPeliculas = resenaService.findTop4PeliculasWithMostResenas();
        return new ResponseEntity<>(topPeliculas, HttpStatus.OK);
    }
}
