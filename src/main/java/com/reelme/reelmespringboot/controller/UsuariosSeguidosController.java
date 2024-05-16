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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<UsuariosSeguidos> getSeguidos(@PathVariable String nombre) {
        Usuario usuarioFound = usuarioService.findByName(nombre);
        List<UsuariosSeguidos> seguidos = usuariosSeguidosService.findByNombreUsuario(usuarioFound);
        return new ResponseEntity(seguidos, HttpStatus.OK);
    }

    @GetMapping("/usuarios/seguidoresDe/{nombre}")
    public ResponseEntity<UsuariosSeguidos> getSeguidores(@PathVariable String nombre) {
        Usuario usuarioFound = usuarioService.findByName(nombre);
        List<UsuariosSeguidos> seguidores = usuariosSeguidosService.findByUsuarioSeguido(usuarioFound);
        return new ResponseEntity(seguidores, HttpStatus.OK);
    }

    @PostMapping("/usuario/seguir")
    public ResponseEntity<?> seguir(@RequestBody Map<String, Object> parametros){
        String usuario = (String) parametros.get("nombreUsuario");
        String nombreUsuarioSeguido = (String) parametros.get("usuarioSeguido");
        Usuario nombreUsuario = usuarioService.findByName(usuario);
        Usuario usuarioSeguido = usuarioService.findByName(nombreUsuarioSeguido);
        UsuariosSeguidos usuariosSeguidos = new UsuariosSeguidos(nombreUsuario, usuarioSeguido);
        usuariosSeguidosService.save(usuariosSeguidos);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/usuario/dejarDeSeguir")
    public ResponseEntity<?> dejarDeSeguir(@RequestBody Map<String, Object> parametros){
        String usuario = (String) parametros.get("nombreUsuario");
        String nombreUsuarioSeguido = (String) parametros.get("usuarioSeguido");
        Usuario nombreUsuario = usuarioService.findByName(usuario);
        Usuario usuarioSeguido = usuarioService.findByName(nombreUsuarioSeguido);
        UsuariosSeguidos usuariosSeguidos = new UsuariosSeguidos(nombreUsuario, usuarioSeguido);
        usuariosSeguidosService.delete(usuariosSeguidos);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/resenasSeguidosPor/{nombre}/{idPelicula}")
    public ResponseEntity<List<Map<String, Object>>> getResenasSeguidos(@PathVariable String nombre, @PathVariable String idPelicula) {
        Usuario usuarioFound = usuarioService.findByName(nombre);
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
        return new ResponseEntity(resenasSeguidos, HttpStatus.OK);
    }


}
