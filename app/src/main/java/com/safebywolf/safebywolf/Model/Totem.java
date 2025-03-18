package com.safebywolf.safebywolf.Model;

import java.util.List;

public class Totem {
    String id;
    String emailUsuario;
    String temperaturaCPU;
    String temperaturaBateria;
    String temperaturaBateriaAntesDelReposo;
    String nivelBateria;
    boolean activo;
    String horaUltimaActualizacion;
    boolean inactivoPorTemperatura;
    String latitud;
    String longitud;
    String statusBateria;
    List<String> grupo;
    String longTime;
    String fecha;
    boolean usbCharge;
    boolean acCharge;
    boolean reiniciar=false;
    String lastPatenteEscaneda;
    String ip;
    String ciudad;
    String comuna;
    String tag;


    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public Totem() {
    }

    public Totem(String id, String emailUsuario, String temperaturaCPU, String temperaturaBateria, String nivelBateria, boolean activo, String horaUltimaActualizacion, boolean inactivoPorTemperatura, String latitud, String longitud, String statusBateria, List<String> grupo, String longTime, String fecha, boolean usbCharge, boolean acCharge, boolean reiniciar, String lastPatenteEscaneda, String ip, String ciudad, String comuna, String tag) {
        this.id = id;
        this.emailUsuario = emailUsuario;
        this.temperaturaCPU = temperaturaCPU;
        this.temperaturaBateria = temperaturaBateria;
        this.nivelBateria = nivelBateria;
        this.activo = activo;
        this.horaUltimaActualizacion = horaUltimaActualizacion;
        this.inactivoPorTemperatura = inactivoPorTemperatura;
        this.latitud = latitud;
        this.longitud = longitud;
        this.statusBateria = statusBateria;
        this.grupo = grupo;
        this.longTime = longTime;
        this.fecha = fecha;
        this.usbCharge = usbCharge;
        this.acCharge = acCharge;
        this.reiniciar = reiniciar;
        this.lastPatenteEscaneda = lastPatenteEscaneda;
        this.ip = ip;
        this.ciudad = ciudad;
        this.comuna = comuna;
        this.tag = tag;
    }

    public Totem(String id, String emailUsuario, String ip){
        this.id = id;
        this.emailUsuario = emailUsuario;
        this.ip = ip;
    }

    public Totem(String id, String emailUsuario, List<String> grupo, String temperaturaCPU, String temperaturaBateria, String nivelBateria,String horaUltimaActualizacion,boolean activo, boolean inactivoPorTemperatura, String latitud, String longitud, String statusBateria, String fecha,String longTime, boolean usbCharge, boolean acCharge, String lastPatenteEscaneda, boolean reiniciar, String ip, String tag, String ciudad, String temperaturaBateriaAntesDelReposo) {
        this.id = id;
        this.emailUsuario = emailUsuario;
        this.grupo=grupo;
        this.temperaturaCPU = temperaturaCPU;
        this.temperaturaBateria = temperaturaBateria;
        this.nivelBateria=nivelBateria;
        this.horaUltimaActualizacion=horaUltimaActualizacion;
        this.activo = activo;
        this.inactivoPorTemperatura=inactivoPorTemperatura;
        this.latitud=latitud;
        this.longitud=longitud;
        this.statusBateria=statusBateria;
        this.fecha=fecha;
        this.longTime=longTime;
        this.usbCharge=usbCharge;
        this.acCharge=acCharge;
        this.lastPatenteEscaneda = lastPatenteEscaneda;
        this.reiniciar=reiniciar;
        this.ip = ip;
        this.ciudad = ciudad;
        this.tag = tag;
        this.temperaturaBateriaAntesDelReposo = temperaturaBateriaAntesDelReposo;
    }

    public String getTemperaturaBateriaAntesDelReposo() {
        return temperaturaBateriaAntesDelReposo;
    }

    public void setTemperaturaBateriaAntesDelReposo(String temperaturaBateriaAntesDelReposo) {
        this.temperaturaBateriaAntesDelReposo = temperaturaBateriaAntesDelReposo;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLastPatenteEscaneda() {
        return lastPatenteEscaneda;
    }

    public void setLastPatenteEscaneda(String lastPatenteEscaneda) {
        this.lastPatenteEscaneda = lastPatenteEscaneda;
    }

    public boolean isReiniciar() {
        return reiniciar;
    }

    public void setReiniciar(boolean reiniciar) {
        this.reiniciar = reiniciar;
    }

    public boolean isUsbCharge() {
        return usbCharge;
    }

    public void setUsbCharge(boolean usbCharge) {
        this.usbCharge = usbCharge;
    }

    public boolean isAcCharge() {
        return acCharge;
    }

    public void setAcCharge(boolean acCharge) {
        this.acCharge = acCharge;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLongTime() {
        return longTime;
    }

    public void setLongTime(String longTime) {
        this.longTime = longTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusBateria() {
        return statusBateria;
    }

    public void setStatusBateria(String statusBateria) {
        this.statusBateria = statusBateria;
    }

    public List<String> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<String> grupo) {
        this.grupo = grupo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getNivelBateria() {
        return nivelBateria;
    }

    public void setNivelBateria(String nivelBateria) {
        this.nivelBateria = nivelBateria;
    }

    public String getHoraUltimaActualizacion() {
        return horaUltimaActualizacion;
    }

    public boolean isInactivoPorTemperatura() {
        return inactivoPorTemperatura;
    }

    public void setInactivoPorTemperatura(boolean inactivoPorTemperatura) {
        this.inactivoPorTemperatura = inactivoPorTemperatura;
    }

    public void setHoraUltimaActualizacion(String horaUltimaActualizacion) {
        this.horaUltimaActualizacion = horaUltimaActualizacion;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getTemperaturaCPU() {
        return temperaturaCPU;
    }

    public void setTemperaturaCPU(String temperaturaCPU) {
        this.temperaturaCPU = temperaturaCPU;
    }

    public String getTemperaturaBateria() {
        return temperaturaBateria;
    }

    public void setTemperaturaBateria(String temperaturaBateria) {
        this.temperaturaBateria = temperaturaBateria;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
