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

// SELECT c.numero AS cursoCodigo, m.nombre AS materiaNombre, n.valor AS notaValor
// FROM public.curso c
// INNER JOIN materia m ON c.materia_id = m.id
// INNER JOIN nota n ON c.materia_id = n.materia_id
//                 AND c.profesor_id = n.profesor_id
// 				WHERE n.estudiante_id=1