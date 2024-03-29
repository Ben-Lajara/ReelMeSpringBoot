package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.model.UsuariosSeguidos;
import com.reelme.reelmespringboot.repository.UsuarioRepository;
import com.reelme.reelmespringboot.service.UsuarioService;
import com.reelme.reelmespringboot.service.UsuariosSeguidosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UsuariosSeguidosController {
    @Autowired
    private UsuariosSeguidosService usuariosSeguidosService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/seguidosPor/{nombre}")
    public ResponseEntity<UsuariosSeguidos> getSeguidos(@PathVariable String nombre) {
        Usuario usuarioFound = usuarioService.findByName(nombre);
        List<UsuariosSeguidos> seguidos = usuariosSeguidosService.findByNombreUsuario(usuarioFound);
        return new ResponseEntity(seguidos, HttpStatus.OK);
    }

    @GetMapping("/seguidores/{nombre}")
    public ResponseEntity<UsuariosSeguidos> getSeguidores(@PathVariable String nombre) {
        Usuario usuarioFound = usuarioService.findByName(nombre);
        List<UsuariosSeguidos> seguidores = usuariosSeguidosService.findByUsuarioSeguido(usuarioFound);
        return new ResponseEntity(seguidores, HttpStatus.OK);
    }

    @PostMapping("/seguir")
    public ResponseEntity<?> seguir(@RequestBody Map<String, Object> parametros){
        String usuario = (String) parametros.get("nombreUsuario");
        String nombreUsuarioSeguido = (String) parametros.get("usuarioSeguido");
        Usuario nombreUsuario = usuarioService.findByName(usuario);
        Usuario usuarioSeguido = usuarioService.findByName(nombreUsuarioSeguido);
        UsuariosSeguidos usuariosSeguidos = new UsuariosSeguidos(nombreUsuario, usuarioSeguido);
        usuariosSeguidosService.save(usuariosSeguidos);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/dejarDeSeguir")
    public ResponseEntity<?> dejarDeSeguir(@RequestBody Map<String, Object> parametros){
        String usuario = (String) parametros.get("nombreUsuario");
        String nombreUsuarioSeguido = (String) parametros.get("usuarioSeguido");
        Usuario nombreUsuario = usuarioService.findByName(usuario);
        Usuario usuarioSeguido = usuarioService.findByName(nombreUsuarioSeguido);
        UsuariosSeguidos usuariosSeguidos = new UsuariosSeguidos(nombreUsuario, usuarioSeguido);
        usuariosSeguidosService.delete(usuariosSeguidos);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
