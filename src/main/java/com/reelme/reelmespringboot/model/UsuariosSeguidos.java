package com.reelme.reelmespringboot.model;

import jakarta.persistence.*;

@Entity(name = "usuarios_seguidos")
@IdClass(UsuariosSeguidosId.class)
public class UsuariosSeguidos {
    @Id
    @ManyToOne
    @JoinColumn(name = "nombre_usuario")
    private Usuario nombreUsuario;

    @Id
    @ManyToOne
    @JoinColumn(name = "usuario_seguido")
    private Usuario usuarioSeguido;

    public UsuariosSeguidos() {
    }

    public UsuariosSeguidos(Usuario nombreUsuario, Usuario usuarioSeguido) {
        this.nombreUsuario = nombreUsuario;
        this.usuarioSeguido = usuarioSeguido;
    }

    public Usuario getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(Usuario nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Usuario getUsuarioSeguido() {
        return usuarioSeguido;
    }

    public void setUsuarioSeguido(Usuario usuarioSeguido) {
        this.usuarioSeguido = usuarioSeguido;
    }

    @Override
    public String toString() {
        return "UsuariosSeguidos{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", usuarioSeguido='" + usuarioSeguido + '\'' +
                '}';
    }
}
