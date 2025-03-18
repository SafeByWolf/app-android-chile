package com.safebywolf.safebywolf.Model;

public class ConfiguracionTotem {
    String horaInicio;
    String horaTermino;
    String limiteMaximoDeTemperaturaCPU;
    String limiteMinimoDeTemperaturaCPU;
    String limiteMaximoDeTemperaturaBateria;
    String limiteMinimoDeTemperaturaBateria;

    public ConfiguracionTotem() {
    }

    public ConfiguracionTotem(String horaInicio, String horaTermino, String limiteMaximoDeTemperaturaCPU, String limiteMinimoDeTemperaturaCPU, String limiteMaximoDeTemperaturaBateria, String limiteMinimoDeTemperaturaBateria) {
        this.horaInicio = horaInicio;
        this.horaTermino = horaTermino;
        this.limiteMaximoDeTemperaturaCPU = limiteMaximoDeTemperaturaCPU;
        this.limiteMinimoDeTemperaturaCPU= limiteMinimoDeTemperaturaCPU;
        this.limiteMaximoDeTemperaturaBateria = limiteMaximoDeTemperaturaBateria;
        this.limiteMinimoDeTemperaturaBateria=limiteMinimoDeTemperaturaBateria;
    }

    public String getLimiteMinimoDeTemperaturaCPU() {
        return limiteMinimoDeTemperaturaCPU;
    }

    public void setLimiteMinimoDeTemperaturaCPU(String limiteMinimoDeTemperaturaCPU) {
        this.limiteMinimoDeTemperaturaCPU = limiteMinimoDeTemperaturaCPU;
    }

    public String getLimiteMinimoDeTemperaturaBateria() {
        return limiteMinimoDeTemperaturaBateria;
    }

    public void setLimiteMinimoDeTemperaturaBateria(String limiteMinimoDeTemperaturaBateria) {
        this.limiteMinimoDeTemperaturaBateria = limiteMinimoDeTemperaturaBateria;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraTermino() {
        return horaTermino;
    }

    public void setHoraTermino(String horaTermino) {
        this.horaTermino = horaTermino;
    }

    public String getLimiteMaximoDeTemperaturaCPU() {
        return limiteMaximoDeTemperaturaCPU;
    }

    public void setLimiteMaximoDeTemperaturaCPU(String limiteMaximoDeTemperaturaCPU) {
        this.limiteMaximoDeTemperaturaCPU = limiteMaximoDeTemperaturaCPU;
    }

    public String getLimiteMaximoDeTemperaturaBateria() {
        return limiteMaximoDeTemperaturaBateria;
    }

    public void setLimiteMaximoDeTemperaturaBateria(String limiteMaximoDeTemperaturaBateria) {
        this.limiteMaximoDeTemperaturaBateria = limiteMaximoDeTemperaturaBateria;
    }
}
