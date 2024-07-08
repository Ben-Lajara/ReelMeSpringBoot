package com.reelme.reelmespringboot.dto;

import com.reelme.reelmespringboot.model.Rango;

public class AboutDTO {
    private String nombre;
    private String apodo;
    private String direccion;
    private String perfil;
    private String color;
    private String bio;
    private int vistas;
    private int vistasYear;
    private int seguidores;
    private int seguidos;
    private Rango rango;

    public AboutDTO() {
    }

    public AboutDTO(String nombre, String apodo, String direccion, String perfil, String color, String bio, int vistas, int vistasYear, int seguidores, int seguidos, Rango rango) {
        this.nombre = nombre;
        this.apodo = apodo;
        this.direccion = direccion;
        this.perfil = perfil;
        this.color = color;
        this.bio = bio;
        this.vistas = vistas;
        this.vistasYear = vistasYear;
        this.seguidores = seguidores;
        this.seguidos = seguidos;
        this.rango = rango;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }

    public int getVistasYear() {
        return vistasYear;
    }

    public void setVistasYear(int vistasYear) {
        this.vistasYear = vistasYear;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguidos() {
        return seguidos;
    }

    public void setSeguidos(int seguidos) {
        this.seguidos = seguidos;
    }

    public Rango getRango() {
        return rango;
    }

    public void setRango(Rango rango) {
        this.rango = rango;
    }
}
