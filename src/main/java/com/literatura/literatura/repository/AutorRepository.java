package com.literatura.literatura.repository;

import com.literatura.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :anio AND (a.fechaMuerte IS NULL OR a.fechaMuerte > :anio)")
    List<Autor> findVivosEnAnio(@Param("anio") int anio);

    Optional<Autor> findByNombre(String nombre);
}
