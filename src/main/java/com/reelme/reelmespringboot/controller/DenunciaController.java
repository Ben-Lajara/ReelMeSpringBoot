package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.model.Denuncia;
import com.reelme.reelmespringboot.model.Pelicula;
import com.reelme.reelmespringboot.model.Resena;
import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.service.DenunciaService;
import com.reelme.reelmespringboot.service.PeliculaService;
import com.reelme.reelmespringboot.service.ResenaService;
import com.reelme.reelmespringboot.service.UsuarioService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DenunciaController {
    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PeliculaService peliculaService;

    @GetMapping("/denuncias")
    public ResponseEntity<?> getDenuncias() {
        try {
            List<Denuncia> denuncias = denunciaService.findAll();
            return ResponseEntity.ok(denuncias);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/denuncias/rechazadas")
    public ResponseEntity<?> getDenunciasRechazadas() {
        try {
            List<Denuncia> denuncias = denunciaService.findByEstadoLike("Rechazada");
            for (Denuncia denuncia : denuncias) {
                Resena resena = resenaService.findById(denuncia.getIdResena());
                denuncia.setComentarioResena(resena.getComentario());
            }
            return ResponseEntity.ok(denuncias);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/denuncias/pendientes")
    public ResponseEntity<?> getDenunciasPendientes() {
        try {
            List<Denuncia> denuncias = denunciaService.findByEstadoIsNull();
            for (Denuncia denuncia : denuncias) {
                Resena resena = resenaService.findById(denuncia.getIdResena());
                denuncia.setComentarioResena(resena.getComentario());
            }
            return ResponseEntity.ok(denuncias);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/denuncias/aceptadas")
    public ResponseEntity<?> getDenunciasAceptadas() {
        try {
            List<Denuncia> denuncias = denunciaService.findByEstadoLike("Aceptada");
            for (Denuncia denuncia : denuncias) {
                Resena resena = resenaService.findById(denuncia.getIdResena());
                denuncia.setComentarioResena(resena.getComentario());
            }
            return ResponseEntity.ok(denuncias);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/denuncias/denunciar")
    public ResponseEntity<?> denunciar(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam String idPelicula, @RequestParam String motivo) {
        try {
            Usuario usuarioDenunciado = usuarioService.findByName(denunciado);
            Optional<Pelicula> peliculaDenunciada = peliculaService.findById(idPelicula);
            if (usuarioDenunciado != null && peliculaDenunciada.isPresent()) {
                Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioDenunciado, peliculaDenunciada);
                int idResena = resena.getId();
                Denuncia denuncia = new Denuncia(denunciante, denunciado, motivo, idResena);
                denunciaService.save(denuncia);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User or movie not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/denuncias/existente")
    public ResponseEntity<?> denunciaExistente(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam String idPelicula) {
        try {
            Usuario usuarioDenunciado = usuarioService.findByName(denunciado);
            Optional<Pelicula> peliculaDenunciada = peliculaService.findById(idPelicula);
            if (usuarioDenunciado != null && peliculaDenunciada.isPresent()) {
                Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioDenunciado, peliculaDenunciada);
                int idResena = resena.getId();
                Denuncia denuncia = denunciaService.findByDenuncianteAndDenunciadoAndIdResena(denunciante, denunciado, idResena);
                return ResponseEntity.ok(denuncia);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/rechazarDenuncia")
    public ResponseEntity<?> rechazarDenuncia(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam int idResena) {
        try {
            Usuario usuarioDenunciado = usuarioService.findByName(denunciado);
            if (usuarioDenunciado != null) {
                Denuncia denuncia = denunciaService.findByDenuncianteAndDenunciadoAndIdResena(denunciante, denunciado, idResena);
                denuncia.setEstado("Rechazada");
                denunciaService.save(denuncia);
                return ResponseEntity.ok(denuncia);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/aceptarDenuncia")
    public ResponseEntity<?> aceptarDenuncia(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam int idResena) {
        try {
            Denuncia denuncia = denunciaService.findByDenuncianteAndDenunciadoAndIdResena(denunciante, denunciado, idResena);
            if (denuncia != null) {
                denuncia.setEstado("Aceptada");
                Resena resena = resenaService.findById(denuncia.getIdResena());
                resena.setDenunciada(true);
                denunciaService.save(denuncia);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "Denunciation not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
