package com.reelme.reelmespringboot.model;

import jakarta.persistence.*;
import org.apache.catalina.User;

@Entity
public class Denuncia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String denunciante;

    private String denunciado;

    @Column(length = 10000)
    private String motivo;

    private int idResena;

    @Transient
    private String comentarioResena;

    private String estado;
    public Denuncia() {
    }

    public Denuncia(String denunciante, String denunciado, String motivo, int idResena) {
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

    public String getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(String denunciante) {
        this.denunciante = denunciante;
    }

    public String getDenunciado() {
        return denunciado;
    }

    public void setDenunciado(String denunciado) {
        this.denunciado = denunciado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getIdResena() {
        return idResena;
    }

    public void setIdResena(int idResena) {
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
