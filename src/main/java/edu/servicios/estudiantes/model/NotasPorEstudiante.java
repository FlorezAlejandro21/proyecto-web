package edu.servicios.estudiantes.model;

public class NotasPorEstudiante {
    private String cursoCodigo;
    private String materiaNombre;
    private Double notaValor;

    public NotasPorEstudiante(){}

    public NotasPorEstudiante(String cursoCodigo, String materiaNombre, Double notaValor) {
        this.cursoCodigo = cursoCodigo;
        this.materiaNombre = materiaNombre;
        this.notaValor = notaValor;
    }

    public String getCursoCodigo() {
        return cursoCodigo;
    }

    public void setCursoCodigo(String cursoCodigo) {
        this.cursoCodigo = cursoCodigo;
    }

    public String getMateriaNombre() {
        return materiaNombre;
    }

    public void setMateriaNombre(String materiaNombre) {
        this.materiaNombre = materiaNombre;
    }

    public Double getNotaValor() {
        return notaValor;
    }

    public void setNotaValor(Double notaValor) {
        this.notaValor = notaValor;
    }
}
