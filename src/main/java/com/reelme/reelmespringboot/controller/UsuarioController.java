package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.dto.AboutDTO;
import com.reelme.reelmespringboot.model.*;
import com.reelme.reelmespringboot.repository.RolRepository;
import com.reelme.reelmespringboot.repository.UsuariosSeguidosRepository;
import com.reelme.reelmespringboot.service.JwtTokenProviderService;
import com.reelme.reelmespringboot.service.ResenaService;
import com.reelme.reelmespringboot.service.UsuarioService;
import com.reelme.reelmespringboot.service.UsuariosSeguidosService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuariosSeguidosService usuariosSeguidosService;

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private JwtTokenProviderService jwtTokenProviderService;

    @PostMapping("/usuario/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        Usuario existingUser = usuarioService.findByName(usuario.getNombre());
        if (existingUser != null) {
            return new ResponseEntity<>(Collections.singletonMap("error", "Username is already in use"), HttpStatus.CONFLICT);
        }
        existingUser = usuarioService.findByEmail(usuario.getEmail());
        if (existingUser != null) {
            return new ResponseEntity<>(Collections.singletonMap("error", "Email is already in use"), HttpStatus.CONFLICT);
        }
        String pwordHash = BCrypt.hashpw(usuario.getPword(), BCrypt.gensalt());
        usuario.setPword(pwordHash);
        usuario.setPerfil("PruebaPerfil.jpg");
        usuario.setRoles(Collections.singleton(rolRepository.findByRol("ROLE_USER")));
        usuarioService.save(usuario);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/usuario/delete")
    public ResponseEntity<?> delete(@RequestParam String pword, @RequestParam String nombre, @RequestHeader("Authorization") String token){
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (!nombre.equals(jwtTokenProviderService.getUsernameFromJwt(token))) {
            return new ResponseEntity<>(Collections.singletonMap("error", "Invalid or expired token"), HttpStatus.UNAUTHORIZED);
        }
        Usuario existingUser = usuarioService.findByName(nombre);
        System.out.println("Usuario encontrado: " + existingUser.getNombre());
        System.out.println("pword: " + pword);
        if (existingUser != null && BCrypt.checkpw(pword, existingUser.getPword())) {
            usuarioService.delete(existingUser);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/usuario/loginNombreEmail")
    public ResponseEntity<?> loginNombreEmail(@RequestParam String nombreEmail, @RequestParam String pword) {
        if (nombreEmail == null || nombreEmail.trim().isEmpty()) {
            return new ResponseEntity<>(Collections.singletonMap("error", "Username/Email cannot be empty"), HttpStatus.BAD_REQUEST);
        }
        Usuario existingUser = usuarioService.findByName(nombreEmail);
        if (existingUser == null) {
            existingUser = usuarioService.findByEmail(nombreEmail);
            if(existingUser == null){
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        }
        if (BCrypt.checkpw(pword, existingUser.getPword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            //response.put("usuario", existingUser);
            response.put("username", existingUser.getNombre());
            response.put("perfil", existingUser.getPerfil());
            response.put("veto", existingUser.getVeto());
            response.put("color", existingUser.getColor());

            List<String> roles = existingUser.getRoles().stream()
                    .map(Rol::getRol)
                    .collect(Collectors.toList());
            response.put("roles", roles);

            String jwt = jwtTokenProviderService.createToken(existingUser.getNombre(), roles);
            response.put("token", jwt);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.singletonMap("error", "Incorrect password"), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/usuario/perfil")
    public ResponseEntity<?> setPerfil(@RequestParam String nombre, @RequestParam String perfil, @RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (!nombre.equals(jwtTokenProviderService.getUsernameFromJwt(token))) {
            return new ResponseEntity<>(Collections.singletonMap("error", "Invalid or expired token"), HttpStatus.UNAUTHORIZED);
        }
        Usuario usuario = usuarioService.findByName(nombre);
        if (usuario != null) {
            usuario.setPerfil(perfil);
            usuarioService.save(usuario);
            return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/usuario/seguidos/{nombre}")
    public ResponseEntity<?> getSeguidos(@PathVariable String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new ResponseEntity<>(Collections.singletonMap("error", "Username cannot be empty"), HttpStatus.BAD_REQUEST);
        }
        Usuario usuarioFound = usuarioService.findByName(nombre);
        if(usuarioFound == null){
            return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
        }
        List<UsuariosSeguidos> seguidos = usuariosSeguidosService.findByNombreUsuario(usuarioFound);
        if(seguidos.isEmpty()){
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
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
        return ResponseEntity.ok(seguidosCompletos);
    }

    @GetMapping("/usuario/{nombre}/seguidos/reviewed")
    public ResponseEntity<?> getResenasRecientesSeguidos(@PathVariable String nombre) {
        try{
            Usuario usuarioFound = usuarioService.findByName(nombre);
            if(usuarioFound == null){
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
            List<UsuariosSeguidos> seguidos = usuariosSeguidosService.findByNombreUsuario(usuarioFound);
            List<Resena> resenasSeguidos = new ArrayList<>();
            for (UsuariosSeguidos seguido : seguidos) {
                Usuario usuario = usuarioService.findByName(seguido.getUsuarioSeguido().getNombre());
                List<Resena> resenasUsuario = resenaService.findByUsuario(usuario);
                Resena resena = resenasUsuario.stream()
                        .sorted(Comparator.comparing(Resena::getFecha).reversed())
                        .findFirst()
                        .orElse(null);
                if(resena != null){
                    resenasSeguidos.add(resena);
                }

            }
            return ResponseEntity.ok(resenasSeguidos);
        }catch (Exception e){
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("/usuarios")
    public ResponseEntity<?> getUsuarios(){
        try{
            List<Usuario> usuarios = usuarioService.findAll();
            if(usuarios.isEmpty()){
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            }else{
                return ResponseEntity.ok(usuarios);
            }
        }catch (Exception e){
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/usuario/{nombre}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable String nombre){
        Usuario usuario = usuarioService.findByName(nombre);
        if(usuario == null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(usuario);
        }
    }

    @GetMapping("/usuario/about/{usuario}")
    public ResponseEntity<?> getAbout(@PathVariable String usuario){
        try {
            Usuario usuarioFound = usuarioService.findByName(usuario);
            if (usuarioFound == null) {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                Date start = calendar.getTime();

                calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
                Date end = calendar.getTime();
                AboutDTO about = new AboutDTO(usuarioFound.getNombre(), usuarioFound.getApodo(), usuarioFound.getDireccion(), usuarioFound.getPerfil(), usuarioFound.getColor(), usuarioFound.getBio(), resenaService.countByUsuario(usuarioFound), resenaService.countByFechaBetweenAndNomUsuario(start, end, usuarioFound), usuariosSeguidosService.countByUsuarioSeguido(usuarioFound), usuariosSeguidosService.countByNombreUsuario(usuarioFound), usuarioFound.getRango());
                return ResponseEntity.ok(about);
            }
        }catch (Exception e){
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuarios/{nombre}")
    public ResponseEntity<List<Usuario>> getUsuariosLike(@PathVariable String nombre) {
        try {
            List<Usuario> usuariosLike = usuarioService.findByNombreContaining(nombre);
            if (usuariosLike.isEmpty()) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
            } else {
                return ResponseEntity.ok(usuariosLike);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("usuario/cambiarPword")
    public ResponseEntity<?> editPword(@RequestParam String nombre, @RequestParam String pword, @RequestParam String pword2, @RequestHeader("Authorization") String token){
        try{
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (!nombre.equals(jwtTokenProviderService.getUsernameFromJwt(token))) {
                return new ResponseEntity<>(Collections.singletonMap("error", "Invalid or expired token"), HttpStatus.UNAUTHORIZED);
            }
            Usuario usuario = usuarioService.findByName(nombre);
            System.out.println("Usuario encontrado: " + usuario.getNombre() + " " + usuario.getPword());
            if(usuario != null){
                if(BCrypt.checkpw(pword, usuario.getPword())){
                    String pwordHash = BCrypt.hashpw(pword2, BCrypt.gensalt());
                    usuario.setPword(pwordHash);
                    usuarioService.save(usuario);
                    return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(Collections.singletonMap("error", "Incorrect password"), HttpStatus.UNAUTHORIZED);
                }

            }else{
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuario/numResenas/{nombre}")
    public ResponseEntity<?> getNumResenas(@PathVariable String nombre, @RequestHeader("Authorization") String token){
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (!nombre.equals(jwtTokenProviderService.getUsernameFromJwt(token))) {
                return new ResponseEntity<>(Collections.singletonMap("error", "Invalid or expired token"), HttpStatus.UNAUTHORIZED);
            }
            Usuario usuario = usuarioService.findByName(nombre);
            if (usuario == null) {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
            int numResenas = resenaService.countByUsuario(usuario);
            return ResponseEntity.ok(numResenas);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/usuario/color")
    public ResponseEntity<?> editColor(@RequestParam String nombre, @RequestParam String color, @RequestHeader("Authorization") String token){
        try{
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (!nombre.equals(jwtTokenProviderService.getUsernameFromJwt(token))) {
                return new ResponseEntity<>(Collections.singletonMap("error", "Invalid or expired token"), HttpStatus.UNAUTHORIZED);
            }
            Usuario usuario = usuarioService.findByName(nombre);
            if(usuario != null){
                usuario.setColor(color);
                usuarioService.save(usuario);
                return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/usuario")
    public ResponseEntity<?> editUsuario(@RequestParam String nombre, @RequestParam(required = false) String email, @RequestParam(required = false) String apodo, @RequestParam(required = false) String direccion, @RequestParam(required = false) String bio, @RequestHeader("Authorization") String token){
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (!nombre.equals(jwtTokenProviderService.getUsernameFromJwt(token))) {
                return new ResponseEntity<>(Collections.singletonMap("error", "Invalid or expired token"), HttpStatus.UNAUTHORIZED);
            }
            Usuario existingUsuario = usuarioService.findByName(nombre);
            if (existingUsuario != null) {
                existingUsuario.setEmail(email != null && !email.isEmpty() ? email : null);
                existingUsuario.setApodo(apodo != null && !apodo.isEmpty() ? apodo : null);
                existingUsuario.setDireccion(direccion != null && !direccion.isEmpty() ? direccion : null);
                existingUsuario.setBio(bio != null && !bio.isEmpty() ? bio : null);
                usuarioService.save(existingUsuario);
                return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/usuario/vetar")
    public ResponseEntity<?> vetar(@RequestParam String nombre, @RequestParam String duracionString, @RequestHeader("Authorization") String token) throws Exception{
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            List<String> roles = jwtTokenProviderService.getRolesFromJwt(token);
            if (!roles.contains("ROLE_ADMIN")) {
                return new ResponseEntity<>(Collections.singletonMap("error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
            }
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
            Date duracionVeto = formateador.parse(duracionString);
            Usuario usuario = usuarioService.findByName(nombre);
            if (usuario != null) {
                usuario.setVeto(duracionVeto);
                usuarioService.save(usuario);
                return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usuario/comprobarVeto")
    public ResponseEntity<?> comprobarVeto(@RequestParam String nombre, @RequestHeader("Authorization") String token){
        try{
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (!nombre.equals(jwtTokenProviderService.getUsernameFromJwt(token))) {
                return new ResponseEntity<>(Collections.singletonMap("error", "Invalid or expired token"), HttpStatus.UNAUTHORIZED);
            }
            Usuario usuario = usuarioService.findByName(nombre);
            if (usuario != null) {
                Date veto = usuario.getVeto();
                if (veto != null) {
                    Date now = new Date();
                    if (now.after(veto)) {
                        usuario.setVeto(null);
                        usuarioService.save(usuario);
                        return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(Collections.singletonMap("status", "User is vetoed"), HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/usuario/levantarVeto")
    public ResponseEntity<?> levantarVeto(@RequestParam String nombre){
        try{
            Usuario usuario = usuarioService.findByName(nombre);
            if (usuario != null) {
                usuario.setVeto(null);
                usuarioService.save(usuario);
                return new ResponseEntity<>(Collections.singletonMap("status", "success"), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
