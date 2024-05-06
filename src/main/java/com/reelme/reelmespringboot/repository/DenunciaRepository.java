package com.reelme.reelmespringboot.repository;

import com.reelme.reelmespringboot.model.Denuncia;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DenunciaRepository extends CrudRepository<Denuncia, Long> {
    List<Denuncia> findAll();
    Denuncia findByDenuncianteAndDenunciadoAndIdResena(String denunciante, String denunciado, int idResena);
}
