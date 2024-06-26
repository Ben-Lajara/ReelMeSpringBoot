package com.reelme.reelmespringboot.model;

import jakarta.persistence.*;
import org.apache.catalina.User;

@Entity
public class Denuncia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "denunciante", referencedColumnName = "nombre")
    private Usuario denunciante;

    @ManyToOne
    @JoinColumn(name = "denunciado", referencedColumnName = "nombre")
    private Usuario denunciado;

    @Column(length = 10000)
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "id_resena")
    private Resena idResena;

    @Transient
    private String comentarioResena;

    private String estado;
    public Denuncia() {
    }

    public Denuncia(Usuario denunciante, Usuario denunciado, String motivo, Resena idResena) {
        this.denunciante = denunciante;
        this.denunciado = denunciado;
        this.motivo = motivo;
        this.idResena = idResena;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(Usuario denunciante) {
        this.denunciante = denunciante;
    }

    public Usuario getDenunciado() {
        return denunciado;
    }

    public void setDenunciado(Usuario denunciado) {
        this.denunciado = denunciado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Resena getIdResena() {
        return idResena;
    }

    public void setIdResena(Resena idResena) {
        this.idResena = idResena;
    }

    public String getComentarioResena() {
        return comentarioResena;
    }

    public void setComentarioResena(String comentarioResena) {
        this.comentarioResena = comentarioResena;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
