package edu.servicios.estudiantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.servicios.estudiantes.model.Estudiante;

public interface EstudianteRepositorio extends JpaRepository<Estudiante, Integer> {
}
