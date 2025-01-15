package com.example.spotterly;

public class Ubicaciones {

    private int ubicacionId;
    private float latitud;
    private float longitud;
    private int valoracion;
    private int usuario;

    public Ubicaciones(int ubicacionId, float latitud, float longitud, int valoracion, int usuario) {
        this.setUbicacionId(ubicacionId);
        this.setLatitud(latitud);
        this.setLongitud(longitud);
        this.setValoracion(valoracion);
        this.setUsuario(usuario);
    }

    public int getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(int ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
}
