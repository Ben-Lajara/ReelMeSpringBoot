package com.reelme.reelmespringboot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Revisionado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaRevisionado;

    @ManyToOne
    @JoinColumn(name = "resena_id")
    @JsonIgnore
    private Resena resena;

    @Column(length = 10000)
    private String comentarioRevisionado;

    public Revisionado() {
    }

    public Revisionado(Date fechaRevisionado, Resena resena) {
        this.fechaRevisionado = fechaRevisionado;
        this.resena = resena;
    }

    public Revisionado(Date fechaRevisionado, Resena resena, String comentarioRevisionado) {
        this.fechaRevisionado = fechaRevisionado;
        this.resena = resena;
        this.comentarioRevisionado = comentarioRevisionado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaRevisionado() {
        return fechaRevisionado;
    }

    public void setFechaRevisionado(Date fechaRevisionado) {
        this.fechaRevisionado = fechaRevisionado;
    }

    public Resena getResena() {
        return resena;
    }

    public void setResena(Resena resena) {
        this.resena = resena;
    }

    public String getComentarioRevisionado() {
        return comentarioRevisionado;
    }

    public void setComentarioRevisionado(String comentarioRevisionado) {
        this.comentarioRevisionado = comentarioRevisionado;
    }
}
