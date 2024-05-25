package com.reelme.reelmespringboot.repository;

import com.reelme.reelmespringboot.model.Revisionado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevisionadoRepository extends CrudRepository<Revisionado, Integer> {
    Revisionado findById(int id);
    void deleteById(int id);
}
