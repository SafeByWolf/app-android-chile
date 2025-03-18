package com.safebywolf.safebywolf.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class UsuarioPatente implements Serializable {
    public static String idUsuario;
    public static String patente;

    public UsuarioPatente(String id, String patente) {
        this.idUsuario = id;
        this.patente = patente;
    }

    public UsuarioPatente(JSONObject objetoJSON) throws JSONException {
        this.idUsuario = objetoJSON.getString("usuario_id");
        this.patente = objetoJSON.getString("id");
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }
}
