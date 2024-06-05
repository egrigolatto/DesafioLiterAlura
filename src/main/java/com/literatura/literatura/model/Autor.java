package com.literatura.literatura.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;

    @Column(name = "fecha_muerte")
    private String fechaMuerte;

    @ManyToMany(mappedBy = "autor", fetch = FetchType.LAZY)
    private List<Libro> libros;

    public Autor() {
    }

    public Autor(String nombre, String fechaNacimiento, String fechaMuerte) {
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaMuerte = fechaMuerte;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(String fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return  "-----------------------------\n" +
                "*********** AUTOR ***********\n" +
                "Nombre: " + nombre + '\n' +
                "Año de nacimiento: " + fechaNacimiento + '\n' +
                "Año de fallecimiento: " + fechaMuerte + '\n' +
                "*****************************";
    }
}
