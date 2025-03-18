package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EncuestaPatrullero implements Serializable {
    String id;
    String correoVigia;
    String patente;
    String observacion;
    Boolean contestada;
    List<String> grupos;
    String imagenPrincipalVehiculo;
    String imagenEncuestaObservacion;
    Integer intentos;
    Boolean finalizado;
    Boolean recuperado;
    String respuesta;

    public EncuestaPatrullero(
            String id,
            String correoVigia,
            String patente,
            String observacion,
            Boolean contestada,
            List<String> grupos,
            String imagenPrincipalVehiculo,
            Integer intentos,
            Boolean finalizado,
            Boolean recuperado,
            String respuesta
    ) {
        this.id = id;
        this.correoVigia = correoVigia;
        this.patente = patente;
        this.observacion = observacion;
        this.contestada = contestada;
        this.grupos = grupos;
        this.imagenPrincipalVehiculo = imagenPrincipalVehiculo;
        this.imagenEncuestaObservacion = "";
        this.intentos = intentos;
        this.finalizado = finalizado;
        this.recuperado = recuperado;
        this.respuesta = respuesta;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getCorreoVigia() {
        return correoVigia;
    }

    public void setCorreoVigia(String correoVigia) {
        this.correoVigia = correoVigia;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getContestada() {
        return contestada;
    }

    public void setContestada(Boolean contestada) {
        this.contestada = contestada;
    }

    public List<String> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<String> grupos) {
        this.grupos = grupos;
    }

    public String getImagenPrincipalVehiculo() {
        return imagenPrincipalVehiculo;
    }

    public void setImagenPrincipalVehiculo(String imagenPrincipalVehiculo) {
        this.imagenPrincipalVehiculo = imagenPrincipalVehiculo;
    }

    public String getImagenEncuestaObservacion() {
        return imagenEncuestaObservacion;
    }

    public void setImagenEncuestaObservacion(String imagenEncuestaObservacion) {
        this.imagenEncuestaObservacion = imagenEncuestaObservacion;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public Boolean getFinalizado() {return finalizado;}

    public void setFinalizado(Boolean finalizado) {this.finalizado = finalizado;}

    public void setRecuperado(Boolean recuperado) {this.recuperado = recuperado;}

    public Boolean getRecuperado() {return recuperado;}

    public void setRespuesta(String respuesta) {this.respuesta = respuesta;}

    public String getRespuesta() {return this.respuesta;}

    @Override
    public String toString() {
        return "EncuestaPatrullero{" +
                "id='" + id + '\'' +
                ", correoVigia='" + correoVigia + '\'' +
                ", patente='" + patente + '\'' +
                ", observacion='" + observacion + '\'' +
                ", contestada=" + contestada +
                ", grupos='" + grupos + '\'' +
                ", imagenPrincipalVehiculo='" + imagenPrincipalVehiculo + '\'' +
                ", imagenEncuestaObservacion='" + imagenEncuestaObservacion + '\'' +
                ", intentos=" + intentos +
                ", finalizado=" + finalizado +
                '}';
    }
}
