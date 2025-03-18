package com.safebywolf.safebywolf.Model;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable {
    public static String id;
    public static String nombre;
    public static String apellido;
    public static String email;
    public static String contacto;
    public static String password;
    public static String fechaNac;
    public static List<String> patentes;
    public String activo;
    public String ip;

    public Usuario(String id, String nombre, String apellido, String fechaNac, String email, String contacto,String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contacto = contacto;
        this.password = password;
        this.fechaNac=fechaNac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public Usuario() {
    }

    public static List<String> getPatentes() {
        return patentes;
    }

    public static void setPatentes(List<String> patentes) {
        Usuario.patentes = patentes;
    }

    public static String getContacto() {
        return contacto;
    }

    public static void setContacto(String contacto) {
        Usuario.contacto = contacto;
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

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }
}



