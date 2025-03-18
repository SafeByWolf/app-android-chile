package com.safebywolf.safebywolf.Activity.TensorFlow.Model;

import android.graphics.RectF;

public class PatenteOCRRectF {
    String patente;
    RectF rectF;
    float confianzaOCR;

    public PatenteOCRRectF(String patente, RectF rectF, float confianzaOCR) {
        this.patente = patente;
        this.rectF = rectF;
        this.confianzaOCR = confianzaOCR;
    }

    public float getConfianzaOCR() {
        return confianzaOCR;
    }

    public void setConfianzaOCR(float confianzaOCR) {
        this.confianzaOCR = confianzaOCR;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }
}
