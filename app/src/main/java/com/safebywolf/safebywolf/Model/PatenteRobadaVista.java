package com.safebywolf.safebywolf.Model;

public class PatenteRobadaVista extends Patente {
    int colorInt;
    boolean isSpeech=false;
    String patenteUsuario;
    String position;
    String longTimeSpeech;
    boolean delete = false;
    String cambiada = "false";

    String urlImagenAmpliada = null;
    String urlImagen = null;

    public PatenteRobadaVista() {
    }

    public PatenteRobadaVista(String id, String patente, String latitud, String longitud, String urlImagen, String fecha, String hora, String ciudad, String direccion, String idUsuario,String nombreUsuario, String apellidoUsuario, String emailUsuario, String contactoUsuario,String patenteUsuario, String longTime, String position, String tipo, boolean tipoAmbas) {
        this.id=id;
        this.patente = patente;
        this.latitud = latitud;
        this.longitud = longitud;
        this.urlImagen = urlImagen;
        this.fecha = fecha;
        this.hora = hora;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.emailUsuario = emailUsuario;
        this.contactoUsuario = contactoUsuario;
        this.longTime=longTime;
        this.tipo=tipo;
        this.tipoAmbas=tipoAmbas;
    }

    public String getUrlImagenAmpliada() {
        return urlImagenAmpliada;
    }

    public void setUrlImagenAmpliada(String urlImagenAmpliada) {
        this.urlImagenAmpliada = urlImagenAmpliada;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public int getColorInt() {
        return colorInt;
    }

    public void setColorInt(int colorInt) {
        this.colorInt = colorInt;
    }

    public boolean isSpeech() {
        return isSpeech;
    }

    public void setSpeech(boolean speech) {
        isSpeech = speech;
    }

    public String getLongTimeSpeech() {
        return longTimeSpeech;
    }

    public void setLongTimeSpeech(String longTimeSpeech) {
        this.longTimeSpeech = longTimeSpeech;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCambiada() {
        return cambiada;
    }

    public void setCambiada(String cambiada) {
        this.cambiada = cambiada;
    }
}
