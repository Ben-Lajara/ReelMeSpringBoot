package com.reelme.reelmespringboot.service;

import com.reelme.reelmespringboot.model.Pelicula;
import com.reelme.reelmespringboot.model.Resena;
import com.reelme.reelmespringboot.model.Usuario;
import com.reelme.reelmespringboot.model.UsuariosSeguidos;
import com.reelme.reelmespringboot.repository.ResenaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Resena> findTop4ByUsuarioOrderByLatestActivityDesc(Usuario usuario) {
        List<Resena> resenas = findByUsuario(usuario);
        resenas.sort((r1, r2) -> {
            Date date1 = r1.getRevisionados().isEmpty() ? r1.getFecha() : r1.getRevisionados().get(r1.getRevisionados().size() - 1).getFechaRevisionado();
            Date date2 = r2.getRevisionados().isEmpty() ? r2.getFecha() : r2.getRevisionados().get(r2.getRevisionados().size() - 1).getFechaRevisionado();
            return date2.compareTo(date1);
        });

        for (Resena resena : resenas) {
            resena.setRevisionado(!resena.getRevisionados().isEmpty());
        }

        return resenas.size() > 4 ? resenas.subList(0, 4) : resenas;
    }


    public int countByFechaBetweenAndNomUsuario(Date dateStart, Date dateEnd, Usuario usuario) {
        return resenaRepository.countByFechaBetweenAndNomUsuario(dateStart, dateEnd, usuario);
    }

    public Resena findById(Long id) {
        return resenaRepository.findById(id);
    }

    public void delete(Resena resena) {
        resenaRepository.delete(resena);
    }

    public List<Pelicula> findTop4PeliculasWithMostResenas() {
        List<Object[]> results = resenaRepository.findTop4PeliculasByResenaCount();
        List<Pelicula> topPeliculas = new ArrayList<>();
        for (Object[] result : results) {
            Pelicula pelicula = (Pelicula) result[0];
            topPeliculas.add(pelicula);
        }
        return topPeliculas;
    }
}
