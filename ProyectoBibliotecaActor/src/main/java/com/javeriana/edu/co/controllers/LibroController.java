package com.javeriana.edu.co.controllers;

import java.util.List;
import com.javeriana.edu.co.models.Libro;
import com.javeriana.edu.co.models.Prestamo;
import com.javeriana.edu.co.repository.DataBase;
import java.util.Date;

public class LibroController {

    private DataBase data;
    private String arg;

    public LibroController() {
        this.data = new DataBase();
    }

    public LibroController(String arg) {
        this.data = new DataBase();
        this.arg = arg;
    }

    public List<Libro> obtenerLibros() {
        List<Libro> libros = this.data.leerFicheroLibro(this.arg);
        return libros;
    }

    public Libro obtenerLibroById(int id) {
        List<Libro> libros = this.data.leerFicheroLibro(this.arg);
        Libro encontro = null;
        for (Libro libro : libros) {
            if (libro.getID() == id) {
                encontro = libro;
            }
        }
        return encontro;
    }

    public Libro obtenerLibrosByCodigo(String id) {
        List<Libro> libros = this.data.leerFicheroLibro(this.arg);
        Libro encontro = null;
        for (Libro libro : libros) {
            if (libro.getCodigo().equals(id)) {
                encontro = libro;
            }
        }
        return encontro;
    }

    public Boolean devolverLibro(Libro lib) {
        Libro libro = lib;
        Boolean modifico = false;
        if (libro != null) {
            int unidades = libro.getUnidades() + 1;
            libro.setUnidades(unidades);
            modifico = this.data.modificarLibro(this.arg, libro);
        }
        return modifico;
    }

    public String solicitarLibro(String codigoLibro,
            int idSolicitante,
            Date fechaSolicitud,
            Date fechaDevolucion) {
        Libro libro = obtenerLibrosByCodigo(codigoLibro);
        String infoLibro = "";
        int total = 0, prestados = 0, disponibles = 0;
        String valido = "No existe";
        if (libro != null) {
            if (libro.getUnidades() > 0) {
                valido = "Satisfactoria";
            } else {
                valido = "Libro en Prestamo";
            }
        }
        if (!valido.equalsIgnoreCase("No existe")) {
            prestados = libro.getUnidadesPrestadas();
            disponibles = libro.getUnidades();
            total = prestados + disponibles;
            if (valido.equalsIgnoreCase("Satisfactoria")) {
                PrestamoController prestamosController = new PrestamoController("prestamos.txt");
                List<Prestamo> prestamosLista = prestamosController.obtenerPrestamos();
                Prestamo prestamo;
                if (prestamosLista == null || prestamosLista.size() == 0) {
                    prestamo = new Prestamo(0, null, null, 1, "", false);
                } else {
                    prestamo = prestamosLista.get(prestamosLista.size() - 1);
                }

                Prestamo nuevo = new Prestamo(prestamo.getIdSolicitud() + 1, fechaSolicitud, fechaDevolucion, idSolicitante, codigoLibro, false);
                if (prestarLibro(libro)) {
                    prestamosController.crearPrestamo(nuevo);
                    libro = obtenerLibrosByCodigo(codigoLibro);
                    prestados = libro.getUnidadesPrestadas();
                    disponibles = libro.getUnidades();
                    total = prestados + disponibles;
                    infoLibro = " " + libro.getTitulo() + " " + libro.getAutor();
                } else {
                    valido = "Libro en Prestamo";
                }

            }
        }
        valido = valido + " " + "total: " + total + " " + "prestados: " + prestados + " " + "disponibles: " + disponibles + infoLibro;

        return valido;
    }

    public Boolean prestarLibro(Libro libro) {
        Boolean modifico = false;
        if (libro != null) {
            int unidades = libro.getUnidades() - 1;
            int unidadesPrestadas = libro.getUnidadesPrestadas() + 1;
            if (unidades >= 0) {
                libro.setUnidades(unidades);
                libro.setUnidadesPrestadas(unidadesPrestadas);
                modifico = this.data.modificarLibro(this.arg, libro);
            }
        }
        return modifico;
    }
}
