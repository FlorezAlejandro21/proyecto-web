package edu.servicios.estudiantes.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String codigo;
    private String nombre;
    private String mantenimiento;
    private String tipo;    
    private String descripcion;
    private Date fecha_hora_inicio;
    private Date fecha_hora_fin;
    private String requerimientos;
    private Date fecha_autor;
    private byte[] costos;
}
