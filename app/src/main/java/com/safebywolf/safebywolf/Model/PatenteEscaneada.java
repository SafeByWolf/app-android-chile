package com.safebywolf.safebywolf.Model;

import java.util.List;

public class PatenteEscaneada extends Patente {
    int cantidad;
    Long tiempoInicial = 0l;
    Long tiempoFinal;
    double tiempoTotal;
    String bound;
    String boundTop;
    String boundBottom;
    String boundLeft;
    String boundRight;
    String zoom;
    String velocidad;
    String positionX;
    String positionY;
    Long lastSetHeadTime = 0l;
    Long lastHeadTime = 0l;
    boolean delete = false;

    boolean escaneada;
    boolean consultada = false;

    String previewWidth;
    String previewHeight;
    String detectionWidth;
    String detectionHeight;

    boolean recuperado = false;
    String cambiada = "false";

    String urlImagenAmpliada = null;
    String urlImagen = null;

    int numFrame;

    String responseApiGoogle = null;
    boolean greaterDetectionPatente = false;
    boolean greaterDetectionAuto = false;

    public PatenteEscaneada() {
    }

    public PatenteEscaneada(String patente, String latitud, String longitud) {
        this.patente = patente;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public PatenteEscaneada(String patente, String latitud, String longitud, int numFrame, String titleImagenAmpliada, int confianzaOCR) {
        this.patente = patente;
        this.latitud = latitud;
        this.longitud = longitud;
        this.numFrame = numFrame;
        this.titleImagenAmpliada = titleImagenAmpliada;
        this.confianzaOCR = confianzaOCR;
    }

    public PatenteEscaneada(String patente, Long tiempoInicial, List<String> grupo, String emailUsuario, String nombreUsuario, String apellidoUsuario, String contactoUsuario) {
        this.patente = patente;
        this.tiempoInicial = tiempoInicial;
        this.emailUsuario = emailUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.contactoUsuario = contactoUsuario;
        this.grupo = grupo;
    }

    public boolean isGreaterDetectionAuto() {
        return greaterDetectionAuto;
    }

    public void setGreaterDetectionAuto(boolean greaterDetectionAuto) {
        this.greaterDetectionAuto = greaterDetectionAuto;
    }

    public boolean isGreaterDetectionPatente() {
        return greaterDetectionPatente;
    }

    public void setGreaterDetectionPatente(boolean greaterDetectionPatente) {
        this.greaterDetectionPatente = greaterDetectionPatente;
    }

    public int getNumFrame() {
        return numFrame;
    }

    public void setNumFrame(int numFrame) {
        this.numFrame = numFrame;
    }

    public String getUrlImagenAmpliada() {
        return urlImagenAmpliada;
    }

    public void setUrlImagenAmpliada(String urlImagenAmpliada) {
        this.urlImagenAmpliada = urlImagenAmpliada;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getCambiada() {
        return cambiada;
    }

    public void setCambiada(String cambiada) {
        this.cambiada = cambiada;
    }

    public boolean isRecuperado() {
        return recuperado;
    }

    public void setRecuperado(boolean recuperado) {
        this.recuperado = recuperado;
    }

    public String getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(String previewWidth) {
        this.previewWidth = previewWidth;
    }

    public String getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(String previewHeight) {
        this.previewHeight = previewHeight;
    }

    public String getDetectionWidth() {
        return detectionWidth;
    }

    public void setDetectionWidth(String detectionWidth) {
        this.detectionWidth = detectionWidth;
    }

    public String getDetectionHeight() {
        return detectionHeight;
    }

    public void setDetectionHeight(String detectionHeight) {
        this.detectionHeight = detectionHeight;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isConsultada() {
        return consultada;
    }

    public void setConsultada(boolean consultada) {
        this.consultada = consultada;
    }

    public boolean isEscaneada() {
        return escaneada;
    }

    public void setEscaneada(boolean escaneada) {
        this.escaneada = escaneada;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Long getTiempoInicial() {
        return tiempoInicial;
    }

    public void setTiempoInicial(Long tiempoInicial) {
        this.tiempoInicial = tiempoInicial;
    }

    public Long getTiempoFinal() {
        return tiempoFinal;
    }

    public void setTiempoFinal(Long tiempoFinal) {
        this.tiempoFinal = tiempoFinal;
    }

    public double getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(double tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getBoundTop() {
        return boundTop;
    }

    public void setBoundTop(String boundTop) {
        this.boundTop = boundTop;
    }

    public String getBoundBottom() {
        return boundBottom;
    }

    public void setBoundBottom(String boundBottom) {
        this.boundBottom = boundBottom;
    }

    public String getBoundLeft() {
        return boundLeft;
    }

    public void setBoundLeft(String boundLeft) {
        this.boundLeft = boundLeft;
    }

    public String getBoundRight() {
        return boundRight;
    }

    public void setBoundRight(String boundRight) {
        this.boundRight = boundRight;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public Long getLastHeadTime() {
        return lastHeadTime;
    }

    public void setLastHeadTime(Long lastHeadTime) {
        this.lastHeadTime = lastHeadTime;
    }

    public Long getLastSetHeadTime() {
        return lastSetHeadTime;
    }

    public void setLastSetHeadTime(Long lastSetHeadTime) {
        this.lastSetHeadTime = lastSetHeadTime;
    }

    public String getPositionX() {
        return positionX;
    }

    public void setPositionX(String positionX) {
        this.positionX = positionX;
    }

    public String getPositionY() {
        return positionY;
    }

    public void setPositionY(String positionY) {
        this.positionY = positionY;
    }

    public String getResponseApiGoogle() {return responseApiGoogle;}

    public void setResponseApiGoogle(String response) {this.responseApiGoogle = response;}

    @Override
    public String toString() {
        return "PatenteEscaneada{" +
                "cantidad=" + cantidad +
                ", tiempoInicial=" + tiempoInicial +
                ", tiempoFinal=" + tiempoFinal +
                ", tiempoTotal=" + tiempoTotal +
                ", bound='" + bound + '\'' +
                ", boundTop='" + boundTop + '\'' +
                ", boundBottom='" + boundBottom + '\'' +
                ", boundLeft='" + boundLeft + '\'' +
                ", boundRight='" + boundRight + '\'' +
                ", zoom='" + zoom + '\'' +
                ", velocidad='" + velocidad + '\'' +
                ", positionX='" + positionX + '\'' +
                ", positionY='" + positionY + '\'' +
                ", lastSetHeadTime=" + lastSetHeadTime +
                ", lastHeadTime=" + lastHeadTime +
                ", delete=" + delete +
                ", isEscaneada=" + escaneada +
                ", isConsultada=" + consultada +
                ", previewWidth='" + previewWidth + '\'' +
                ", previewHeight='" + previewHeight + '\'' +
                ", detectionWidth='" + detectionWidth + '\'' +
                ", detectionHeight='" + detectionHeight + '\'' +
                ", recuperado=" + recuperado +
                ", cambiada='" + cambiada + '\'' +
                ", urlImagenAmpliada='" + urlImagenAmpliada + '\'' +
                ", urlImagen='" + urlImagen + '\'' +
                ", numFrame=" + numFrame +
                ", responseApiGoogle='" + responseApiGoogle + '\'' +
                '}';
    }
}
