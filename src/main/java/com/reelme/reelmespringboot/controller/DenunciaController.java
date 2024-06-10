package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.dto.DenunciaDTO;
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

import javax.swing.text.html.Option;
import java.util.stream.Collectors;
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

    private DenunciaDTO convertToDTO(Denuncia denuncia) {
        DenunciaDTO dto = new DenunciaDTO();
        dto.setId(denuncia.getId());
        dto.setDenunciante(denuncia.getDenunciante().getNombre());
        dto.setDenunciado(denuncia.getDenunciado().getNombre());
        dto.setMotivo(denuncia.getMotivo());
        dto.setIdResena(denuncia.getIdResena().getId());
        dto.setComentarioResena(denuncia.getIdResena().getComentario());
        dto.setEstado(denuncia.getEstado());
        return dto;
    }

    private Denuncia convertToEntity(DenunciaDTO dto) {
        Denuncia denuncia = new Denuncia();
        denuncia.setId(dto.getId());
        denuncia.setDenunciante(usuarioService.findByName(dto.getDenunciante()));
        denuncia.setDenunciado(usuarioService.findByName(dto.getDenunciado()));
        denuncia.setMotivo(dto.getMotivo());
        denuncia.setIdResena(resenaService.findById(dto.getIdResena()));
        denuncia.setEstado(dto.getEstado());
        return denuncia;
    }

    @GetMapping("/denuncias")
    public ResponseEntity<?> getDenuncias() {
        try {
            List<Denuncia> denuncias = denunciaService.findAll();
            List<DenunciaDTO> denunciaDTOs = denuncias.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(denunciaDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/denuncias/rechazadas")
    public ResponseEntity<?> getDenunciasRechazadas() {
        try {
            List<Denuncia> denuncias = denunciaService.findByEstadoLike("Rechazada");
            List<DenunciaDTO> denunciaDTOs = denuncias.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(denunciaDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/denuncias/pendientes")
    public ResponseEntity<?> getDenunciasPendientes() {
        try {
            List<Denuncia> denuncias = denunciaService.findByEstadoIsNull();
            List<DenunciaDTO> denunciaDTOs = denuncias.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(denunciaDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/denuncias/aceptadas")
    public ResponseEntity<?> getDenunciasAceptadas() {
        try {
            List<Denuncia> denuncias = denunciaService.findByEstadoLike("Aceptada");
            List<DenunciaDTO> denunciaDTOs = denuncias.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(denunciaDTOs);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/denuncias/denunciar")
    public ResponseEntity<?> denunciar(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam String idPelicula, @RequestParam String motivo) {
        try {
            Usuario usuarioDenunciante = usuarioService.findByName(denunciante);
            Usuario usuarioDenunciado = usuarioService.findByName(denunciado);
            Optional<Pelicula> pelicula = peliculaService.findById(idPelicula);
            System.out.println("Pelicula" + pelicula.get().getTitulo()  + " " + pelicula.get().getId());

            if (usuarioDenunciante != null && usuarioDenunciado != null && pelicula.isPresent()) {
                Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioDenunciado, pelicula);
                System.out.println("Resena" + resena.getComentario() + " " + resena.getId());
                Denuncia denuncia = new Denuncia(usuarioDenunciante, usuarioDenunciado, motivo, resena);
                denunciaService.save(denuncia);
                DenunciaDTO denunciaDTO = convertToDTO(denuncia);
                return ResponseEntity.ok(denunciaDTO);
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
            Usuario usuarioDenunciante = usuarioService.findByName(denunciante);
            Usuario usuarioDenunciado = usuarioService.findByName(denunciado);
            Optional<Pelicula> pelicula = peliculaService.findById(idPelicula);
            Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioDenunciante, pelicula);

            if (usuarioDenunciante != null && usuarioDenunciado != null && resena != null) {
                Denuncia denuncia = denunciaService.findByDenuncianteAndDenunciadoAndIdResena(usuarioDenunciante, usuarioDenunciado, resena);
                DenunciaDTO denunciaDTO = convertToDTO(denuncia);
                return ResponseEntity.ok(denunciaDTO);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User or movie not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/rechazarDenuncia")
    public ResponseEntity<?> rechazarDenuncia(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam Long idResena) {
        try {
            Usuario usuarioDenunciante = usuarioService.findByName(denunciante);
            Usuario usuarioDenunciado = usuarioService.findByName(denunciado);
            Resena resena = resenaService.findById(idResena);

            if (usuarioDenunciante != null && usuarioDenunciado != null && resena != null) {
                Denuncia denuncia = denunciaService.findByDenuncianteAndDenunciadoAndIdResena(usuarioDenunciante, usuarioDenunciado, resena);
                denuncia.setEstado("Rechazada");
                denunciaService.save(denuncia);
                DenunciaDTO denunciaDTO = convertToDTO(denuncia);
                return ResponseEntity.ok(denunciaDTO);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User or movie not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/aceptarDenuncia")
    public ResponseEntity<?> aceptarDenuncia(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam Long idResena) {
        try {
            Usuario usuarioDenunciante = usuarioService.findByName(denunciante);
            Usuario usuarioDenunciado = usuarioService.findByName(denunciado);
            Resena resena = resenaService.findById(idResena);

            if (usuarioDenunciante != null && usuarioDenunciado != null && resena != null) {
                Denuncia denuncia = denunciaService.findByDenuncianteAndDenunciadoAndIdResena(usuarioDenunciante, usuarioDenunciado, resena);
                denuncia.setEstado("Aceptada");
                resena.setDenunciada(true);
                denunciaService.save(denuncia);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("error", "User or movie not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
