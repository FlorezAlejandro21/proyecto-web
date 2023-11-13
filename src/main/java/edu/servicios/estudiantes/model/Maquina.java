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
public class Maquina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String codigo;
    private String marca;
    private String modelo;
    private String potencia;
    private String productividad;
    private String voltaje;
    private String peso;
    private String estado;
    private Date fecha_adquisision;
    private byte[] imagen;
}
