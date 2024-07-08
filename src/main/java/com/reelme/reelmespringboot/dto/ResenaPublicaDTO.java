package com.reelme.reelmespringboot.dto;

import com.reelme.reelmespringboot.model.Revisionado;

import java.util.Date;
import java.util.List;

public class ResenaPublicaDTO {
    public Date fecha;
    public String comentario;
    public float calificacion;
    public boolean gustado;
    public boolean spoiler;
    public String usuario;
    public String idPelicula;
    public String titulo;
    public String year;
    public String foto;
    public List<Revisionado> revisionados;
    public boolean denunciada;

    public ResenaPublicaDTO() {
    }

    public ResenaPublicaDTO(Date fecha, String comentario, float calificacion, boolean gustado, boolean spoiler, String usuario, String idPelicula, String titulo, String year, String foto, List<Revisionado> revisionados, boolean denunciada) {
        this.fecha = fecha;
        this.comentario = comentario;
        this.calificacion = calificacion;
        this.gustado = gustado;
        this.spoiler = spoiler;
        this.usuario = usuario;
        this.idPelicula = idPelicula;
        this.titulo = titulo;
        this.year = year;
        this.foto = foto;
        this.revisionados = revisionados;
        this.denunciada = denunciada;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public boolean isGustado() {
        return gustado;
    }

    public void setGustado(boolean gustado) {
        this.gustado = gustado;
    }

    public boolean isSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Revisionado> getRevisionados() {
        return revisionados;
    }

    public void setRevisionados(List<Revisionado> revisionados) {
        this.revisionados = revisionados;
    }

    public boolean isDenunciada() {
        return denunciada;
    }

    public void setDenunciada(boolean denunciada) {
        this.denunciada = denunciada;
    }
}
