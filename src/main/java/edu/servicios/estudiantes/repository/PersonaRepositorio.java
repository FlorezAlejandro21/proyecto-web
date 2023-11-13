package edu.servicios.estudiantes.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import edu.servicios.estudiantes.model.Persona;


public interface PersonaRepositorio extends JpaRepository<Persona, Integer> {
}
