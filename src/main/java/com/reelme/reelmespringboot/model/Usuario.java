package com.reelme.reelmespringboot.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Usuario {
    @Id
    private String nombre;

    private String email;
    private String pword;

    private String perfil;

    @Enumerated(EnumType.STRING)
    private Rango rango;

    private String bio;

    private String color;

    @OneToMany(mappedBy = "usuarioSeguido")
    private List<UsuariosSeguidos> seguidos;

    @OneToMany(mappedBy = "nombreUsuario")
    private List<UsuariosSeguidos> seguidores;



    public Usuario() {
    }

    public Usuario(String nombre, String pword) {
        this.nombre = nombre;
        this.pword = pword;
    }

    public Usuario(String nombre, String pword, String perfil) {
        this.nombre = nombre;
        this.pword = pword;
        this.perfil = perfil;
    }

    public Usuario(String nombre, String email, String pword, String perfil) {
        this.nombre = nombre;
        this.email = email;
        this.pword = pword;
        this.perfil = perfil;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public Rango getRango() {
        return rango;
    }

    public void setRango(Rango rango) {
        this.rango = rango;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", pword='" + pword + '\'' +
                '}';
    }
}
