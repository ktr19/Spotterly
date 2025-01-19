package com.example.spotterly;

public class SuscripcionUsuario {
    private int idSuscripcionUsuario;
    private String telefono;
    private int suscripcionId;
    private String fechaInicio;
    private String fechaFin;

    // Getters y setters
    public int getIdSuscripcionUsuario() {
        return idSuscripcionUsuario;
    }

    public void setIdSuscripcionUsuario(int idSuscripcionUsuario) {
        this.idSuscripcionUsuario = idSuscripcionUsuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getSuscripcionId() {
        return suscripcionId;
    }

    public void setSuscripcionId(int suscripcionId) {
        this.suscripcionId = suscripcionId;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}
