package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Pelicula;
import com.reelme.reelmespringboot.model.Resena;
import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.repository.ResenaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ResenaService {
    @Autowired
    private ResenaRepository resenaRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Resena save(Resena resena) {
        return resenaRepository.save(resena);
    }

    public Resena findByUsuarioAndIdPelicula(Usuario usuario, Optional<Pelicula> idPelicula) {
        return resenaRepository.findByNomUsuarioAndIdPelicula(usuario, idPelicula);
    }

    public List<Resena> findByUsuario(Usuario usuario) {
        System.out.println("Usuario: " + usuario.getNombre());
        return resenaRepository.findByNomUsuario(usuario);
    }

    public List<Resena> findByIdPelicula(Optional<Pelicula> idPelicula) {
        return resenaRepository.findByIdPelicula(idPelicula);
    }

    public int countByUsuario(Usuario usuario) {
        return resenaRepository.countByNomUsuario(usuario);
    }

    public List<Resena> findTop4ByUsuarioOrderByFechaDesc(Usuario usuario) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Resena> query = cb.createQuery(Resena.class);
        Root<Resena> root = query.from(Resena.class);
        query.select(root).where(cb.equal(root.get("nomUsuario"), usuario)).orderBy(cb.desc(root.get("fecha")));
        return entityManager.createQuery(query).setMaxResults(4).getResultList();
    }

    public int countByFechaBetweenAndNomUsuario(Date dateStart, Date dateEnd, Usuario usuario) {
        return resenaRepository.countByFechaBetweenAndNomUsuario(dateStart, dateEnd, usuario);
    }

    public Resena findById(int id) {
        return resenaRepository.findById(id);
    }

    public void delete(Resena resena) {
        resenaRepository.delete(resena);
    }


}
