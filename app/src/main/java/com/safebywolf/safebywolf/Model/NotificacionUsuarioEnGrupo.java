package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class NotificacionUsuarioEnGrupo {
    String id;
    String grupo;
    String tipo;
    String idUsuario;
    String email;
    @ServerTimestamp
    private Date timestamp;
    String aceptado;

    public NotificacionUsuarioEnGrupo() {
    }

    public NotificacionUsuarioEnGrupo(String id, String grupo, String idUsuario, String email, String aceptado) {
        this.id = id;
        this.grupo = grupo;
        this.idUsuario = idUsuario;
        this.email = email;
        this.aceptado = aceptado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAceptado() {
        return aceptado;
    }

    public void setAceptado(String aceptado) {
        this.aceptado = aceptado;
    }
}
