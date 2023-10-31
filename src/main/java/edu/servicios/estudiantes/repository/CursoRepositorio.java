package edu.servicios.estudiantes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.servicios.estudiantes.model.Curso;

public interface CursoRepositorio extends JpaRepository<Curso, Integer> {
    @Query("SELECT c.numero AS cursoCodigo, m.nombre AS materiaNombre, n.valor AS notaValor " +
       "FROM Curso c " +
       "INNER JOIN c.materia m " +
       "INNER JOIN Nota n ON c.materia.id = n.materia.id AND c.profesor.id = n.profesor.id " +
       "WHERE c.estudiante.id = :estudianteId")

    List<Object[]> findNotasPorEstudiante(@Param("estudianteId") Integer estudianteId);
}

