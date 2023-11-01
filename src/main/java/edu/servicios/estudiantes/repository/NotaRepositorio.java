package edu.servicios.estudiantes.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import edu.servicios.estudiantes.model.Nota;

public interface NotaRepositorio extends JpaRepository<Nota, Integer> {
}
