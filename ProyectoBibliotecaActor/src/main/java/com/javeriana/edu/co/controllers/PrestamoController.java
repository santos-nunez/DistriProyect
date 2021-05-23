package com.javeriana.edu.co.controllers;

import java.util.Date;
import java.util.List;

import com.javeriana.edu.co.models.Libro;
import com.javeriana.edu.co.models.Prestamo;
import com.javeriana.edu.co.repository.DataBase;

public class PrestamoController {

    private String arg;
    private DataBase data;

    public PrestamoController(String arg) {
        this.data = new DataBase();
        this.arg = arg;
    }

    public PrestamoController() {
        this.data = new DataBase();
    }

    public List<Prestamo> obtenerPrestamos() {
        List<Prestamo> prestamos = this.data.leerFicheroPrestamo(this.arg);
        return prestamos;
    }

    public Prestamo obtenerPrestamoById(int id) {
        List<Prestamo> prestamos = this.data.leerFicheroPrestamo(this.arg);
        Prestamo encontro = null;
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getIdSolicitud() == id) {
                encontro = prestamo;
            }
        }
        return encontro;
    }

    public boolean renovarPrestamo(int idSolicitud, Date fechaSolicitud, Date fechaDevolucion) {
        Prestamo prestamo = obtenerPrestamoById(idSolicitud);
        Boolean modificado = false;
        if (prestamo != null) {
            if (prestamo.getFechaSolicitud().before(fechaSolicitud)
                    && prestamo.getFechaFinalizacion().before(fechaDevolucion)) {
                prestamo.setFechaDevolucion(fechaDevolucion);
                prestamo.setFechaSolicitud(fechaSolicitud);
                prestamo.setFinalizado(false);
                modificado = this.data.modificarPrestamo(this.arg, prestamo);
            }
        }
        return modificado;
    }

    public boolean devolverPrestamo(int idSolicitud) {
        Prestamo prestamo = obtenerPrestamoById(idSolicitud);
        if (prestamo == null) {
            return false;
        }
        String codigo = prestamo.getCodigoLibro();
        Boolean modificado = false;
        LibroController libro = new LibroController("libros.txt");
        Libro lib = libro.obtenerLibrosByCodigo(codigo);
        if (lib != null) {
            if (prestamo != null && !prestamo.getFinalizado()) {
                prestamo.setFinalizado(true);
                modificado = this.data.modificarPrestamo(this.arg, prestamo);
                libro.devolverLibro(lib);
            }
        }

        return modificado;
    }

    public boolean crearPrestamo(Prestamo nuevo) {
        return this.data.agregrar(this.arg, nuevo);
    }
}
