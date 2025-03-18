package com.safebywolf.safebywolf.Model;

import android.graphics.Bitmap;

public class AutoPatenteEnPantalla {
    String patente;
    boolean isPatente = false;
    boolean isAuto = false;
    Bitmap bitmapAuto;
    Bitmap bitmapPatente;

    public AutoPatenteEnPantalla(String patente) {
        this.patente = patente;
    }

    public Bitmap getBitmapAuto() {
        return bitmapAuto;
    }

    public void setBitmapAuto(Bitmap bitmapAuto) {
        this.bitmapAuto = bitmapAuto;
    }

    public Bitmap getBitmapPatente() {
        return bitmapPatente;
    }

    public void setBitmapPatente(Bitmap bitmapPatente) {
        this.bitmapPatente = bitmapPatente;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public boolean isPatente() {
        return isPatente;
    }

    public void setPatente(boolean patente) {
        isPatente = patente;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean auto) {
        isAuto = auto;
    }
}
