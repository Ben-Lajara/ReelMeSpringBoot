package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Denuncia;
import com.reelme.reelmespringboot.repository.DenunciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DenunciaService {
    @Autowired
    private DenunciaRepository denunciaRepository;

    public List<Denuncia> findAll() {
        return denunciaRepository.findAll();
    }

    public Denuncia save(Denuncia denuncia) {
        return denunciaRepository.save(denuncia);
    }

    public Denuncia findByDenuncianteAndDenunciadoAndIdResena(String denunciante, String denunciado, int idResena) {
        return denunciaRepository.findByDenuncianteAndDenunciadoAndIdResena(denunciante, denunciado, idResena);
    }
}
