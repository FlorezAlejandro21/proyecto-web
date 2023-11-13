package edu.servicios.estudiantes.repository;

import edu.servicios.estudiantes.model.Maquina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaquinaRepositorio extends JpaRepository<Maquina, Integer> {
    
}
