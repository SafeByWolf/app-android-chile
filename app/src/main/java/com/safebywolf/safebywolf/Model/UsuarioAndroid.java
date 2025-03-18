package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UsuarioAndroid {
    private String id;
    private String accountVersion;
    private String nombre;
    private String apellido;
    private String email;
    private String contacto;
    private String password;
    private List<String> patentes;
    private List<String> grupo = new ArrayList<>();
    private List<Map<String, String>> grupos = new ArrayList<>();
    private String activo;
    private Date expiredSession;
    private Date ultimaConexion;
    private Date ultimoIngreso;
    private String tokenFirebaseInstalation;
    private String tokenFirebaseMessaging;
    private String tokenFirebaseSession;
    private String ip = "";
    private String tag = "";
    private boolean totem = false;
    private String modelType;
    private boolean autoSinPatente = false;
    private String tokenApp;

    private boolean soap = false;

    @ServerTimestamp
    private Date timestamp;

    public UsuarioAndroid(String id, String nombre, String apellido, String email, String contacto,String password, String modelType, String accountVersion) {
        this.id = id;
        this.accountVersion = accountVersion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contacto = contacto;
        this.password = password;
        this.patentes = patentes;
        this.modelType = modelType;
    }

    public String getTokenApp() {
        return tokenApp;
    }

    public void setTokenApp(String tokenApp) {
        this.tokenApp = tokenApp;
    }

    public String getAccountVersion() {
        return accountVersion;
    }

    public void setAccountVersion(String accountVersion) {
        this.accountVersion = accountVersion;
    }

    public boolean isSoap() {
        return soap;
    }

    public void setSoap(boolean soap) {
        this.soap = soap;
    }

    public List<Map<String, String>> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Map<String, String>> grupos) {
        this.grupos = grupos;
    }

    public boolean isTotem() {
        return totem;
    }

    public void setTotem(boolean totem) {
        this.totem = totem;
    }

    public boolean isAutoSinPatente() {
        return autoSinPatente;
    }

    public void setAutoSinPatente(boolean autoSinPatente) {
        this.autoSinPatente = autoSinPatente;
    }
    
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getExpiredSession() {
        return expiredSession;
    }

    public void setExpiredSession(Date expiredSession) {
        this.expiredSession = expiredSession;
    }

    public String getTokenFirebaseInstalation() {
        return tokenFirebaseInstalation;
    }

    public void setTokenFirebaseInstalation(String tokenFirebaseInstalation) {
        this.tokenFirebaseInstalation = tokenFirebaseInstalation;
    }

    public String getTokenFirebaseMessaging() {
        return tokenFirebaseMessaging;
    }

    public void setTokenFirebaseMessaging(String tokenFirebaseMessaging) {
        this.tokenFirebaseMessaging = tokenFirebaseMessaging;
    }

    public String getTokenFirebaseSession() {
        return tokenFirebaseSession;
    }

    public void setTokenFirebaseSession(String tokenFirebaseSession) {
        this.tokenFirebaseSession = tokenFirebaseSession;
    }

    public UsuarioAndroid() {
    }

    public Date getUltimaConexion() {
        return ultimaConexion;
    }

    public void setUltimaConexion(Date ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }

    public Date getUltimoIngreso() {
        return ultimoIngreso;
    }

    public void setUltimoIngreso(Date ultimoIngreso) {
        this.ultimoIngreso = ultimoIngreso;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getPatentes() {
        return patentes;
    }

    public void setPatentes(List<String> patentes) {
        this.patentes = patentes;
    }

    public List<String> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<String> grupo) {
        this.grupo = grupo;
    }

}
