package com.reelme.reelmespringboot.model;

import java.io.Serializable;

public class UsuariosSeguidosId implements Serializable {
    private String nombreUsuario;
    private String usuarioSeguido;

    public UsuariosSeguidosId() {
    }

    public UsuariosSeguidosId(String nombreUsuario, String usuarioSeguido) {
        this.nombreUsuario = nombreUsuario;
        this.usuarioSeguido = usuarioSeguido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getUsuarioSeguido() {
        return usuarioSeguido;
    }

    public void setUsuarioSeguido(String usuarioSeguido) {
        this.usuarioSeguido = usuarioSeguido;
    }
}
