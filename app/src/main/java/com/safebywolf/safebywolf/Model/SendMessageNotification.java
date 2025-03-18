package com.safebywolf.safebywolf.Model;

public class SendMessageNotification {
    String title;
    String content;
    String image;
    String fmcToken;
    String patente;
    String latitud;
    String longitud;
    String fecha;
    String hora;
    String ciudad;

    public SendMessageNotification() {
    }

    public SendMessageNotification(String title, String content, String image, String fmcToken, String patente, String latitud, String longitud, String fecha, String hora, String ciudad) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.fmcToken = fmcToken;
        this.patente = patente;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
        this.hora = hora;
        this.ciudad = ciudad;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFmcToken() {
        return fmcToken;
    }

    public void setFmcToken(String fmcToken) {
        this.fmcToken = fmcToken;
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
}
