package edu.servicios.estudiantes.repository;

import edu.servicios.estudiantes.model.Orden;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepositorio extends JpaRepository<Orden, Integer> {
    List<Orden> findByCodigo(String codigo);
}
