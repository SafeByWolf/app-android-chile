package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class PatenteListaNegra {
    String nombreUsuario;
    String apellidoUsuario;
    String correoUsuario;
    String contactoUsuario;
    String nombreValidaImagen;
    String apellidoValidaImagen;
    String emailValidaImagen;
    String contactoValidaImagen;
    String horaIgualAImagen;
    String fechaIgualAImagen;
    String igualAImagen;
    String robada;
    String id;
    String patente;
    String color;
    String marca;
    String modelo;
    String duenoNombre;
    String duenoRut;
    String fechaRobo;
    String observacion;
    String duenoDatabase;
    List<String> grupo;
    List<String> grupoCompartido;
    List<String> gruposUsuario;
    List<GroupType> listaGrupoTipo;
    String tipoVehiculo;

    String ano;
    String motor;
    String chasis;
    String tipo;

    String aseguradora;
    String idProse;
    String sinSiniestro;
    @ServerTimestamp
    private Date fechaCreacion ;

    //origen de la denuncia
    String origen;

    //Base de datos
    boolean baseSoap = false;
    boolean baseRobada = false;
    boolean baseListaNegra = false;
    boolean baseClonado = false;

    boolean alertar = true;

    //denuncia
    String denuncianteNombre;
    String denuncianteRut;
    String denuncianteEmail;
    String denuncianteTelefono;
    String dirRobo;
    String comisaria;
    String fechaDenuncia;
    int donacion = 0;

    public PatenteListaNegra() {
    }

    public PatenteListaNegra(String id, String patente, String marca, String modelo, String color, String duenoDatabase, List<String> grupo, String duenoNombre, String duenoRut, String fechaRobo, String observacion, boolean baseListaNegra, boolean baseRobada, boolean baseSoap, boolean baseClonado) {
        this.id = id;
        this.patente = patente;
        this.color = color;
        this.marca = marca;
        this.modelo = modelo;
        this.duenoNombre = duenoNombre;
        this.duenoRut = duenoRut;
        this.fechaRobo = fechaRobo;
        this.observacion = observacion;
        this.grupo = grupo;
        this.baseListaNegra = baseListaNegra;
        this.baseRobada = baseRobada;
        this.baseSoap = baseSoap;
        this.baseClonado = baseClonado;
        this.duenoDatabase = duenoDatabase;
    }

    public String getNombreValidaImagen() {
        return nombreValidaImagen;
    }

    public void setNombreValidaImagen(String nombreValidaImagen) {
        this.nombreValidaImagen = nombreValidaImagen;
    }

    public String getApellidoValidaImagen() {
        return apellidoValidaImagen;
    }

    public void setApellidoValidaImagen(String apellidoValidaImagen) {
        this.apellidoValidaImagen = apellidoValidaImagen;
    }

    public String getEmailValidaImagen() {
        return emailValidaImagen;
    }

    public void setEmailValidaImagen(String emailValidaImagen) {
        this.emailValidaImagen = emailValidaImagen;
    }

    public String getContactoValidaImagen() {
        return contactoValidaImagen;
    }

    public void setContactoValidaImagen(String contactoValidaImagen) {
        this.contactoValidaImagen = contactoValidaImagen;
    }

    public String getHoraIgualAImagen() {
        return horaIgualAImagen;
    }

    public void setHoraIgualAImagen(String horaIgualAImagen) {
        this.horaIgualAImagen = horaIgualAImagen;
    }

    public String getFechaIgualAImagen() {
        return fechaIgualAImagen;
    }

    public void setFechaIgualAImagen(String fechaIgualAImagen) {
        this.fechaIgualAImagen = fechaIgualAImagen;
    }

    public String getIgualAImagen() {
        return igualAImagen;
    }

    public void setIgualAImagen(String igualAImagen) {
        this.igualAImagen = igualAImagen;
    }

    public String getRobada() {
        return robada;
    }

    public void setRobada(String robada) {
        this.robada = robada;
    }

    public String getDenuncianteNombre() {
        return denuncianteNombre;
    }

    public void setDenuncianteNombre(String denuncianteNombre) {
        this.denuncianteNombre = denuncianteNombre;
    }

    public String getDenuncianteRut() {
        return denuncianteRut;
    }

    public void setDenuncianteRut(String denuncianteRut) {
        this.denuncianteRut = denuncianteRut;
    }

    public String getDenuncianteTelefono() {
        return denuncianteTelefono;
    }

    public void setDenuncianteTelefono(String denuncianteTelefono) {
        this.denuncianteTelefono = denuncianteTelefono;
    }

    public String getDirRobo() {
        return dirRobo;
    }

    public void setDirRobo(String dirRobo) {
        this.dirRobo = dirRobo;
    }

    public String getComisaria() {
        return comisaria;
    }

    public void setComisaria(String comisaria) {
        this.comisaria = comisaria;
    }

    public String getFechaDenuncia() {
        return fechaDenuncia;
    }

    public void setFechaDenuncia(String fechaDenuncia) {
        this.fechaDenuncia = fechaDenuncia;
    }

    public int getDonacion() {
        return donacion;
    }

    public void setDonacion(int donacion) {
        this.donacion = donacion;
    }

    public boolean isAlertar() {
        return alertar;
    }

    public void setAlertar(boolean alertar) {
        this.alertar = alertar;
    }

    public boolean isBaseSoap() {
        return baseSoap;
    }

    public void setBaseSoap(boolean baseSoap) {
        this.baseSoap = baseSoap;
    }

    public boolean isBaseRobada() {
        return baseRobada;
    }

    public void setBaseRobada(boolean baseRobada) {
        this.baseRobada = baseRobada;
    }

    public boolean isBaseListaNegra() {
        return baseListaNegra;
    }

    public boolean isBaseClonado() {
        return baseClonado;
    }

    public void setBaseClonado(boolean baseClonado) {
        this.baseClonado = baseClonado;
    }

    public void setBaseListaNegra(boolean baseListaNegra) {
        this.baseListaNegra = baseListaNegra;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDenuncianteEmail() {
        return denuncianteEmail;
    }

    public void setDenuncianteEmail(String denuncianteEmail) {
        this.denuncianteEmail = denuncianteEmail;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getApellidoUsuario() {
        return apellidoUsuario;
    }

    public void setApellidoUsuario(String apellidoUsuario) {
        this.apellidoUsuario = apellidoUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getContactoUsuario() {
        return contactoUsuario;
    }

    public void setContactoUsuario(String contactoUsuario) {
        this.contactoUsuario = contactoUsuario;
    }



    public String getAseguradora() {
        return aseguradora;
    }

    public void setAseguradora(String aseguradora) {
        this.aseguradora = aseguradora;
    }

    public String getIdProse() {
        return idProse;
    }

    public void setIdProse(String idProse) {
        this.idProse = idProse;
    }

    public String getSinSiniestro() {
        return sinSiniestro;
    }

    public void setSinSiniestro(String sinSiniestro) {
        this.sinSiniestro = sinSiniestro;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipoVehiculo) {
        this.tipo = tipoVehiculo;
    }

    public List<String> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<String> grupo) {
        this.grupo = grupo;
    }

    public String getDuenoDatabase() {
        return duenoDatabase;
    }

    public void setDuenoDatabase(String duenoDatabase) {
        this.duenoDatabase = duenoDatabase;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDuenoNombre() {
        return duenoNombre;
    }

    public void setDuenoNombre(String duenoNombre) {
        this.duenoNombre = duenoNombre;
    }

    public String getDuenoRut() {
        return duenoRut;
    }

    public void setDuenoRut(String duenoRut) {
        this.duenoRut = duenoRut;
    }

    public String getFechaRobo() {
        return fechaRobo;
    }

    public void setFechaRobo(String fechaRobo) {
        this.fechaRobo = fechaRobo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public List<String> getGruposUsuario() {
        return gruposUsuario;
    }

    public void setGruposUsuario(List<String> gruposUsuario) {
        this.gruposUsuario = gruposUsuario;
    }

    public List<GroupType> getListaGrupoTipo() {
        return listaGrupoTipo;
    }

    public void setListaGrupoTipo(List<GroupType> listaGrupoTipo) {
        this.listaGrupoTipo = listaGrupoTipo;
    }

    public List<String> getGrupoCompartido() {
        return grupoCompartido;
    }

    public void setGrupoCompartido(List<String> grupoCompartido) {
        this.grupoCompartido = grupoCompartido;
    }
}
