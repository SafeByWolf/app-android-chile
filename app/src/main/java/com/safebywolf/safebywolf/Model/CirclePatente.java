package com.safebywolf.safebywolf.Model;

import com.google.android.gms.maps.model.Circle;

public class CirclePatente {
    Circle markerCircle;
    PatenteRobadaVista patenteRobadaVista;

    public CirclePatente(Circle markerCircle, PatenteRobadaVista patenteRobadaVista) {
        this.markerCircle = markerCircle;
        this.patenteRobadaVista = patenteRobadaVista;
    }

    public PatenteRobadaVista getPatenteRobadaVista() {
        return patenteRobadaVista;
    }

    public void setPatenteRobadaVista(PatenteRobadaVista patenteRobadaVista) {
        this.patenteRobadaVista = patenteRobadaVista;
    }

    public Circle getMarkerCircle() {
        return markerCircle;
    }

    public void setMarkerCircle(Circle markerCircle) {
        this.markerCircle = markerCircle;
    }

}
