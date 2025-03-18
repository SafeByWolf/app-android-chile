package com.safebywolf.safebywolf.Model;

public class VersionNueva {
    String version;
    String urlDescarga;
    String authority;

    public VersionNueva() {
    }

    public VersionNueva(String version, String urlDescarga) {
        this.version = version;
        this.urlDescarga = urlDescarga;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }
}
