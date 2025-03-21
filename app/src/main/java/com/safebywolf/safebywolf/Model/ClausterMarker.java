package com.safebywolf.safebywolf.Model;


import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClausterMarker implements ClusterItem {
    private LatLng position;
    private String title;
    private String snippet;
    private Bitmap iconPicture;
    private UsuarioAndroid usuario;

    public ClausterMarker(LatLng position, String title, String snippet, Bitmap drawable) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.iconPicture = drawable;
    }

    public ClausterMarker() {
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public Bitmap getIconPicture() {
        return iconPicture;
    }

    public void setIconPicture(Bitmap iconPicture) {
        this.iconPicture = iconPicture;
    }

    public UsuarioAndroid getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioAndroid usuario) {
        this.usuario = usuario;
    }
}
