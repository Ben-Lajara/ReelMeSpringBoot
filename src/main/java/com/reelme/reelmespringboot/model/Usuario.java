package com.reelme.reelmespringboot.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "nombre")
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
    private String apodo;

    private String direccion;

    @OneToMany(mappedBy = "usuarioSeguido")
    private List<UsuariosSeguidos> seguidos;

    @OneToMany(mappedBy = "nombreUsuario")
    private List<UsuariosSeguidos> seguidores;

    @ManyToMany
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(
                    name = "nombreUsuario", referencedColumnName = "nombre"),
            inverseJoinColumns = @JoinColumn(
                    name = "idRol", referencedColumnName = "id"))
    private Set<Rol> roles= new HashSet<>();

    private Date veto;

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

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public Date getVeto() {
        return veto;
    }

    public void setVeto(Date veto) {
        this.veto = veto;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", pword='" + pword + '\'' +
                '}';
    }
}
