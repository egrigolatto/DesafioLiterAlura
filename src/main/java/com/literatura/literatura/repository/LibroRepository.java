package com.literatura.literatura.repository;

import com.literatura.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query(value = "SELECT l FROM Libro l ORDER BY l.numeroDeDescargas DESC LIMIT 10")
    List<Libro> findTop10ByNumeroDeDescargasDesc();

    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) LIKE %:parteDelTitulo%")
    List<Libro> findByTituloContainingIgnoreCase(String parteDelTitulo);
}
