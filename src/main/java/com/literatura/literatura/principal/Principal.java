package com.literatura.literatura.principal;

import com.literatura.literatura.model.Autor;
import com.literatura.literatura.model.Datos;
import com.literatura.literatura.model.DatosLibros;
import com.literatura.literatura.model.Libro;
import com.literatura.literatura.repository.AutorRepository;
import com.literatura.literatura.repository.LibroRepository;
import com.literatura.literatura.service.ConsumoApi;
import com.literatura.literatura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private static ConsumoApi consumoApi = new ConsumoApi();
    private static ConvierteDatos conversor = new ConvierteDatos();
    private static Scanner teclado = new Scanner(System.in);
    // private static List<Libro> libros = new ArrayList<>();
    private static LibroRepository repositorioLibro;
    private static AutorRepository repositorioAutor;

    public Principal(LibroRepository LibroRepository, AutorRepository autorRepository) {
        repositorioLibro = LibroRepository;
        repositorioAutor = autorRepository;
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ------------------------------
                    1 - Buscar libro y cargar a la DB
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Top 10 libros mas descargados
                    6 - Buscar libro por titulo
              
                    0 - Salir
                    
                    ------------------------------
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnUnDeterminadoAnio();
                    break;
                case 5:
                    top10LibrosMasDescargados();
                case 6:
                    buscarLibroEnDB();
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private void buscarLibroEnDB() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el título del libro que desea buscar:");
        String parteDelTitulo = scanner.nextLine().toLowerCase();

        List<Libro> librosEncontrados = repositorioLibro.findByTituloContainingIgnoreCase(parteDelTitulo);

        if (!librosEncontrados.isEmpty()) {
            System.out.println("Libros encontrados:");
            librosEncontrados.forEach(System.out::println);
        } else {
            System.out.println("No se encontraron libros con ese título.");
        }
    }

    public void top10LibrosMasDescargados() {

        List<Libro> top10Libros = repositorioLibro.findTop10ByNumeroDeDescargasDesc();

        if (top10Libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Top 10 libros más descargados:");
            top10Libros.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosEnUnDeterminadoAnio() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el año para ver los autores vivos: ");
        int anio = scanner.nextInt();
        scanner.nextLine();

        List<Autor> autoresVivos = repositorioAutor.findVivosEnAnio(anio);

        if (autoresVivos.isEmpty()) {
            System.out.println("No hay autores vivos en el año " + anio);
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            autoresVivos.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {

        List<Autor> autores = repositorioAutor.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Libros registrados:");
            autores.forEach(System.out::println);
        }
    }

    public static void buscarLibroPorTitulo() {

        var json = consumoApi.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        var tituloLibro = teclado.nextLine();
        json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(t -> t.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()) {
            System.out.println("Libro encontrado");
            DatosLibros datosLibros = libroBuscado.get();

            List<Autor> autores = datosLibros.autor().stream()
                    .map(datosAutor -> {
                        Optional<Autor> autorExistente = repositorioAutor.findByNombre(datosAutor.nombre());
                        return autorExistente.orElseGet(() -> {
                            Autor nuevoAutor = new Autor(datosAutor.nombre(), datosAutor.fechaNacimiento(), datosAutor.fechaMuerte());
                            return repositorioAutor.save(nuevoAutor);
                        });
                    })
                    .collect(Collectors.toList());

            Libro libro = new Libro(datosLibros, autores);
            repositorioLibro.save(libro);

            System.out.println(libro);
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    private void listarLibrosRegistrados() {

        List<Libro> libros = repositorioLibro.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Libros registrados:");
            libros.forEach(System.out::println);
        }
    }

}

