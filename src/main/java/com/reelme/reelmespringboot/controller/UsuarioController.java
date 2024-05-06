package com.reelme.reelmespringboot.controller;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        String pwordHash = BCrypt.hashpw(usuario.getPword(), BCrypt.gensalt());
        usuario.setPword(pwordHash);
        usuario.setPerfil("PruebaPerfil.jpg");
        usuario.setRoles(Collections.singleton(rolRepository.findByRol("ROLE_USER")));
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam String pword, @RequestParam String nombre) {
        Usuario existingUser = usuarioService.findByName(nombre);
        if (existingUser != null && BCrypt.checkpw(pword, existingUser.getPword())) {
            usuarioService.delete(existingUser);
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/loginNombreEmail")
    public ResponseEntity<?> loginNombreEmail(@RequestParam String nombreEmail, @RequestParam String pword) {
        Usuario existingUser = usuarioService.findByName(nombreEmail);
        if (existingUser == null) {
            existingUser = usuarioService.findByEmail(nombreEmail);
        }
        //if (existingUser != null && existingUser.getPword().equals(pword)) {
        if (existingUser != null && BCrypt.checkpw(pword, existingUser.getPword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("usuario", existingUser);

            List<String> roles = existingUser.getRoles().stream()
                    .map(Rol::getRol)
                    .collect(Collectors.toList());
            response.put("roles", roles);

            String jwt = jwtTokenProviderService.createToken(existingUser.getNombre(), roles);
            response.put("token", jwt);

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

    @GetMapping("/usuario/{nombre}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable String nombre){
        Usuario usuario = usuarioService.findByName(nombre);
        if(usuario == null){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(usuario);
        }
    }

    @GetMapping("/about/{usuario}")
    public ResponseEntity<Map<String, Object>> getAbout(@PathVariable String usuario){
        Usuario usuarioFound = usuarioService.findByName(usuario);
        if(usuarioFound == null){
            return ResponseEntity.notFound().build();
        }else{
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            Date start = calendar.getTime();

            calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
            Date end = calendar.getTime();
            Map<String, Object> about = new HashMap<>();
            about.put("nombre", usuarioFound.getNombre());
            about.put("apodo", usuarioFound.getApodo());
            about.put("direccion", usuarioFound.getDireccion());
            about.put("perfil", usuarioFound.getPerfil());
            about.put("color", usuarioFound.getColor());
            about.put("bio", usuarioFound.getBio());
            about.put("vistas", resenaService.countByUsuario(usuarioFound));
            about.put("vistasYear", resenaService.countByFechaBetweenAndNomUsuario(start, end, usuarioFound));
            about.put("seguidores", usuariosSeguidosService.countByUsuarioSeguido(usuarioFound));
            about.put("seguidos", usuariosSeguidosService.countByNombreUsuario(usuarioFound));
            about.put("rango", usuarioFound.getRango());
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

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public void saveFile(MultipartFile file) throws IOException {
        try{
            System.out.println("Se ejecuta saveFile");
            if (!file.isEmpty()) {
                System.out.println("El archivo no está vacío");
                byte[] bytes = file.getBytes();
                System.out.println(bytes);
                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                System.out.println(path);
                Files.write(path, bytes);
                System.out.println("Después de Files.write");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("nombre") String nombre){
        try {
            // Guarda el archivo en el sistema de archivos
            System.out.println(nombre);
            System.out.println(file.getOriginalFilename());
            saveFile(file);
            System.out.println("Después de saveFile");
            // Actualiza el usuario con la ruta de la imagen
            Usuario usuario = usuarioService.findByName(nombre);
            System.out.println(usuario.getNombre());
            usuario.setPerfil(file.getOriginalFilename());
            usuarioService.save(usuario);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/cambiarPword")
    public ResponseEntity<?> editPword(@RequestBody Usuario updatedUsuario) {
        try {
            Usuario existingUsuario = usuarioService.findByName(updatedUsuario.getNombre());
            System.out.println(existingUsuario.getNombre());
            if (existingUsuario != null) {
                String pwordHash = BCrypt.hashpw(updatedUsuario.getPword(), BCrypt.gensalt());
                existingUsuario.setPword(pwordHash);
                usuarioService.save(existingUsuario);
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Usuario not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/numResenas/{nombre}")
    public ResponseEntity<Integer> getNumResenas(@PathVariable String nombre){
        Usuario usuario = usuarioService.findByName(nombre);
        int numResenas = resenaService.countByUsuario(usuario);
        return ResponseEntity.ok(numResenas);
    }

    @PutMapping("/color")
    public ResponseEntity<?> editColor(@RequestBody Usuario updatedUsuario) {
        try {
            Usuario existingUsuario = usuarioService.findByName(updatedUsuario.getNombre());
            if (existingUsuario != null) {
                existingUsuario.setColor(updatedUsuario.getColor());
                usuarioService.save(existingUsuario);
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Usuario not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/bio")
    public ResponseEntity<?> editBio(@RequestBody Usuario updatedUsuario) {
        try {
            Usuario existingUsuario = usuarioService.findByName(updatedUsuario.getNombre());
            if (existingUsuario != null) {
                existingUsuario.setBio(updatedUsuario.getBio());
                usuarioService.save(existingUsuario);
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Usuario not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/usuario")
    public ResponseEntity<?> editUsuario(@RequestBody Usuario updatedUsuario){
        try {
            Usuario existingUsuario = usuarioService.findByName(updatedUsuario.getNombre());
            if (existingUsuario != null) {
                existingUsuario.setEmail(updatedUsuario.getEmail());
                existingUsuario.setPerfil(updatedUsuario.getPerfil());
                existingUsuario.setBio(updatedUsuario.getBio());
                existingUsuario.setApodo(updatedUsuario.getApodo());
                existingUsuario.setDireccion(updatedUsuario.getDireccion());
                usuarioService.save(existingUsuario);
                Map<String, String> response = new HashMap<>();
                response.put("status", "success");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Usuario not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/vetar")
    public ResponseEntity<?> vetar(@RequestParam String nombre, @RequestParam String duracionString) throws Exception{
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy, HH:mm:ss");
        Date duracionVeto = formateador.parse(duracionString);
        try{
            Usuario usuario = usuarioService.findByName(nombre);
            usuario.setVeto(duracionVeto);
            usuarioService.save(usuario);
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

    @PutMapping("/levantarVeto")
    public ResponseEntity<?> levantarVeto(@RequestParam String nombre){
        try{
            Usuario usuario = usuarioService.findByName(nombre);
            usuario.setVeto(null);
            usuarioService.save(usuario);
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
