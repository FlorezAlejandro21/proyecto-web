package edu.servicios.estudiantes.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.util.StringUtils;

import edu.servicios.estudiantes.repository.MaquinaRepositorio;
import edu.servicios.estudiantes.repository.OrdenRepositorio;
import edu.servicios.estudiantes.repository.PersonaRepositorio;
import edu.servicios.estudiantes.model.Maquina;
import edu.servicios.estudiantes.model.Orden;
import edu.servicios.estudiantes.model.Persona;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api", produces = "application/json")
public class EstudianteControlador {

    @Autowired
    private PersonaRepositorio repositorioPersona;

    @Autowired
    private MaquinaRepositorio repositorioMaquina;

    @Autowired
    private OrdenRepositorio repositorioOrden;

    @GetMapping("/personas")
    public List<Persona> traerPersona() {
        return repositorioPersona.findAll();
    }

    @GetMapping("/persona/{id}")
    public Optional<Persona> traeUnaPersona(@PathVariable Integer id) {
        return repositorioPersona.findById(id);
    }

    @GetMapping("/maquina/{id}")
    public Optional<Maquina> traeUnaMaquina(@PathVariable Integer id) {
        return repositorioMaquina.findById(id);
    }

    @PostMapping("/crear-persona")
    public ResponseEntity<Persona> creaEstudiante(@RequestBody Persona persona) {
        Persona nuevaPersona = repositorioPersona.save(persona);
        return new ResponseEntity<>(nuevaPersona, HttpStatus.CREATED);
    }

    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<HttpStatus> borraEstudiante(@PathVariable Integer id) {
        Optional<Persona> persona = repositorioPersona.findById(id);
        if (persona == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repositorioPersona.delete(persona.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/borrar-maquina/{id}")
    public ResponseEntity<HttpStatus> borraMaquina(@PathVariable Integer id) {
        Optional<Maquina> maquina = repositorioMaquina.findById(id);
        if (maquina == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repositorioMaquina.delete(maquina.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam("manual") MultipartFile manual) {

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

            // Manejar la imagen
            if (imagen != null && !imagen.isEmpty()) {
                String imagenFileName = StringUtils.cleanPath(imagen.getOriginalFilename());
                String imagenFilePath = Paths.get(uploadPath, imagenFileName).toString();

                Files.createDirectories(Paths.get(uploadPath));
                Files.write(Paths.get(imagenFilePath), imagen.getBytes());
                maquina.setImagen(Files.readAllBytes(Paths.get(imagenFilePath)));
            }

            // Manejar el archivo manual (PDF)
            if (manual != null && !manual.isEmpty()) {
                String manualFileName = StringUtils.cleanPath(manual.getOriginalFilename());
                String manualFilePath = Paths.get(uploadPath, manualFileName).toString();

                Files.createDirectories(Paths.get(uploadPath));
                Files.write(Paths.get(manualFilePath), manual.getBytes());
                maquina.setManual(Files.readAllBytes(Paths.get(manualFilePath)));
            }

            repositorioMaquina.save(maquina);

            return "Registro exitoso";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la máquina: " + e.getMessage();
        }
    }

    @PostMapping("/crear-orden")
    public String registrarOrden(
            @RequestParam("codigo") String codigo,
            @RequestParam("nombre") String nombre,
            @RequestParam("mantenimiento") String mantenimiento,
            @RequestParam("tipo") String tipo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("fecha_hora_inicio") String fechaIniStr,
            @RequestParam("fecha_hora_fin") String fechaFinStr,
            @RequestParam("requerimientos") String requerimientos,
            @RequestParam("fechaAutorizacion") String fechaStr,
            @RequestParam("costos") MultipartFile costos) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            dateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date fecha = dateFormat.parse(fechaStr);
            Date fechaI = dateFormat2.parse(fechaIniStr);
            Date fechaF = dateFormat2.parse(fechaFinStr);

            Orden orden = new Orden();
            orden.setNombre(nombre);
            orden.setCodigo(codigo);
            orden.setMantenimiento(mantenimiento);
            orden.setTipo(tipo);
            orden.setDescripcion(descripcion);
            orden.setFecha_hora_inicio(fechaI);
            orden.setFecha_hora_fin(fechaF);
            orden.setRequerimientos(requerimientos);
            orden.setFecha_autor(fecha);

            // Manejar el archivo manual (PDF)
            if (costos != null && !costos.isEmpty()) {
                String manualFileName = StringUtils.cleanPath(costos.getOriginalFilename());
                String manualFilePath = Paths.get(uploadPath, manualFileName).toString();

                Files.createDirectories(Paths.get(uploadPath));
                Files.write(Paths.get(manualFilePath), costos.getBytes());
                orden.setCostos(Files.readAllBytes(Paths.get(manualFilePath)));
            }

            repositorioOrden.save(orden);

            return "Registro exitoso";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la máquina: " + e.getMessage();
        }
    }

    @PutMapping("/actualizar-maquina/{id}")
    public ResponseEntity<String> actualizarMaquina(
            @PathVariable Integer id,
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
            @RequestParam("mantenerImagen") boolean mantenerImagen,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            @RequestParam("manual") MultipartFile manual) {

        try {
            Optional<Maquina> optionalMaquina = repositorioMaquina.findById(id);
            if (optionalMaquina.isEmpty()) {
                return new ResponseEntity<>("Maquina no encontrada", HttpStatus.NOT_FOUND);
            }

            Maquina maquina = optionalMaquina.get();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = dateFormat.parse(fechaStr);

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

            // Manejar la imagen solo si se proporciona una nueva y no se elige mantener la
            // existente
            if (!mantenerImagen && imagen != null && !imagen.isEmpty()) {
                String imagenFileName = StringUtils.cleanPath(imagen.getOriginalFilename());
                String imagenFilePath = Paths.get(uploadPath, imagenFileName).toString();

                Files.createDirectories(Paths.get(uploadPath));
                Files.write(Paths.get(imagenFilePath), imagen.getBytes());
                maquina.setImagen(Files.readAllBytes(Paths.get(imagenFilePath)));
            }

            // Manejar el archivo manual (PDF) solo si se proporciona uno nuevo
            if (manual != null && !manual.isEmpty()) {
                String manualFileName = StringUtils.cleanPath(manual.getOriginalFilename());
                String manualFilePath = Paths.get(uploadPath, manualFileName).toString();

                Files.createDirectories(Paths.get(uploadPath));
                Files.write(Paths.get(manualFilePath), manual.getBytes());
                maquina.setManual(Files.readAllBytes(Paths.get(manualFilePath)));
            }

            repositorioMaquina.save(maquina);

            return new ResponseEntity<>("Actualización exitosa", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error al actualizar la máquina: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
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
