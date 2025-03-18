package com.safebywolf.safebywolf.Model;

import com.google.firebase.firestore.ServerTimestamp;
import com.safebywolf.safebywolf.Class.Utils.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Patente {
    //vehiculo
    String id;
    String patente;
    String marca = null;
    String modelo = null;
    String color = null;
    String chasis = null;
    String motor = null;
    String tipoVehiculo = null;
    String ano = null;

    //observacion del vhiculo
    HashMap<String,String> observacion = new HashMap<>();
    HashMap<String, List<String>> gruposCompartidos = new HashMap<>();

    //campos AACH
    String aseguradora = null;
    String idProse = null;
    String sinSiniestro = null;

    //campos de escaneo
    String latitud = null;
    String longitud = null;
    String fecha = null;
    String hora = null;
    String pais = null;
    String region = null;
    String ciudad = null;
    String comuna = null;
    String direccion = null;

    //url Imagen
    String titleImagenAmpliada = null;

    //datos de usuario escaneador
    String idUsuario = null;
    String nombreUsuario = null;
    String apellidoUsuario = null;
    String emailUsuario = null;
    String contactoUsuario = null;

    //datos de validacion de una patente
    String igualAImagen = null;
    String robada = null;
    Boolean sinPatente = false;
    String verificada="false";
    Boolean visible=true;

    //campos para saber de que base de datos proviene la patente
    List<String> duenoDatabase = new ArrayList<>();
    String tipo = null; //robada o lista negra
    Boolean tipoAmbas=false; //si patente esta en coleccion de lista negra y robada al mismo tiempo

    //grupos a los quue pertenece la patente
    List<String> grupo = new ArrayList<>();
    List<GroupType> listaGrupoTipo = new ArrayList<>();//grupos de usuario, de patente y de comuna
    List<AlertGroup> alertToGroups = new ArrayList<>();//grupos de usuario, de patente y de comuna


    //corresponde a un maximo de 15 fotos por lectura
    String idRafaga = null;
    boolean headRafaga = false;

    //un set rafaga son un conjunto de rafagas por usuario de una patente en especifico en un tiempo de 30 minutos
    String idSetRafaga = null;
    boolean headSetRafaga = false;

    //marca de tiempo de llegada al servidor

    @ServerTimestamp
    private Date timestamp;
    String longTime = null;

    //origen desde donde se subio el dato
    List<String> origen = new ArrayList<>();

    //denunciante
    List<String> denunciantes = new ArrayList<>();

    //totem
    Boolean totem;

    //Base de datos
    boolean baseSoap = false;
    boolean baseRobada = false;
    boolean baseListaNegra = false;
    boolean baseClonado = false;
    boolean baseCompartida = false;

    boolean alertar = true;


    private Date fechaCreacion ;

    boolean alertGroup = false;

    //denuncia
    String denuncianteNombre;
    String denuncianteRut;
    String denuncianteEmail;
    String denuncianteTelefono;
    String dirRobo;
    String comisaria;
    String fechaDenuncia;
    String fechaRobo;
    int donacion;

    int confianzaOCR = 0;

    public boolean isBaseClonado() {
        return baseClonado;
    }

    public void setBaseClonado(boolean baseClonado) {
        this.baseClonado = baseClonado;
    }

    public int getConfianzaOCR() {
        return confianzaOCR;
    }

    public void setConfianzaOCR(Integer confianzaOCR) {
        this.confianzaOCR = null != confianzaOCR ? confianzaOCR : 0;
    }

    public String getDenuncianteNombre() {
        return denuncianteNombre;
    }


    public List<AlertGroup> getAlertToGroups() {
        return alertToGroups;
    }

    public void setAlertToGroups(List<AlertGroup> alertToGroups) {
        this.alertToGroups = alertToGroups;
    }

    public void setDenuncianteNombre(String denuncianteNombre) {
        this.denuncianteNombre = denuncianteNombre;
    }

    public String getFechaRobo() {
        return fechaRobo;
    }

    public void setFechaRobo(String fechaRobo) {
        this.fechaRobo = fechaRobo;
    }

    public String getDenuncianteRut() {
        return denuncianteRut;
    }

    public void setDenuncianteRut(String denuncianteRut) {
        this.denuncianteRut = denuncianteRut;
    }

    public String getDenuncianteEmail() {
        return denuncianteEmail;
    }

    public void setDenuncianteEmail(String denuncianteEmail) {
        this.denuncianteEmail = denuncianteEmail;
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

    public boolean isAlertGroup() {
        return alertGroup;
    }

    public void setAlertGroup(Boolean alertGroup) {
        this.alertGroup = null != alertGroup ? alertGroup : false;
    }

    public Patente() {
    }

    public boolean isAlertar() {
        return alertar;
    }

    public void setAlertar(Boolean alertar) {
        this.alertar = null != alertar ? alertar : false;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public HashMap<String, String> getObservacion() {
        return observacion;
    }

    public void setObservacion(HashMap<String, String> observacion) {
        this.observacion = observacion;
    }

    public HashMap<String, List<String>> getGruposCompartidos() {
        return gruposCompartidos;
    }

    public void setGruposCompartidos(HashMap<String, List<String>> gruposCompartidos) {
        this.gruposCompartidos = gruposCompartidos;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTitleImagenAmpliada() {
        return titleImagenAmpliada;
    }

    public void setTitleImagenAmpliada(String titleImagenAmpliada) {
        this.titleImagenAmpliada = titleImagenAmpliada;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    public String getContactoUsuario() {
        return contactoUsuario;
    }

    public void setContactoUsuario(String contactoUsuario) {
        this.contactoUsuario = contactoUsuario;
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

    public Boolean getSinPatente() {
        return sinPatente;
    }

    public void setSinPatente(Boolean sinPatente) {
        this.sinPatente = sinPatente;
    }

    public String getVerificada() {
        return verificada;
    }

    public void setVerificada(String verificada) {
        this.verificada = verificada;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = null != visible ? visible : false;
    }

    public List<String> getDuenoDatabase() {
        return duenoDatabase;
    }

    public void setDuenoDatabase(List<String> duenoDatabase) {
        this.duenoDatabase = duenoDatabase;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getTipoAmbas() {
        return tipoAmbas;
    }

    public void setTipoAmbas(Boolean tipoAmbas) {
        this.tipoAmbas = tipoAmbas;

    }

    public List<String> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<String> grupo) {
        this.grupo = grupo;
    }

    public List<GroupType> getListaGrupoTipo() {
        return listaGrupoTipo;
    }

    public void setListaGrupoTipo(List<GroupType> listaGrupoTipo) {
        this.listaGrupoTipo = listaGrupoTipo;
    }

    public String getIdRafaga() {
        return idRafaga;
    }

    public void setIdRafaga(String idRafaga) {
        this.idRafaga = idRafaga;
    }

    public boolean isHeadRafaga() {
        return headRafaga;
    }

    public void setHeadRafaga(Boolean headRafaga) {
        this.headRafaga = null != headRafaga ? headRafaga : false;
    }

    public String getIdSetRafaga() {
        return idSetRafaga;
    }

    public void setIdSetRafaga(String idSetRafaga) {
        this.idSetRafaga = idSetRafaga;
    }

    public boolean isHeadSetRafaga() {
        return headSetRafaga;
    }

    public void setHeadSetRafaga(Boolean headSetRafaga) {
        this.headSetRafaga = null != headSetRafaga ? headSetRafaga : false;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getLongTime() {
        return longTime;
    }

    public void setLongTime(String longTime) {
        this.longTime = longTime;
    }

    public List<String> getOrigen() {
        return origen;
    }

    public void setOrigen(List<String> origen) {
        this.origen = origen;
    }

    public List<String> getDenunciantes() {
        return denunciantes;
    }

    public void setDenunciantes(List<String> denunciantes) {
        this.denunciantes = denunciantes;
    }

    public Boolean getTotem() {
        return totem;
    }

    public void setTotem(Boolean totem) {
        this.totem = null != totem ? totem : false;
    }

    public boolean isBaseSoap() {
        return baseSoap;
    }

    public void setBaseSoap(Boolean baseSoap) {
        this.baseSoap = null != baseSoap ? baseSoap : false;
    }

    public boolean isBaseRobada() {
        return baseRobada;
    }

    public void setBaseRobada(Boolean baseRobada) {
        this.baseRobada = null != baseRobada ? baseRobada : false;
    }

    public boolean isBaseListaNegra() {
        return baseListaNegra;
    }

    public void setBaseListaNegra(Boolean baseListaNegra) {
        this.baseListaNegra = null != baseListaNegra ? baseListaNegra : false;
    }

    public boolean isBaseCompartida() {
        return baseCompartida;
    }

    public void setBaseCompartida(Boolean baseCompartida) {
        this.baseCompartida = null != baseCompartida ? baseCompartida : false;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Patente{" +
                "id='" + id + '\'' +
                ", patente='" + patente + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", color='" + color + '\'' +
                ", chasis='" + chasis + '\'' +
                ", motor='" + motor + '\'' +
                ", tipoVehiculo='" + tipoVehiculo + '\'' +
                ", ano='" + ano + '\'' +
                ", observacion=" + observacion +
                ", gruposCompartidos=" + gruposCompartidos +
                ", aseguradora='" + aseguradora + '\'' +
                ", idProse='" + idProse + '\'' +
                ", sinSiniestro='" + sinSiniestro + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", pais='" + pais + '\'' +
                ", region='" + region + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", comuna='" + comuna + '\'' +
                ", direccion='" + direccion + '\'' +
                ", titleImagenAmpliada='" + titleImagenAmpliada + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", apellidoUsuario='" + apellidoUsuario + '\'' +
                ", emailUsuario='" + emailUsuario + '\'' +
                ", contactoUsuario='" + contactoUsuario + '\'' +
                ", igualAImagen='" + igualAImagen + '\'' +
                ", robada='" + robada + '\'' +
                ", isSinPatente=" + sinPatente +
                ", verificada='" + verificada + '\'' +
                ", visible=" + visible +
                ", duenoDatabase=" + duenoDatabase +
                ", tipo='" + tipo + '\'' +
                ", tipoAmbas=" + tipoAmbas +
                ", grupo=" + grupo +
                ", listaGrupoTipo=" + listaGrupoTipo +
                ", alertToGroups=" + alertToGroups +
                ", idRafaga='" + idRafaga + '\'' +
                ", headRafaga=" + headRafaga +
                ", idSetRafaga='" + idSetRafaga + '\'' +
                ", headSetRafaga=" + headSetRafaga +
                ", timestamp=" + timestamp +
                ", longTime='" + longTime + '\'' +
                ", origen=" + origen +
                ", denunciantes=" + denunciantes +
                ", totem=" + totem +
                ", baseSoap=" + baseSoap +
                ", baseRobada=" + baseRobada +
                ", baseListaNegra=" + baseListaNegra +
                ", baseClonado=" + baseClonado +
                ", baseCompartida=" + baseCompartida +
                ", alertar=" + alertar +
                ", fechaCreacion=" + fechaCreacion +
                ", isAlertGroup=" + alertGroup +
                ", denuncianteNombre='" + denuncianteNombre + '\'' +
                ", denuncianteRut='" + denuncianteRut + '\'' +
                ", denuncianteEmail='" + denuncianteEmail + '\'' +
                ", denuncianteTelefono='" + denuncianteTelefono + '\'' +
                ", dirRobo='" + dirRobo + '\'' +
                ", comisaria='" + comisaria + '\'' +
                ", fechaDenuncia='" + fechaDenuncia + '\'' +
                ", fechaRobo='" + fechaRobo + '\'' +
                ", donacion=" + donacion +
                ", confianzaOCR=" + confianzaOCR +
                '}';
    }

}
