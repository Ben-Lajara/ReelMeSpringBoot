package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.model.*;
import com.reelme.reelmespringboot.repository.UsuariosSeguidosRepository;
import com.reelme.reelmespringboot.service.ResenaService;
import com.reelme.reelmespringboot.service.UsuarioService;
import com.reelme.reelmespringboot.service.UsuariosSeguidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuariosSeguidosService usuariosSeguidosService;

    @Autowired
    private ResenaService resenaService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        usuarioService.save(usuario);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Usuario existingUser = usuarioService.findByName(usuario.getNombre());
        if (existingUser != null && existingUser.getPword().equals(usuario.getPword())) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/seguidos/{nombre}")
    public ResponseEntity<List<Map<String, Object>>> getSeguidos(@PathVariable String nombre) {
        Usuario usuarioFound = usuarioService.findByName(nombre);
        List<UsuariosSeguidos> seguidos = usuariosSeguidosService.findByNombreUsuario(usuarioFound);
        List<Map<String, Object>> seguidosCompletos = new ArrayList<>();
        for (UsuariosSeguidos seguido : seguidos) {
            Usuario usuario = usuarioService.findByName(seguido.getUsuarioSeguido().getNombre());
            List<Resena> resenasUsuario = resenaService.findByUsuario(usuario);
            Resena resena = resenasUsuario.stream()
                    .sorted(Comparator.comparing(Resena::getFecha).reversed())
                    .findFirst()
                    .orElse(null);
            if(resena != null){
                Pelicula pelicula = resena.getIdPelicula();
                Map<String, Object> seguidoCompleto = new HashMap<>();
                seguidoCompleto.put("usuarioSeguido", usuario.getNombre());
                seguidoCompleto.put("idPelicula", pelicula.getId());
                seguidoCompleto.put("titulo", pelicula.getTitulo());
                seguidoCompleto.put("year", pelicula.getYear());
                seguidoCompleto.put("foto", pelicula.getFoto());
                seguidoCompleto.put("fecha", resena.getFecha());
                seguidoCompleto.put("calificacion", resena.getCalificacion());
                seguidoCompleto.put("comentario", resena.getComentario());
                seguidoCompleto.put("gustado", resena.isGustado());
                seguidosCompletos.add(seguidoCompleto);
            }

        }
        for(Map<String, Object> seguidoCompleto : seguidosCompletos) {
            System.out.println(seguidoCompleto);
        }

        return ResponseEntity.ok(seguidosCompletos);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> getUsuarios(){
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/about/{usuario}")
    public ResponseEntity<Map<String, Object>> getAbout(@PathVariable String usuario){
        Usuario usuarioFound = usuarioService.findByName(usuario);
        if(usuarioFound == null){
            return ResponseEntity.notFound().build();
        }else{
            Map<String, Object> about = new HashMap<>();
            about.put("nombre", usuarioFound.getNombre());
            about.put("vistas", resenaService.countByUsuario(usuarioFound));
            about.put("seguidores", usuariosSeguidosService.countByUsuarioSeguido(usuarioFound));
            about.put("seguidos", usuariosSeguidosService.countByNombreUsuario(usuarioFound));
            return ResponseEntity.ok(about);
        }
    }

    @GetMapping("/usuarios/{nombre}")
    public ResponseEntity<List<Usuario>> getUsuariosLike(@PathVariable String nombre) {
        List<Usuario> usuariosLike = usuarioService.findByNombreContaining(nombre);
        if (usuariosLike.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(usuariosLike);
        }
    }

}
