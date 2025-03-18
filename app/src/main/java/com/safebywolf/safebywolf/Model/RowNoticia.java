package com.safebywolf.safebywolf.Model;

import java.util.ArrayList;
import java.util.Date;

public class RowNoticia {
    String id;
    String titulo;
    String contenido;
    ArrayList<String> imagenes;
    String autor;
    Date timestamp;

    public RowNoticia() {
    }

    public RowNoticia(String id, String titulo, String contenido, ArrayList<String> imagenes, String autor, Date timestamp) {
        this.id = id;
        this.titulo = titulo;
        this.contenido = contenido;
        this.imagenes = imagenes;
        this.autor = autor;
        this.timestamp = timestamp;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }
}
