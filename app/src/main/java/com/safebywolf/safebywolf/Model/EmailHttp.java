package com.safebywolf.safebywolf.Model;

public class EmailHttp {

    private String type;
    private String mensaje;
    private String email;
    private String correoContacto;
    private String telefonoContacto;

    public EmailHttp(String type, String mensaje, String email, String correoContacto, String telefonoContacto) {
        this.type = type;
        this.mensaje = mensaje;
        this.email = email;
        this.correoContacto = correoContacto;
        this.telefonoContacto = telefonoContacto;
    }

    public String getTipoContacto() {
        return type;
    }

    public void setTipoContacto(String tipoContacto) {
        this.type = tipoContacto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEmisor() {
        return email;
    }

    public void setEmisor(String emisor) {
        this.email = emisor;
    }

    public String getCorreoContacto() {
        return correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }
}
