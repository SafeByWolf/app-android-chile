package com.safebywolf.safebywolf.Model;

public class RutSensitive {
    String nombre, rut, sexo, direccion, ciudad;

    public RutSensitive() {
    }

    public RutSensitive(String nombre, String rut, String sexo, String direccion, String ciudad) {
        this.nombre = nombre;
        this.rut = rut;
        this.sexo = sexo;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
