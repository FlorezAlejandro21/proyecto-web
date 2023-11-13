package edu.servicios.estudiantes.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import edu.servicios.estudiantes.repository.MaquinaRepositorio;
import edu.servicios.estudiantes.repository.OrdenRepositorio;
import edu.servicios.estudiantes.repository.PersonaRepositorio;
import edu.servicios.estudiantes.repository.UsuariosRepositorio;
import edu.servicios.estudiantes.model.Maquina;
import edu.servicios.estudiantes.model.Orden;
import edu.servicios.estudiantes.model.Persona;
import edu.servicios.estudiantes.model.Usuarios;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api", produces = "application/json")
public class EstudianteControlador {
    @Autowired
    private UsuariosRepositorio repositorioEstudiante;

    @Autowired
    private PersonaRepositorio repositorioPersona;

    @Autowired
    private MaquinaRepositorio repositorioMaquina;

    @Autowired
    private OrdenRepositorio repositorioOrden;

    @GetMapping("/usuarios")
    public List<Usuarios> traerPersonas() {
        return repositorioEstudiante.findAll();
    }

    @GetMapping("/personas")
    public List<Persona> traerPersona() {
        return repositorioPersona.findAll();
    }

    @GetMapping("/persona/{id}")
    public Optional<Persona> traeUnaPersona(@PathVariable Integer id) {
        return repositorioPersona.findById(id);
    }

    @GetMapping("/login/{email}/{password}")
    public ResponseEntity<String> autenticarUsuario(
            @PathVariable("email") String email,
            @PathVariable("password") String contrasenia) {

        Usuarios usuario = repositorioEstudiante.findByEmail(email);

        if (usuario != null && usuario.getContrasenia().equals(contrasenia)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<Usuarios> creaUsuario(@RequestBody Usuarios persona) {
        Usuarios nuevoUsuario = repositorioEstudiante.save(persona);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PostMapping("/crear-persona")
    public ResponseEntity<Persona> creaEstudiante(@RequestBody Persona persona) {
        Persona nuevaPersona = repositorioPersona.save(persona);
        return new ResponseEntity<>(nuevaPersona, HttpStatus.CREATED);
    }

    @GetMapping("/ordenes/{codigo}")
    public ResponseEntity<List<Orden>> traerOrdenesPorCodigo(@PathVariable String codigo) {
        List<Orden> ordenes = repositorioOrden.findByCodigo(codigo);

        if (!ordenes.isEmpty()) {
            return new ResponseEntity<>(ordenes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/crear-orden")
    public ResponseEntity<Orden> creaOrden(@RequestBody Orden orden) {
        Orden nuevaOrden = repositorioOrden.save(orden);
        return new ResponseEntity<>(nuevaOrden, HttpStatus.CREATED);
    }

    @Value("${upload.path}")
    private String uploadPath;

    @PostMapping("/registrar")
    public String registrarMaquina(
            @RequestParam("nombre") String nombre,
            @RequestParam("codigo") String codigo,
            @RequestParam("marca") String marca,
            @RequestParam("modelo") String modelo,
            @RequestParam("potencia") String potencia,
            @RequestParam("productividad") String productividad,
            @RequestParam("voltaje") String voltaje,
            @RequestParam("peso") String peso,
            @RequestParam("estado") String estado,
            @RequestParam("fecha") String fechaStr,
            @RequestParam("imagen") MultipartFile imagen) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = dateFormat.parse(fechaStr);
            Maquina maquina = new Maquina();
            maquina.setNombre(nombre);
            maquina.setCodigo(codigo);
            maquina.setMarca(marca);
            maquina.setModelo(modelo);
            maquina.setPotencia(potencia);
            maquina.setProductividad(productividad);
            maquina.setVoltaje(voltaje);
            maquina.setPeso(peso);
            maquina.setEstado(estado);
            maquina.setFecha_adquisision(fecha);

            String fileName = StringUtils.cleanPath(imagen.getOriginalFilename());
            String filePath = Paths.get(uploadPath, fileName).toString();

            // Verifica si la carpeta de destino existe, si no, créala
            Files.createDirectories(Paths.get(uploadPath));

            // Guardar la imagen en el sistema de archivos
            Files.write(Paths.get(filePath), imagen.getBytes());
            maquina.setImagen(Files.readAllBytes(Paths.get(filePath)));

            repositorioMaquina.save(maquina);

            return "Registro exitoso";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la máquina: " + e.getMessage();
        }
    }

    @GetMapping("/maquinas")
    public List<Maquina> obtenerTodasLasMaquinas() {
        return repositorioMaquina.findAll();
    }

    @PutMapping("/actualiza/{id}")
    public ResponseEntity<Persona> actualizaEstudiante(@PathVariable Integer id,
            @RequestBody Persona persona) {
        Optional<Persona> personaActual = repositorioPersona.findById(id);
        if (personaActual == null) {
            return new ResponseEntity<Persona>(HttpStatus.NOT_FOUND);
        }
        personaActual.get().setUsuario(persona.getUsuario());
        personaActual.get().setEmail(persona.getEmail());
        personaActual.get().setContraseña(persona.getContraseña());
        personaActual.get().setCargo(persona.getCargo());
        personaActual.get().setCiudad(persona.getCiudad());
        personaActual.get().setDireccion(persona.getDireccion());
        personaActual.get().setTelefono(persona.getTelefono());
        personaActual.get().setFecha_ingreso(persona.getFecha_ingreso());
        repositorioPersona.save(personaActual.get());
        return new ResponseEntity<Persona>(HttpStatus.OK);
    }
}
