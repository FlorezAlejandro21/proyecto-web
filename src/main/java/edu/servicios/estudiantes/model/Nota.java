package edu.servicios.estudiantes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "nota")
public class Nota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "materia_id")
    private Materia materia;
    
    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Persona profesor;
    
    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private Persona estudiante;
    
    @Column(name = "observacion")
    private String observacion;
    
    @Column(name = "valor")
    private Double valor;
    
    // @Column(name = "porcentaje")
    // private Double porcentaje;

}
