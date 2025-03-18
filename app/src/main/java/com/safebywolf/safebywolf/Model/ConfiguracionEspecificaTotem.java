package com.safebywolf.safebywolf.Model;

public class ConfiguracionEspecificaTotem {
    private boolean isZoomAutomaticoActivado = true;
    private int zoomMinimo = 12;
    private int tiempoMaximoConZoomAutomaticoSeg = 5;
    private int minimoDeLecturas = 1;
    private String proporcionAlturaMinimaAuto = "0.23";
    private String proporcionAlturaMinimaPatente = "0.041";
    private String proporcionAnchoMinimoAuto = "0.13";
    private String proporcionAnchoMinimoPatente = "0.04";
    private String latitud = "";
    private String longitud = "";
    private String comuna = "";
    private String email = "";
    private String confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem = "";
    private String confianzaMinimaOCRProbabilidadFlotanteTotem = "";

    public ConfiguracionEspecificaTotem() {
    }

    public ConfiguracionEspecificaTotem(String email) {
        this.email = email;
    }

    public String getConfianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem() {
        return confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem;
    }

    public void setConfianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem(String confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem) {
        this.confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem = confianzaMinimaDeteccionPatenteProbabilidadFlotanteTotem;
    }

    public String getConfianzaMinimaOCRProbabilidadFlotanteTotem() {
        return confianzaMinimaOCRProbabilidadFlotanteTotem;
    }

    public void setConfianzaMinimaOCRProbabilidadFlotanteTotem(String confianzaMinimaOCRProbabilidadFlotanteTotem) {
        this.confianzaMinimaOCRProbabilidadFlotanteTotem = confianzaMinimaOCRProbabilidadFlotanteTotem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isZoomAutomaticoActivado() {
        return isZoomAutomaticoActivado;
    }

    public void setZoomAutomaticoActivado(boolean zoomAutomaticoActivado) {
        isZoomAutomaticoActivado = zoomAutomaticoActivado;
    }

    public int getZoomMinimo() {
        return zoomMinimo;
    }

    public void setZoomMinimo(int zoomMinimo) {
        this.zoomMinimo = zoomMinimo;
    }

    public int getTiempoMaximoConZoomAutomaticoSeg() {
        return tiempoMaximoConZoomAutomaticoSeg;
    }

    public void setTiempoMaximoConZoomAutomaticoSeg(int tiempoMaximoConZoomAutomaticoSeg) {
        this.tiempoMaximoConZoomAutomaticoSeg = tiempoMaximoConZoomAutomaticoSeg;
    }

    public int getMinimoDeLecturas() {
        return minimoDeLecturas;
    }

    public void setMinimoDeLecturas(int minimoDeLecturas) {
        this.minimoDeLecturas = minimoDeLecturas;
    }

    public String getProporcionAlturaMinimaAuto() {
        return proporcionAlturaMinimaAuto;
    }

    public void setProporcionAlturaMinimaAuto(String proporcionAlturaMinimaAuto) {
        this.proporcionAlturaMinimaAuto = proporcionAlturaMinimaAuto;
    }

    public String getProporcionAlturaMinimaPatente() {
        return proporcionAlturaMinimaPatente;
    }

    public void setProporcionAlturaMinimaPatente(String proporcionAlturaMinimaPatente) {
        this.proporcionAlturaMinimaPatente = proporcionAlturaMinimaPatente;
    }

    public String getProporcionAnchoMinimoAuto() {
        return proporcionAnchoMinimoAuto;
    }

    public void setProporcionAnchoMinimoAuto(String proporcionAnchoMinimoAuto) {
        this.proporcionAnchoMinimoAuto = proporcionAnchoMinimoAuto;
    }

    public String getProporcionAnchoMinimoPatente() {
        return proporcionAnchoMinimoPatente;
    }

    public void setProporcionAnchoMinimoPatente(String proporcionAnchoMinimoPatente) {
        this.proporcionAnchoMinimoPatente = proporcionAnchoMinimoPatente;
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

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }
}
