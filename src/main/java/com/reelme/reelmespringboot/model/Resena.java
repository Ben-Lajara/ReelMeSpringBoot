package com.reelme.reelmespringboot.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date fecha;
    private float calificacion;
    private String comentario;
    private boolean gustado;

    @ManyToOne
    @JoinColumn(name = "id_pelicula")
    private Pelicula idPelicula;

    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario nomUsuario;

    public Resena() {
    }

    public Resena(int id, Date fecha, float calificacion, String comentario, boolean gustado, Pelicula idPelicula, Usuario nomUsuario) {
        this.id = id;
        this.fecha = fecha;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.gustado = gustado;
        this.idPelicula = idPelicula;
        this.nomUsuario = nomUsuario;
    }

    public Resena(Date fecha, float calificacion, String comentario, boolean gustado, Pelicula idPelicula, Usuario nomUsuario) {
        this.fecha = fecha;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.gustado = gustado;
        this.idPelicula = idPelicula;
        this.nomUsuario = nomUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setIdPelicula(Pelicula id_pelicula) {
        this.idPelicula = id_pelicula;
    }

    public Usuario getNomUsuario() {
        return nomUsuario;
    }

    public void setNomUsuario(Usuario usuario) {
        this.nomUsuario = usuario;
    }

    @Override
    public String toString() {
        return "Resena{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", calificacion=" + calificacion +
                ", comentario='" + comentario + '\'' +
                ", gustado=" + gustado +
                ", idPelicula=" + idPelicula +
                ", usuario=" + nomUsuario +
                '}';
    }
}
