package com.safebywolf.safebywolf.Model;

public class GroupType {

    private String nombre;
    private String tipo;
    private String tipoDeBase;

    public GroupType(){

    }

    public GroupType(String nombre, String tipo, String tipoDeBase) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tipoDeBase = tipoDeBase;
    }

    public String getTipoDeBase() {
        return tipoDeBase;
    }

    public void setTipoDeBase(String tipoDeBase) {
        this.tipoDeBase = tipoDeBase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
