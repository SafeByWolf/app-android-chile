package com.safebywolf.safebywolf.Model;

public class RowPatenteEscaneada {
    String patente;
    String confianza;
    boolean greaterDetectionPatente = false;
    boolean greaterDetectionAuto = false;

    public RowPatenteEscaneada() {
    }

    public RowPatenteEscaneada(String patente, String confianza, boolean greaterDetectionPatente, boolean greaterDetectionAuto) {
        this.patente = patente;
        this.confianza = confianza;
        this.greaterDetectionPatente = greaterDetectionPatente;
        this.greaterDetectionAuto = greaterDetectionAuto;
    }

    public boolean isGreaterDetectionAuto() {
        return greaterDetectionAuto;
    }

    public void setGreaterDetectionAuto(boolean greaterDetectionAuto) {
        this.greaterDetectionAuto = greaterDetectionAuto;
    }

    public boolean isGreaterDetectionPatente() {
        return greaterDetectionPatente;
    }

    public void setGreaterDetectionPatente(boolean greaterDetectionPatente) {
        this.greaterDetectionPatente = greaterDetectionPatente;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getConfianza() {
        return confianza;
    }

    public void setConfianza(String confianza) {
        this.confianza = confianza;
    }
}
