package com.safebywolf.safebywolf.Model;

public class PatenteSensitive {
    String patente, vehiculo, marca, modelo, rutAsociado, nMotor, anio, duenio, nChasis, multas, color;

    public PatenteSensitive() {
    }

    public PatenteSensitive(String patente, String vehiculo, String marca, String modelo, String rutAsociado, String nMotor, String anio, String duenio, String nChasis, String multas, String color) {
        this.patente = patente;
        this.vehiculo = vehiculo;
        this.marca = marca;
        this.modelo = modelo;
        this.rutAsociado = rutAsociado;
        this.nMotor = nMotor;
        this.anio = anio;
        this.duenio = duenio;
        this.nChasis = nChasis;
        this.multas = multas;
        this.color = color;
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

    public String getRutAsociado() {
        return rutAsociado;
    }

    public void setRutAsociado(String rutAsociado) {
        this.rutAsociado = rutAsociado;
    }

    public String getnMotor() {
        return nMotor;
    }

    public void setnMotor(String nMotor) {
        this.nMotor = nMotor;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getDuenio() {
        return duenio;
    }

    public void setDuenio(String duenio) {
        this.duenio = duenio;
    }

    public String getnChasis() {
        return nChasis;
    }

    public void setnChasis(String nChasis) {
        this.nChasis = nChasis;
    }

    public String getMultas() {
        return multas;
    }

    public void setMultas(String multas) {
        this.multas = multas;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
