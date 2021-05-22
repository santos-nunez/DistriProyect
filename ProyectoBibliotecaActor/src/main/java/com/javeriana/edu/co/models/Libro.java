package com.javeriana.edu.co.models;

public class Libro {

    private int id;
    private String codigo;
    private int unidades;
    private int unidadesPrestadas;
    private String titulo;
    private String autor;

    public Libro(int id, String codigo, int unidades, int unidadesPrestadas, String titulo, String autor) {
        this.id = id;
        this.codigo = codigo;
        this.autor = autor;
        this.unidadesPrestadas = unidadesPrestadas;
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

    public String getTitulo() {
        return this.titulo;
    }

    public int getUnidades() {
        return this.unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public void setUnidadesPrestadas(int unidades) {
        this.unidadesPrestadas = unidades;
    }

    public int getUnidadesPrestadas() {
        return this.unidadesPrestadas;
    }
}
