package com.reelme.reelmespringboot.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;

public class DenunciaDTO {
    private Long id;

    private String denunciante;

    private String denunciado;

    @Column(length = 10000)
    private String motivo;

    private Long idResena;

    @Transient
    private String comentarioResena;

    private String estado;
    public DenunciaDTO() {
    }

    public DenunciaDTO(String denunciante, String denunciado, String motivo, Long idResena) {
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

    public Long getIdResena() {
        return idResena;
    }

    public void setIdResena(Long idResena) {
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
