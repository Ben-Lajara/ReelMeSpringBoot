package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.model.Resena;
import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.model.UsuariosSeguidos;
import com.reelme.reelmespringboot.repository.UsuarioRepository;
import com.reelme.reelmespringboot.service.ResenaService;
import com.reelme.reelmespringboot.service.UsuarioService;
import com.reelme.reelmespringboot.service.UsuariosSeguidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class UsuariosSeguidosController {
    @Autowired
    private UsuariosSeguidosService usuariosSeguidosService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ResenaService resenaService;

    @GetMapping("/usuarios/seguidosPor/{nombre}")
    public ResponseEntity<?> getSeguidos(@PathVariable String nombre) {
        try {
            Usuario usuarioFound = usuarioService.findByName(nombre);
            if (usuarioFound != null) {
                List<UsuariosSeguidos> seguidos = usuariosSeguidosService.findByNombreUsuario(usuarioFound);
                return new ResponseEntity<>(seguidos, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuarios/seguidoresDe/{nombre}")
    public ResponseEntity<?> getSeguidores(@PathVariable String nombre) {
        try {
            Usuario usuarioFound = usuarioService.findByName(nombre);
            if (usuarioFound != null) {
                List<UsuariosSeguidos> seguidores = usuariosSeguidosService.findByUsuarioSeguido(usuarioFound);
                return new ResponseEntity<>(seguidores, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/usuario/seguir")
    public ResponseEntity<?> seguir(@RequestBody Map<String, Object> parametros){
        try {
            String usuario = (String) parametros.get("nombreUsuario");
            String nombreUsuarioSeguido = (String) parametros.get("usuarioSeguido");
            Usuario nombreUsuario = usuarioService.findByName(usuario);
            Usuario usuarioSeguido = usuarioService.findByName(nombreUsuarioSeguido);
            if (nombreUsuario != null && usuarioSeguido != null) {
                UsuariosSeguidos usuariosSeguidos = new UsuariosSeguidos(nombreUsuario, usuarioSeguido);
                usuariosSeguidosService.save(usuariosSeguidos);
                return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/usuario/dejarDeSeguir")
    public ResponseEntity<?> dejarDeSeguir(@RequestBody Map<String, Object> parametros){
        try {
            String usuario = (String) parametros.get("nombreUsuario");
            String nombreUsuarioSeguido = (String) parametros.get("usuarioSeguido");
            Usuario nombreUsuario = usuarioService.findByName(usuario);
            Usuario usuarioSeguido = usuarioService.findByName(nombreUsuarioSeguido);
            if (nombreUsuario != null && usuarioSeguido != null) {
                UsuariosSeguidos usuariosSeguidos = new UsuariosSeguidos(nombreUsuario, usuarioSeguido);
                usuariosSeguidosService.delete(usuariosSeguidos);
                return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/resenasSeguidosPor/{nombre}/{idPelicula}")
    public ResponseEntity<?> getResenasSeguidos(@PathVariable String nombre, @PathVariable String idPelicula) {
        try {
            Usuario usuarioFound = usuarioService.findByName(nombre);
            if (usuarioFound != null) {
                List<UsuariosSeguidos> seguidos = usuariosSeguidosService.findByNombreUsuario(usuarioFound);
                List<Map<String, Object>> resenasSeguidos = new ArrayList<>();
                for (UsuariosSeguidos seguido : seguidos) {
                    Usuario usuario = usuarioService.findByName(seguido.getUsuarioSeguido().getNombre());
                    List<Resena> resenasUsuario = resenaService.findByUsuario(usuario);
                    Resena resena = resenasUsuario.stream()
                            .filter(r -> r.getIdPelicula().getId().equals(idPelicula))
                            .findFirst()
                            .orElse(null);
                    if (resena != null) {
                        Map<String, Object> resenaMap = new HashMap<>();
                        resenaMap.put("usuario", usuario.getNombre());
                        resenaMap.put("perfil", usuario.getPerfil());
                        resenaMap.put("color", usuario.getColor());
                        resenaMap.put("calificacion", resena.getCalificacion());
                        resenaMap.put("comentario", resena.getComentario());
                        resenaMap.put("gustado", resena.isGustado());
                        resenasSeguidos.add(resenaMap);
                    }
                }
                return new ResponseEntity<>(resenasSeguidos, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
