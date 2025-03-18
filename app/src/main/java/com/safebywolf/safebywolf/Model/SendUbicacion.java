package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class SendUbicacion {
    String id;
    String currentLatitud;
    String currentLongitud;
    String latitud;
    String longitud;
    String fecha;
    String hora;
    String longTime;
    List<String> patentes;
    String idUsuario;
    String tokenDeviceFirebase;
    List<String> grupo;
    String correo;
    String nombre;
    String apellido;
    boolean locationAvaible;
    //marca de tiempo de llegada al servidor
    @ServerTimestamp
    private Date timestamp;

    public String getCurrentLatitud() {
        return currentLatitud;
    }

    public void setCurrentLatitud(String currentLatitud) {
        this.currentLatitud = currentLatitud;
    }

    public String getCurrentLongitud() {
        return currentLongitud;
    }

    public void setCurrentLongitud(String currentLongitud) {
        this.currentLongitud = currentLongitud;
    }

    public SendUbicacion(){}

    public SendUbicacion(String id, String correo, String nombre, String apellido, String latitud, String longitud, String fecha, String hora, String longTime, List<String> patentes, String idUsuario, List<String> grupo, String tokenDeviceFirebase, boolean locationAvaible, String currentLatitud, String currentLongitud) {
        this.id = id;
        this.correo=correo;
        this.nombre=nombre;
        this.apellido=apellido;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
        this.hora = hora;
        this.longTime = longTime;
        this.patentes = patentes;
        this.idUsuario = idUsuario;
        this.grupo=grupo;
        this.tokenDeviceFirebase=tokenDeviceFirebase;
        this.locationAvaible=locationAvaible;
        this.currentLatitud = currentLatitud;
        this.currentLongitud = currentLongitud;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isLocationAvaible() {
        return locationAvaible;
    }

    public void setLocationAvaible(boolean locationAvaible) {
        this.locationAvaible = locationAvaible;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public List<String> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<String> grupo) {
        this.grupo = grupo;
    }

    public String getTokenDeviceFirebase() {
        return tokenDeviceFirebase;
    }

    public void setTokenDeviceFirebase(String tokenDeviceFirebase) {
        this.tokenDeviceFirebase = tokenDeviceFirebase;
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

    public String getLongTime() {
        return longTime;
    }

    public void setLongTime(String longTime) {
        this.longTime = longTime;
    }

    public List<String> getPatentes() {
        return patentes;
    }

    public void setPatentes(List<String> patentes) {
        this.patentes = patentes;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
