package com.safebywolf.safebywolf.Model;

import java.util.Date;

public class UrlImagenPatenteApi {
    private String id;
    private String urlImagen;
    private Date timestamp;
    private String patente;
    private boolean isAmpliada;

    public UrlImagenPatenteApi(String id, String urlImagen, String patente, boolean isAmpliada, Date timestamp) {
        this.id = id;
        this.urlImagen = urlImagen;
        this.patente = patente;
        this.isAmpliada = isAmpliada;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public boolean getIsAmpliada() {
        return isAmpliada;
    }

    public void setIsAmpliada(boolean  isAmpliada) {
        this.isAmpliada = isAmpliada;
    }
}
