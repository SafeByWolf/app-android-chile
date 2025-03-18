package com.safebywolf.safebywolf.Model;

import java.util.Date;

public class TokenApp {
    String id;
    String accountVersion;
    String descripcion;
    String nombreEncargadoToken;
    String emailEncargadoToken;
    Date expirationDate;
    Date timestamp;
    String nombreToken;
    String token;
    int usos;
    int usuarios;
    boolean visible;

    public TokenApp() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountVersion() {
        return accountVersion;
    }

    public void setAccountVersion(String accountVersion) {
        this.accountVersion = accountVersion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreEncargadoToken() {
        return nombreEncargadoToken;
    }

    public void setNombreEncargadoToken(String nombreEncargadoToken) {
        this.nombreEncargadoToken = nombreEncargadoToken;
    }

    public String getEmailEncargadoToken() {
        return emailEncargadoToken;
    }

    public void setEmailEncargadoToken(String emailEncargadoToken) {
        this.emailEncargadoToken = emailEncargadoToken;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNombreToken() {
        return nombreToken;
    }

    public void setNombreToken(String nombreToken) {
        this.nombreToken = nombreToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUsos() {
        return usos;
    }

    public void setUsos(int usos) {
        this.usos = usos;
    }

    public int getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(int usuarios) {
        this.usuarios = usuarios;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
