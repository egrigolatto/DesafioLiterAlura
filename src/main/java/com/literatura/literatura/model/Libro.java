package com.literatura.literatura.model;

import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String titulo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_idioma", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "idioma")
    private List<String> idioma;

    private Double numeroDeDescargas;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autor;

    public Libro() {
    }

    public Libro(DatosLibros datosLibros, List<Autor> autores) {
        this.titulo = datosLibros.titulo();
        this.idioma = datosLibros.idiomas();
        this.numeroDeDescargas = datosLibros.numeroDescargas();
        this.autor = autores;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getIdioma() {
        return idioma;
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public List<Autor> getAutor() {
        return autor;
    }

    public void setAutor(List<Autor> autor) {
        this.autor = autor;
    }

    public void inicializarAutores() {
        Hibernate.initialize(autor);
    }

    @Override
    public String toString() {

        inicializarAutores();

        String autoresNombres = autor.stream()
                .map(Autor::getNombre)
                .collect(Collectors.joining(", "));

        String idiomasString = String.join(", ", idioma);

        return  "-----------------------------\n" +
                "*********** LIBRO ***********\n" +
                "Titulo: " + titulo + '\n' +
                "Autor(es): " + autoresNombres + '\n' +
                "Idioma(s): " + idiomasString + '\n' +
                "Número de Descargas: " + numeroDeDescargas + '\n' +
                "*****************************";
    }
}
