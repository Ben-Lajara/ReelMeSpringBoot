package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.service.RevisionadoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RevisionadoController {
    @Autowired
    private RevisionadoService revisionadoService;

    @DeleteMapping("/revisionado")
    public ResponseEntity<?> deleteRevisionado(@RequestParam int id) {
        revisionadoService.delete(id);
        return ResponseEntity.ok().build();
    }
}
