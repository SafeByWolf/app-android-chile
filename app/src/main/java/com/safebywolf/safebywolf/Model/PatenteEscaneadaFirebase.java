package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatenteEscaneadaFirebase {
    private String id;
    private String patente;
    private String longitud = null;
    private String latitud = null;
    private List<String> grupo = new ArrayList<>();
    private List<GroupType> listaGrupoTipo = new ArrayList<>();//grupos de usuario, de patente y de comuna
    private String tipo = null; //robada o lista negra
    private String urlImagen = null;
    private String urlImagenAmpliada = null;
    private String emailUsuario = null;
    private String comuna = null;
    private String fecha = null;
    @ServerTimestamp
    private Date timestamp;
    private Boolean visible=true;

    public PatenteEscaneadaFirebase() {
    }

    public PatenteEscaneadaFirebase(String id, String patente, String longitud, String latitud, List<String> grupo, List<GroupType> listaGrupoTipo, String tipo, String urlImagen, String urlImagenAmpliada, String emailUsuario, String comuna, String fecha) {
        this.id = id;
        this.patente = patente;
        this.longitud = longitud;
        this.latitud = latitud;
        this.grupo = grupo;
        this.listaGrupoTipo = listaGrupoTipo;
        this.tipo = tipo;
        this.urlImagen = urlImagen;
        this.urlImagenAmpliada = urlImagenAmpliada;
        this.emailUsuario = emailUsuario;
        this.comuna = comuna;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public List<String> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<String> grupo) {
        this.grupo = grupo;
    }

    public List<GroupType> getListaGrupoTipo() {
        return listaGrupoTipo;
    }

    public void setListaGrupoTipo(List<GroupType> listaGrupoTipo) {
        this.listaGrupoTipo = listaGrupoTipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getUrlImagenAmpliada() {
        return urlImagenAmpliada;
    }

    public void setUrlImagenAmpliada(String urlImagenAmpliada) {
        this.urlImagenAmpliada = urlImagenAmpliada;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "PatenteEscaneada{" +
                "id=" + id +
                ", patente=" + patente +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", grupo=" + grupo +
                ", grupotipo='" + listaGrupoTipo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", urlimagen='" + urlImagen + '\'' +
                ", urlImagenAmpliada='" + urlImagenAmpliada + '\'' +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", comuna='" + comuna + '\'' +
                ", fecha='" + fecha + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", visible='" + visible + '\'' +
                '}';
    }
}
