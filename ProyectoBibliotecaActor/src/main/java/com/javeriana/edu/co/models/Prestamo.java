package com.javeriana.edu.co.models;

import java.util.Date;

public class Prestamo {
    private int idSolicitud;
    private Date fechaSolicitud;
    private Date fechaDevolucion;
    private int idSolicitante;
    private String codigoLibro;
    private boolean finalizado;

    public Prestamo(int idSolicitud, Date fechaSolicitud, Date fechaDevolucion, int idSolicitante, String codigolibro,
            boolean finalizado) {
        this.idSolicitud = idSolicitud;
        this.idSolicitante = idSolicitante;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaDevolucion = fechaDevolucion;
        this.codigoLibro = codigolibro;
        this.finalizado = finalizado;
    }

    public Prestamo() {

    }

    public void setFechaDevolucion(Date fecha) {
        this.fechaDevolucion = fecha;
    }

    public void setFechaSolicitud(Date fecha) {
        this.fechaSolicitud = fecha;
    }

    public void setFinalizado(Boolean estado) {
        this.finalizado = estado;
    }

    public int getIdSolicitud() {
        return this.idSolicitud;
    }

    public int getIdSolicitante() {
        return this.idSolicitante;
    }

    public String getCodigoLibro() {
        return this.codigoLibro;
    }

    public Boolean getFinalizado() {
        return this.finalizado;
    }

    public Date getFechaSolicitud() {
        return this.fechaSolicitud;
    }

    public Date getFechaFinalizacion() {
        return this.fechaDevolucion;
    }

}
