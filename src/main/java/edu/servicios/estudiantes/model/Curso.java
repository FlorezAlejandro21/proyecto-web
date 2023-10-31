package edu.servicios.estudiantes.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "curso")
@IdClass(CursoId.class)
public class Curso {
    
        @Id
        @ManyToOne
        @JoinColumn(name = "materia_id")
        private Materia materia;
    
        @Id
        @ManyToOne
        @JoinColumn(name = "profesor_id")
        private Persona profesor;
    
        @ManyToOne
        @JoinColumn(name = "estudiante_id")
        private Persona estudiante;
    
        @Id
        private String numero;
    
        private Date fecha_inicio;
        private Date fecha_fin;
}
