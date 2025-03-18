package com.safebywolf.safebywolf.Model;

public class Comuna {
    String comuna;
    String comunaKey;
    String pais;
    String paisKey;
    String region;
    String regionKey;
    boolean activo = false;

    public Comuna() {
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getComunaKey() {
        return comunaKey;
    }

    public void setComunaKey(String comunaKey) {
        this.comunaKey = comunaKey;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPaisKey() {
        return paisKey;
    }

    public void setPaisKey(String paisKey) {
        this.paisKey = paisKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionKey() {
        return regionKey;
    }

    public void setRegionKey(String regionKey) {
        this.regionKey = regionKey;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
