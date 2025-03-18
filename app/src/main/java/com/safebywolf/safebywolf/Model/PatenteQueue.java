package com.safebywolf.safebywolf.Model;

import com.safebywolf.safebywolf.Activity.TensorFlow.Model.BitmapRectF;
import com.safebywolf.safebywolf.Class.Utils.CurrentDate;
import com.google.mlkit.vision.text.Text;

import java.util.Date;

public class PatenteQueue {
    BitmapRectF bitmapRectF;
    CurrentDate currentDate;
    PatenteEscaneada patenteEscaneada;

    public PatenteQueue(PatenteEscaneada patenteEscaneada, BitmapRectF bitmapRectF) {
        this.bitmapRectF = bitmapRectF;
        this.currentDate = new CurrentDate(new Date());
        this.patenteEscaneada = patenteEscaneada;
    }

    public PatenteEscaneada getPatenteEscaneada() {
        return patenteEscaneada;
    }

    public void setPatenteEscaneada(PatenteEscaneada patenteEscaneada) {
        this.patenteEscaneada = patenteEscaneada;
    }

    public BitmapRectF getBitmapRectF() {
        return bitmapRectF;
    }

    public void setBitmapRectF(BitmapRectF bitmapRectF) {
        this.bitmapRectF = bitmapRectF;
    }

    public CurrentDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(CurrentDate currentDate) {
        this.currentDate = currentDate;
    }
}
