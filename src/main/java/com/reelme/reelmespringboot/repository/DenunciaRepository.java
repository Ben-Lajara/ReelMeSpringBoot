package com.reelme.reelmespringboot.repository;

import com.reelme.reelmespringboot.model.Denuncia;
import com.reelme.reelmespringboot.model.Resena;
import com.reelme.reelmespringboot.model.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DenunciaRepository extends CrudRepository<Denuncia, Long> {
    List<Denuncia> findAll();
    List<Denuncia> findByEstadoLike(String estado);
    List<Denuncia> findByEstadoIsNull();
    //Denuncia findByDenuncianteAndDenunciadoAndIdResena(String denunciante, String denunciado, int idResena);
    Denuncia findByDenuncianteAndDenunciadoAndIdResena(Usuario denunciante, Usuario denunciado, Resena idResena);
}
