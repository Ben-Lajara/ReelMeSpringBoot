package com.reelme.reelmespringboot.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Usuario {
    @Id
    private String nombre;
    private String pword;

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

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", pword='" + pword + '\'' +
                '}';
    }
}
