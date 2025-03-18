package com.safebywolf.safebywolf.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ImagenPatente implements Serializable {
    String id;
    String patente;
    String url;
    String fecha;
    String idUsuario;
    String latitud;
    String longitud;
    Bitmap bitmap;

    public ImagenPatente() {
    }

    public ImagenPatente(String id, String patente, String url, String fecha, String idUsuario, String latitud, String longitud) {
        this.id = id;
        this.patente=patente;
        this.url = url;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
