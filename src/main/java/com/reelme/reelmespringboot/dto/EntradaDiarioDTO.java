package com.reelme.reelmespringboot.dto;

import com.reelme.reelmespringboot.model.Pelicula;
import com.reelme.reelmespringboot.model.Usuario;

import java.util.Date;

public class EntradaDiarioDTO {
    private Date fecha;
    private float calificacion;
    private String comentario;
    private boolean gustado;
    private Pelicula idPelicula;
    private Usuario nomUsuario;
    private boolean denunciada;
    private boolean esRevisionado;

    public EntradaDiarioDTO() {
    }

    public EntradaDiarioDTO(Date fecha, float calificacion, String comentario, boolean gustado, Pelicula idPelicula, Usuario nomUsuario, boolean denunciada, boolean esRevisionado) {
        this.fecha = fecha;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.gustado = gustado;
        this.idPelicula = idPelicula;
        this.nomUsuario = nomUsuario;
        this.denunciada = denunciada;
        this.esRevisionado = esRevisionado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public boolean isGustado() {
        return gustado;
    }

    public void setGustado(boolean gustado) {
        this.gustado = gustado;
    }

    public Pelicula getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(Pelicula idPelicula) {
        this.idPelicula = idPelicula;
    }

    public Usuario getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(Usuario nomUsuario) {
        this.nomUsuario = nomUsuario;
    }

    public boolean isDenunciada() {
        return denunciada;
    }

    public void setDenunciada(boolean denunciada) {
        this.denunciada = denunciada;
    }

    public boolean isEsRevisionado() {
        return esRevisionado;
    }

    public void setEsRevisionado(boolean esRevisionado) {
        this.esRevisionado = esRevisionado;
    }
}
