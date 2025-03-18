package com.safebywolf.safebywolf.Activity.TensorFlow.Model;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class BitmapRectF {
    Bitmap originalBitmap;
    Bitmap cropBitmap;
    RectF rectF;
    RectF originalRectF;
    String title;
    float confidence;
    boolean matched = false;
    boolean autoContainsPatente = false;
    int sensorOrientation;

    public BitmapRectF(Bitmap originalBitmap, Bitmap cropBitmap, String title, float minimumConfidence,int sensorOrientation) {
        this.originalBitmap = originalBitmap;
        this.cropBitmap = cropBitmap;
        this.title = title;
        this.confidence = minimumConfidence;
        this.sensorOrientation = sensorOrientation;
    }

    public int getSensorOrientation() {
        return sensorOrientation;
    }

    public void setSensorOrientation(int sensorOrientation) {
        this.sensorOrientation = sensorOrientation;
    }

    public boolean isAutoContainsPatente() {
        return autoContainsPatente;
    }

    public void setAutoContainsPatente(boolean autoContainsPatente) {
        this.autoContainsPatente = autoContainsPatente;
    }

    public RectF getOriginalRectF() {
        return originalRectF;
    }

    public void setOriginalRectF(RectF originalRectF) {
        this.originalRectF = originalRectF;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getOriginalBitmap() {
        return originalBitmap;
    }

    public void setOriginalBitmap(Bitmap originalBitmap) {
        this.originalBitmap = originalBitmap;
    }

    public Bitmap getCropBitmap() {
        return cropBitmap;
    }

    public void setCropBitmap(Bitmap cropBitmap) {
        this.cropBitmap = cropBitmap;
    }

    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }
}
