package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Revisionado;
import com.reelme.reelmespringboot.repository.RevisionadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevisionadoService {
    @Autowired
    private RevisionadoRepository revisionadoRepository;

    public void save(Revisionado revisionado) {
        revisionadoRepository.save(revisionado);
    }

    public Revisionado findById(int id) {
        return revisionadoRepository.findById(id);
    }

}
