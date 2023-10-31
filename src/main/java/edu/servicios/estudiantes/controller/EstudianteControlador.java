package edu.servicios.estudiantes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.servicios.estudiantes.repository.EstudianteRepositorio;
import edu.servicios.estudiantes.repository.CursoRepositorio;
import edu.servicios.estudiantes.model.NotasPorEstudiante;
import edu.servicios.estudiantes.model.Persona;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/")
public class EstudianteControlador {
    @Autowired
    private EstudianteRepositorio repositorioEstudiante;
    @Autowired
    private CursoRepositorio repositorioCurso;

    @GetMapping("/estudiantes")
    public List<Persona> traerEstudiantes() {
        return repositorioEstudiante.findByRol("E");
    }

    @GetMapping("/profesores")
    public List<Persona> traerProfesores() {
        return repositorioEstudiante.findByRol("P");
    }

    @GetMapping("/personas")
    public List<Persona> traerPersonas() {
        return repositorioEstudiante.findAll();
    }

    @GetMapping("/persona/{id}")
    public Optional<Persona> traeUnaPersona(@PathVariable Integer id) {
        return repositorioEstudiante.findById(id);
    }

    @PostMapping("/crear")
    public ResponseEntity<Persona> creaEstudiante(@RequestBody Persona persona) {
        Persona nuevaPersona = repositorioEstudiante.save(persona);
        return new ResponseEntity<>(nuevaPersona, HttpStatus.CREATED);
    }

    @PutMapping("/actualiza/{id}")
    public ResponseEntity<Persona> actualizaEstudiante(@PathVariable Integer id,
            @RequestBody Persona persona) {
        Optional<Persona> personaActual = repositorioEstudiante.findById(id);
        if (personaActual == null) {
            return new ResponseEntity<Persona>(HttpStatus.NOT_FOUND);
        }
        personaActual.get().setNombre(persona.getNombre());
        personaActual.get().setApellido(persona.getApellido());
        personaActual.get().setCorreo(persona.getCorreo());
        personaActual.get().setRol(persona.getRol());
        repositorioEstudiante.save(personaActual.get());
        return new ResponseEntity<Persona>(HttpStatus.OK);
    }

    @DeleteMapping("/borra/{id}")
    public ResponseEntity<HttpStatus> borraEstudiante(@PathVariable Integer id) {
        Optional<Persona> persona = repositorioEstudiante.findById(id);
        if (persona == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repositorioEstudiante.delete(persona.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/estudiante/{estudianteId}/notas")
    public List<NotasPorEstudiante> obtenerNotasPorEstudiante(@PathVariable Integer estudianteId) {
        List<Object[]> resultados = repositorioCurso.findNotasPorEstudiante(estudianteId);
        List<NotasPorEstudiante> notas = new ArrayList<>();
        
        for (Object[] resultado : resultados) {
            NotasPorEstudiante nota = new NotasPorEstudiante();
            nota.setCursoCodigo((String) resultado[0]);
            nota.setMateriaNombre((String) resultado[1]);
            nota.setNotaValor((Double) resultado[2]);
            notas.add(nota);
        }
        
        return notas;
    }

}
