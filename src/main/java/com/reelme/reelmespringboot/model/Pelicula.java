package com.reelme.reelmespringboot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Pelicula {
    @Id
    private String id;
    private String titulo;
    private String year;
    private String foto;


    public Pelicula() {
    }

    public Pelicula(String id, String titulo, String year, String foto) {
        this.id = id;
        this.titulo = titulo;
        this.year = year;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Pelicula{" +
                "id='" + id + '\'' +
                ", titulo='" + titulo + '\'' +
                ", year='" + year + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
