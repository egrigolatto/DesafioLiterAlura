package com.literatura.literatura.model;

public class Autor {
    private String nombre;
    private String fechaNacimiento;
    private String fechaMuerte;

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaMuerte = datosAutor.fechaMuerte();
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(String fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }



    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return  "-----------------------------\n" +
                "*********** AUTOR ***********\n" +
                "Nombre: " + nombre + '\n' +
                "Año de nacimiento: " + fechaNacimiento + '\n' +
                "Año de fallecimiento: " + fechaMuerte + '\n' +
                "*****************************"
                ;
    }
}
