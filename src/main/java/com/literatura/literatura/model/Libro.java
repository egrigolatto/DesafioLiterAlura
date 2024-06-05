package com.literatura.literatura.model;

import java.util.stream.Collectors;

public class Libro {
    private String titulo;
    private String autor;
    private String idioma;
    private Double numeroDeDescargas;

    public Libro(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        if (datosLibros.autor() != null && !datosLibros.autor().isEmpty()) {
            this.autor = datosLibros.autor().stream()
                    .map(DatosAutor::nombre)
                    .collect(Collectors.joining(", "));
        } else {
            this.autor = "Desconocido";
        }
        if (datosLibros.idiomas() != null && !datosLibros.idiomas().isEmpty()) {
            this.idioma = String.join(", ", datosLibros.idiomas());
        } else {
            this.idioma = "Desconocido";
        }
        this.numeroDeDescargas = datosLibros.numeroDescargas();
    }


    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return  "-----------------------------\n" +
                "*********** LIBRO ***********\n" +
                "Titulo: " + titulo + '\n' +
                "Autor: " + autor + '\n' +
                "Idioma: " + idioma + '\n' +
                "Número de Descargas: " + numeroDeDescargas + '\n' +
                "*****************************"
                ;
    }
}
