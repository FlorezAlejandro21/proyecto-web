package edu.servicios.estudiantes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.servicios.estudiantes.model.Persona;

public interface EstudianteRepositorio extends JpaRepository<Persona, Integer> {
    List<Persona> findByRol(String rol);
}
