package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.FieldValue;

import java.util.List;

public class ControlVehicular {
    String lugar;
    String patente;
    String vehiculo;
    String marca;
    String modelo;
    String rut;
    String nombre;
    String fechaDeNacimiento;
    String nacionalidad;
    String servicio;
    String cuadrante;
    FieldValue timeStamp;
    String hora;
    String fecha;
    String carabEmail;
    String carabNombre;
    String carabApellido;
    List<String> carabGrupos;
    String lugarLatitud;
    String lugarLongitud;
    String origen = "app";

    public ControlVehicular() {
    }

    public ControlVehicular(String lugar, String patente, String vehiculo, String marca, String modelo, String rut, String nombre, String fechaDeNacimiento, String nacionalidad, String servicio,
                            String cuadrante, FieldValue timeStamp, String hora,String fecha, String carabEmail, String carabNombre, String carabApellido, List<String> carabGrupos, String lugarLatitud, String lugarLongitud) {
        this.lugar = lugar;
        this.patente = patente;
        this.vehiculo = vehiculo;
        this.marca = marca;
        this.modelo = modelo;
        this.rut = rut;
        this.nombre = nombre;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.nacionalidad = nacionalidad;
        this.servicio = servicio;
        this.cuadrante = cuadrante;
        this.timeStamp = timeStamp;
        this.hora = hora;
        this.fecha = fecha;
        this.carabEmail = carabEmail;
        this.carabNombre = carabNombre;
        this.carabApellido = carabApellido;
        this.carabGrupos = carabGrupos;
        this.lugarLatitud = lugarLatitud;
        this.lugarLongitud = lugarLongitud;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getCuadrante() {
        return cuadrante;
    }

    public void setCuadrante(String cuadrante) {
        this.cuadrante = cuadrante;
    }

    public FieldValue getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(FieldValue timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCarabEmail() {
        return carabEmail;
    }

    public void setCarabEmail(String carabEmail) {
        this.carabEmail = carabEmail;
    }

    public String getCarabNombre() {
        return carabNombre;
    }

    public void setCarabNombre(String carabNombre) {
        this.carabNombre = carabNombre;
    }

    public String getCarabApellido() {
        return carabApellido;
    }

    public void setCarabApellido(String carabApellido) {
        this.carabApellido = carabApellido;
    }

    public List<String> getCarabGrupos() {
        return carabGrupos;
    }

    public void setCarabGrupos(List<String> carabGrupos) {
        this.carabGrupos = carabGrupos;
    }

    public String getLugarLatitud() {
        return lugarLatitud;
    }

    public void setLugarLatitud(String lugarLatitud) {
        this.lugarLatitud = lugarLatitud;
    }

    public String getLugarLongitud() {
        return lugarLongitud;
    }

    public void setLugarLongitud(String lugarLongitud) {
        this.lugarLongitud = lugarLongitud;
    }
}
