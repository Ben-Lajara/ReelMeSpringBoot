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
        List<Denuncia> denuncias = denunciaService.findAll();
        return ResponseEntity.ok(denuncias);
    }

    @GetMapping("/denuncias/rechazadas")
    public ResponseEntity<?> getDenunciasRechazadas() {
        List<Denuncia> denuncias = denunciaService.findByEstadoLike("Rechazada");

        for (Denuncia denuncia : denuncias) {
            Resena resena = resenaService.findById(denuncia.getIdResena());
            denuncia.setComentarioResena(resena.getComentario());
        }

        return ResponseEntity.ok(denuncias);
    }

    @GetMapping("/denuncias/pendientes")
    public ResponseEntity<?> getDenunciasPendientes() {
        List<Denuncia> denuncias = denunciaService.findByEstadoIsNull();
        for (Denuncia denuncia : denuncias) {
            Resena resena = resenaService.findById(denuncia.getIdResena());
            denuncia.setComentarioResena(resena.getComentario());
        }
        return ResponseEntity.ok(denuncias);
    }

    @GetMapping("/denuncias/aceptadas")
    public ResponseEntity<?> getDenunciasAceptadas() {
        List<Denuncia> denuncias = denunciaService.findByEstadoLike("Aceptada");
        for (Denuncia denuncia : denuncias) {
            Resena resena = resenaService.findById(denuncia.getIdResena());
            denuncia.setComentarioResena(resena.getComentario());
        }
        return ResponseEntity.ok(denuncias);
    }


    @PostMapping("/denuncias/denunciar")
    public ResponseEntity<?> denunciar(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam String idPelicula, @RequestParam String motivo) {
        System.out.println("Se ha accedido a denunciar.");
        Usuario usuarioDenunciado = usuarioService.findByName(denunciado);
        Optional<Pelicula> peliculaDenunciada = peliculaService.findById(idPelicula);
        Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioDenunciado, peliculaDenunciada);
        int idResena = resena.getId();
        Denuncia denuncia = new Denuncia(denunciante, denunciado, motivo, idResena);
        denunciaService.save(denuncia);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/denuncias/existente")
    public ResponseEntity<Denuncia> denunciaExistente(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam String idPelicula) {
        Usuario usuarioDenunciado = usuarioService.findByName(denunciado);
        Optional<Pelicula> peliculaDenunciada = peliculaService.findById(idPelicula);
        Resena resena = resenaService.findByUsuarioAndIdPelicula(usuarioDenunciado, peliculaDenunciada);
        int idResena = resena.getId();
        Denuncia denuncia = denunciaService.findByDenuncianteAndDenunciadoAndIdResena(denunciante, denunciado, idResena);
        return ResponseEntity.ok(denuncia);
    }


    @PutMapping("/rechazarDenuncia")
    public ResponseEntity<?> rechazarDenuncia(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam int idResena) {
        Usuario usuarioDenunciado = usuarioService.findByName(denunciado);
        Denuncia denuncia = denunciaService.findByDenuncianteAndDenunciadoAndIdResena(denunciante, denunciado, idResena);
        denuncia.setEstado("Rechazada");
        denunciaService.save(denuncia);
        return ResponseEntity.ok(denuncia);
    }

    @PutMapping("/aceptarDenuncia")
    public ResponseEntity<?> aceptarDenuncia(@RequestParam String denunciante, @RequestParam String denunciado, @RequestParam int idResena) {

        Denuncia denuncia = denunciaService.findByDenuncianteAndDenunciadoAndIdResena(denunciante, denunciado, idResena);
        denuncia.setEstado("Aceptada");
        Resena resena = resenaService.findById(denuncia.getIdResena());
        resena.setDenunciada(true);
        denunciaService.save(denuncia);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}