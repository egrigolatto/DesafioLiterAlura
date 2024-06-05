package com.literatura.literatura.principal;

import com.literatura.literatura.model.Autor;
import com.literatura.literatura.model.Datos;
import com.literatura.literatura.model.DatosLibros;
import com.literatura.literatura.model.Libro;
import com.literatura.literatura.service.ConsumoApi;
import com.literatura.literatura.service.ConvierteDatos;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Comparator;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private static ConsumoApi consumoApi = new ConsumoApi();
    private static ConvierteDatos conversor = new ConvierteDatos();
    private static Scanner teclado = new Scanner(System.in);

    public void mostrarMenu(){
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ------------------------------
                    
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Top 10 libros mas descargados
                    
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
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    top10LibrosMasDescargados();
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
       }

    private void top10LibrosMasDescargados() {
        // Obtener todos los datos de la API
        var json = consumoApi.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);

        List<Libro> top10Libros = datos.resultados().stream()
                .sorted(Comparator.comparingDouble(DatosLibros::numeroDescargas).reversed())
                .limit(10)
                .map(Libro::new)
                .collect(Collectors.toList());

        // Mostrar los 10 libros más descargados
        System.out.println("Top 10 libros más descargados:");
        top10Libros.forEach(System.out::println);
    }

    private void listarLibrosPorIdioma() {
        // Obtener todos los datos de la API
        var json = consumoApi.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);

        // Obtenemos todos los idiomas disponibles
        Set<String> idiomas = datos.resultados().stream()
                .flatMap(datosLibros -> datosLibros.idiomas().stream())
                .collect(Collectors.toSet());

        // Mostrar el menú de idiomas
        System.out.println("Ingrese el idioma para buscar los libros:");
        idiomas.forEach(idioma -> System.out.println(idioma + " - " + traducirIdioma(idioma)));

        // Leer la opción del usuario
        String opcionIdioma = teclado.nextLine();

        // Verificar si el idioma seleccionado es válido
        if (!idiomas.contains(opcionIdioma)) {
            System.out.println("Idioma no válido. Por favor, seleccione una opción válida.");
            return;
        }

        // Filtrar y mostrar los libros en el idioma seleccionado
        List<Libro> librosEnIdioma = datos.resultados().stream()
                .filter(libro -> libro.idiomas().contains(opcionIdioma))
                .map(Libro::new)
                .collect(Collectors.toList());

        // Nostrar los libros encontrados
        if (librosEnIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma seleccionado.");
        } else {
            System.out.println("Libros en " + traducirIdioma(opcionIdioma) + ":");
            librosEnIdioma.forEach(System.out::println);
        }
    }

    // Método para traducir el código de idioma a su nombre
    private String traducirIdioma(String codigoIdioma) {
        switch (codigoIdioma) {
            case "es":
                return "español";
            case "en":
                return "inglés";
            case "fr":
                return "francés";
            case "pt":
                return "portugués";
            default:
                return "desconocido";
        }
    }

    private static void listarAutoresVivosEnUnDeterminadoAnio() {
        // Pedir al usuario que ingrese un año
        System.out.println("Ingrese el año para verificar los autores vivos:");
        int anio = teclado.nextInt();
        teclado.nextLine();

        // Obtener todos los datos de la API
        var json = consumoApi.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);

        // Filtrar y listar autores vivos en el año especificado
        List<Autor> autoresVivos = datos.resultados().stream()
                .flatMap(datosLibros -> datosLibros.autor().stream())
                .map(datosAutor -> new Autor(datosAutor))
                .filter(autor -> {
                    int nacimiento = Integer.parseInt(autor.getFechaNacimiento());
                    int muerte = autor.getFechaMuerte() == null ? Integer.MAX_VALUE : Integer.parseInt(autor.getFechaMuerte());
                    return nacimiento <= anio && anio <= muerte;
                })
                .collect(Collectors.toList());

        // Imprimir autores vivos en el año especificado
        if (autoresVivos.isEmpty()) {
            System.out.println("No hay autores vivos en el año " + anio + ".");
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            autoresVivos.forEach(System.out::println);
        }
    }

    private void listarAutoresRegistrados() {
        // Obtener todos los datos de la API
        var json = consumoApi.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);

        // Extraer y listar autores únicos
        List<Autor> autores = datos.resultados().stream()
                .flatMap(datosLibros -> datosLibros.autor().stream())
                .map(datosAutor -> new Autor(datosAutor))
                .distinct()
                .collect(Collectors.toList());

        // Imprimir todos los autores
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("Autores registrados:");
            autores.forEach(System.out::println);
        }
    }


    private static void listarLibrosRegistrados() {
        // Obtener todos los datos de la API
        var json = consumoApi.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);

        // Convertir los resultados en objetos Libro
        List<Libro> libros = datos.resultados().stream()
                .map(datosLibros -> new Libro(datosLibros))
                .collect(Collectors.toList());

        // Imprimir todos los libros
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("Libros registrados:");
            libros.forEach(System.out::println);
        }
    }

    public static void buscarLibroPorTitulo() {
            // Obtener todos los datos de la API
            var json = consumoApi.obtenerDatos(URL_BASE);
            System.out.println(json);
            var datos = conversor.obtenerDatos(json, Datos.class);
            System.out.println(datos);

            // Búsqueda de libros por título
            System.out.println("Ingrese el nombre del libro que desea buscar: ");
            var tituloLibro = teclado.nextLine();
            json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
            var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
            Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                    .filter(t -> t.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                    .findFirst();
            if (libroBuscado.isPresent()) {
                System.out.println("Libro encontrado");
                Libro libro = new Libro(libroBuscado.get());
                System.out.println(libro);
            } else {
                System.out.println("Libro no encontrado");
            }
        }


}

