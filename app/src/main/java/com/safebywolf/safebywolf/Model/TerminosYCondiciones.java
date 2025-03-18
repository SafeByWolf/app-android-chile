package com.safebywolf.safebywolf.Model;

public class TerminosYCondiciones {
    String titulo;
    String contenido;

    public TerminosYCondiciones() {
    }

    public TerminosYCondiciones(String titulo, String contenido) {
        this.titulo = titulo;
        this.contenido = contenido;
    }

    public String getTitulo() {
        return titulo;
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
}
