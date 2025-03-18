package com.safebywolf.safebywolf.Model;

import com.google.android.gms.maps.model.Marker;

public class MarkerPatente {
    Marker markerOptions;
    PatenteRobadaVista patenteRobadaVista;

    public MarkerPatente(Marker markerOptions, PatenteRobadaVista patenteRobadaVista) {
        this.markerOptions = markerOptions;
        this.patenteRobadaVista = patenteRobadaVista;
    }

    public Marker getMarkerOptions() {
        return markerOptions;
    }

    public void setMarkerOptions(Marker markerOptions) {
        this.markerOptions = markerOptions;
    }

    public PatenteRobadaVista getPatenteRobadaVista() {
        return patenteRobadaVista;
    }

    public void setPatenteRobadaVista(PatenteRobadaVista patenteRobadaVista) {
        this.patenteRobadaVista = patenteRobadaVista;
    }
}
