package com.gitlab.alelizzt.universidad.universidadbackend.controlador;

import com.gitlab.alelizzt.universidad.universidadbackend.exception.BadRequestException;
import com.gitlab.alelizzt.universidad.universidadbackend.modelo.entidades.Carrera;
import com.gitlab.alelizzt.universidad.universidadbackend.modelo.entidades.Profesor;
import com.gitlab.alelizzt.universidad.universidadbackend.modelo.entidades.Persona;
import com.gitlab.alelizzt.universidad.universidadbackend.servicios.contratos.CarreraDAO;
import com.gitlab.alelizzt.universidad.universidadbackend.servicios.contratos.PersonaDAO;
import com.gitlab.alelizzt.universidad.universidadbackend.servicios.contratos.ProfesorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/profesores")
public class ProfesorController extends PersonaController {

    private final CarreraDAO carreraDAO;
    private final ProfesorDAO profesorDAO;

    @Autowired
    public ProfesorController(@Qualifier("profesorDAOImpl") PersonaDAO service, CarreraDAO carreraDAO, ProfesorDAO profesorDAO) {
        super(service);
        this.carreraDAO = carreraDAO;
        this.profesorDAO = profesorDAO;
        nombreEntidad = "Profesor";
    }

    /**
    @GetMapping
    public List<Persona> obtenerTodos(){
        Stream<Persona> personas = ((List<Persona>) service.findAll()).stream();
        List<Persona> profesores = personas.filter(persona -> persona instanceof Profesor).collect(Collectors.toList());
        if(profesores.isEmpty()){
            throw new BadRequestException("No se ha encontrado algun profesor");
        }
        return profesores;
    }**/

    @GetMapping("/carrera")
    public List<Persona> buscarProfesoresPorCarrera(@RequestParam String carrera){
        List<Persona> profesoresByCarrera = (List<Persona>) profesorDAO.findProfesoresByCarrera(carrera);
        if(profesoresByCarrera.isEmpty()){
            throw new BadRequestException("No se ha encontrado algun profesor");
        }
        return profesoresByCarrera;
    }

    @PutMapping("{idProfesor}/carrera/{idCarreras}")
    public Persona asignarCarrerasProfesor(@PathVariable Integer idProfesor, @PathVariable Set<Carrera> idCarreras){
        Optional<Persona> oProfesor = service.findById(idProfesor);
        if(!oProfesor.isPresent()){
            throw new BadRequestException(String.format("El/La profesor/a con id %d no existe", idProfesor));
        }
        Profesor profesor= (Profesor) oProfesor.get();
        profesor.setCarrera(idCarreras);

        return service.save(profesor);
    }

}
