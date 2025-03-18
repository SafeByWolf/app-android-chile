package com.safebywolf.safebywolf.Model;

public class PatenteColor {
    String patente;
    int color;

    public PatenteColor(String patente, int color) {
        this.patente = patente;
        this.color = color;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
