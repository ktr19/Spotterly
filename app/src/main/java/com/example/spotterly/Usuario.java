package com.example.spotterly;

public class Usuario {
    private int telefono;
    private String password;
    private boolean tieneSuscripcion;
    private int suscripcionId;
    private int usos;
    private String nombre;

    // Constructor
    public Usuario(int telefono, String password, boolean tieneSuscripcion, int suscripcionId, int usos) {
        this.telefono = telefono;
        this.password = password;
        this.tieneSuscripcion = tieneSuscripcion;
        this.suscripcionId = suscripcionId;
        this.usos = usos;
    }

    public Usuario() {

    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {this.nombre = nombre;}

    // Getters y setters
    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTieneSuscripcion() {
        return tieneSuscripcion;
    }

    public void setTieneSuscripcion(boolean tieneSuscripcion) {
        this.tieneSuscripcion = tieneSuscripcion;
    }

    public int getSuscripcionId() {
        return suscripcionId;
    }

    public void setSuscripcionId(int suscripcionId) {
        this.suscripcionId = suscripcionId;
    }

    public int getUsos() {
        return usos;
    }

    public void setUsos(int usos) {
        this.usos = usos;
    }

    // Método toString para facilitar la depuración
    @Override
    public String toString() {
        return "Usuario{" +
                "telefono=" + telefono +
                ", password='" + password + '\'' +
                ", tieneSuscripcion=" + tieneSuscripcion +
                ", suscripcionId=" + suscripcionId +
                ", usos=" + usos +
                '}';
    }
}