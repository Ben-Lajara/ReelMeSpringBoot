package com.reelme.reelmespringboot.controller;

import com.reelme.reelmespringboot.service.RevisionadoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RevisionadoController {
    @Autowired
    private RevisionadoService revisionadoService;

    @DeleteMapping("/revisionado")
    public ResponseEntity<?> deleteRevisionado(@RequestParam int id) {
        try{
            if(revisionadoService.findById(id) != null){
                revisionadoService.delete(id);
                return ResponseEntity.ok().build();
            }else{
                return new ResponseEntity<>(Collections.singletonMap("error", "Revisionado not found"), HttpStatus.NOT_FOUND);
            }

        }catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
