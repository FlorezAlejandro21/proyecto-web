package edu.servicios.estudiantes.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import edu.servicios.estudiantes.model.Usuarios;

public interface UsuariosRepositorio extends JpaRepository<Usuarios, Integer> {
    Usuarios findByEmail(String email);
}
