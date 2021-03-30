package com.javeriana.edu.co.models;

public class Libro {

    private int id;
    private String codigo;
    private int unidades;
    private String titulo;
    private String autor;

    public Libro(int id, String codigo, int unidades, String titulo, String autor) {
        this.id = id;
        this.codigo = codigo;
        this.autor = autor;
        this.unidades = unidades;
        this.titulo = titulo;
    }

    public Libro() {

    }

    public int getID() {
        return this.id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public String getAutor() {
        return this.autor;
    }

    public int getUnidades() {
        return this.unidades;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

}