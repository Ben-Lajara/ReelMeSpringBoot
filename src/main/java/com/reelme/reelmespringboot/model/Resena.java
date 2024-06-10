package com.reelme.reelmespringboot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
public class Resena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fecha;
    private float calificacion;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String comentario;
    private boolean gustado;
    private boolean spoiler;

    @ManyToOne
    @JoinColumn(name = "id_pelicula")
    private Pelicula idPelicula;

    @ManyToOne
    @JoinColumn(name = "usuario")
    private Usuario nomUsuario;

    private boolean denunciada;

    @OneToMany(mappedBy = "resena", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Revisionado> revisionados;

    @Transient
    private boolean isRevisionado;

    public Resena() {
    }

    public Resena(Long id, Date fecha, float calificacion, String comentario, boolean gustado, Pelicula idPelicula, Usuario nomUsuario) {
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

    public Resena(Date fecha, float calificacion, String comentario, boolean gustado, boolean spoiler, Pelicula idPelicula, Usuario nomUsuario) {
        this.fecha = fecha;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.gustado = gustado;
        this.idPelicula = idPelicula;
        this.nomUsuario = nomUsuario;
        this.spoiler = spoiler;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isDenunciada() {
        return denunciada;
    }

    public void setDenunciada(boolean denunciada) {
        this.denunciada = denunciada;
    }

    public List<Revisionado> getRevisionados() {
        return revisionados;
    }

    public void setRevisionados(Revisionado revisionado) {
        this.revisionados.add(revisionado);
    }


    public boolean isRevisionado() {
        return isRevisionado;
    }

    public void setRevisionado(boolean revisionado) {
        isRevisionado = revisionado;
    }

    public boolean isSpoiler() {
        return spoiler;
    }

    public void setSpoiler(boolean spoiler) {
        this.spoiler = spoiler;
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
