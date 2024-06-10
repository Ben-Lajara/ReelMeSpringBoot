package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Denuncia;
import com.reelme.reelmespringboot.model.Resena;
import com.reelme.reelmespringboot.model.Usuario;
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

    public List<Denuncia> findByEstadoLike(String estado) {
        return denunciaRepository.findByEstadoLike(estado);
    }

    public List<Denuncia> findByEstadoIsNull() {
        return denunciaRepository.findByEstadoIsNull();
    }

    public Denuncia save(Denuncia denuncia) {
        return denunciaRepository.save(denuncia);
    }

    /*public Denuncia findByDenuncianteAndDenunciadoAndIdResena(String denunciante, String denunciado, int idResena) {
        return denunciaRepository.findByDenuncianteAndDenunciadoAndIdResena(denunciante, denunciado, idResena);
    }*/

    public Denuncia findByDenuncianteAndDenunciadoAndIdResena(Usuario denunciante, Usuario denunciado, Resena idResena) {
        return denunciaRepository.findByDenuncianteAndDenunciadoAndIdResena(denunciante, denunciado, idResena);
    }

}
