package com.javeriana.edu.co.controllers;

import java.util.List;
import com.javeriana.edu.co.models.Libro;
import com.javeriana.edu.co.repository.DataBase;

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

    public Boolean devolverLibro(String id) {
        Libro libro = obtenerLibrosByCodigo(id);
        Boolean modifico = false;
        if (libro != null) {
            int unidades = libro.getUnidades() + 1;
            libro.setUnidades(unidades);
            modifico = this.data.modificarLibro(this.arg, libro);
        }
        return modifico;
    }
}
