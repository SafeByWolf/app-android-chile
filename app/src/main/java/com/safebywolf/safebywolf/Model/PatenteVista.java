package com.safebywolf.safebywolf.Model;

public class PatenteVista {
        String patente;
        String latitud;
        String longitud;
        String urlImagen;
        String fecha;
        String hora;
        String ciudad;
        String idUsuario;
        String patenteUsuario;
        String longTime;

    public PatenteVista() {
    }

    public PatenteVista(String patente, String latitud, String longitud, String urlImagen, String fecha, String hora, String ciudad, String idUsuario, String patenteUsuario, String longTime) {
        this.patente = patente;
        this.latitud = latitud;
        this.longitud = longitud;
        this.urlImagen = urlImagen;
        this.fecha = fecha;
        this.hora = hora;
        this.ciudad = ciudad;
        this.idUsuario = idUsuario;
        this.patenteUsuario = patenteUsuario;
        this.longTime=longTime;
    }

    public String getLongTime() {
        return longTime;
    }

    public void setLongTime(String longTime) {
        this.longTime = longTime;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
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

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPatenteUsuario() {
        return patenteUsuario;
    }

    public void setPatenteUsuario(String patenteUsuario) {
        this.patenteUsuario = patenteUsuario;
    }
}
