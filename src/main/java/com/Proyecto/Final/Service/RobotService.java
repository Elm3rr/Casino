package com.Proyecto.Final.Service;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.DTO.RobotRequest;
import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Repository.PersonaRepository;
import com.Proyecto.Final.Repository.RobotRepository;
import com.Proyecto.Final.Repository.UserRepository;

@Service
public class RobotService {

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonaRepository personaRepository;

    //Logica de Busquedas
    public Robot findRobot(Long id){
        return robotRepository.findById(id).get();
    }

    public List<Robot> robots_ranking(String estado){
        return robotRepository.findByEstadoAndEliminadoDescalificadoFalseOrderByVictoriasDesc(estado, PageRequest.of(0, 10));
    }

    public List<Robot> robots_list_habilitados(String estado){
        return robotRepository.findByEstadoOrderByVictoriasDesc(estado, PageRequest.of(0, 10));
    }

    public List<Robot> robot_busqueda(String estado, String busqueda) {
        return robotRepository.findByEstadoAndNombreContainingOrEstadoAndJugadorContaining(estado, busqueda, estado, busqueda );
    }

    //Ocultar y restaurar robots
    public void ocultar_robot(long id, String motivo, Principal principal){
        Usuario usuario = userRepository.findByUsername(principal.getName())            
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        Robot robot = findRobot(id);
        robot.setEliminadoDescalificado(true);
        robot.setEstado("Eliminado");
        robot.setMotivoeliminacion(motivo);
        robot.setRobotEliminadoPor(usuario);
        robotRepository.save(robot);
    }

    public void restaurar_robot(long id){
        Robot robot = findRobot(id);
        robot.setEliminadoDescalificado(false);
        robot.setEstado("Disponible");
        robot.setMotivoeliminacion(null);
        robot.setRobotEliminadoPor(null);
        robotRepository.save(robot);
    }

    //Logica de Modificación y Guardado
    
    public RobotRequest ActualData(Robot robot){
        RobotRequest robotRequest = new RobotRequest();
        robotRequest.setId(robot.getId());
        robotRequest.setNombre(robot.getNombre());
        robotRequest.setVictorias(robot.getVictorias());
        robotRequest.setAltura(robot.getAltura());
        robotRequest.setPeso(robot.getPeso());
        robotRequest.setEnvergadura(robot.getEnvergadura());
        robotRequest.setJugador(robot.getJugador());
        robotRequest.setArmas(robot.getArmas());
        return robotRequest;
    }

    public Robot saveRobot(RobotRequest robotRequest, String nombre, Principal principal){

        Persona persona = personaRepository.findByUsername(principal.getName())            
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        Robot robot;

        if (robotRequest.getId() == null) {
            robot = new Robot();
            robot.setEstado("Disponible");
            robot.setEliminadoDescalificado(false);
            robot.setVictorias(0);
            robot.setDerrotas(0);
        } else {
            robot = robotRepository.findById(robotRequest.getId())
            .orElseThrow(() -> new IllegalArgumentException("Robot no encontrado"));
        }        
        robot.setNombre(robotRequest.getNombre());
        robot.setVictorias(robotRequest.getVictorias());
        robot.setDerrotas(robotRequest.getDerrotas());
        robot.setAltura(robotRequest.getAltura());
        robot.setPeso(robotRequest.getPeso());
        robot.setEnvergadura(robotRequest.getEnvergadura());
        robot.setJugador(robotRequest.getJugador());
        robot.setArmas(robotRequest.getArmas());
        robot.setImage(nombre);
        robot.setCreadoPor(persona);

        try {
            return robotRepository.save(robot);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el robot: " + e.getMessage());
        }

    }

}
