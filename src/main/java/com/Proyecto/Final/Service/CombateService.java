package com.Proyecto.Final.Service;

import java.util.List;
import java.time.format.DateTimeFormatter;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.DTO.CombateRequest;
import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Repository.CombateRepository;
import com.Proyecto.Final.Repository.RobotRepository;
import com.Proyecto.Final.Repository.UserRepository;

@Service
public class CombateService {
    @Autowired
    private CombateRepository combateRepository;

    @Autowired
    private RobotRepository robotRepository;

    @Autowired
    private UserRepository userRepository;

    //Logica de busquedas
    public List<Combate> Lista_Combates_Pendientes(String estado){
        return combateRepository.findByEstadoAndEliminadoVetadoFalseOrderByFechacombateAsc(estado, PageRequest.of(0,10));
    }

    public List<Combate> Combate_buscado(String estado, String busqueda){
        return combateRepository.findByRobot1NombreContainingAndEstadoContainingOrRobot2NombreContainingAndEstadoContaining(busqueda, estado, busqueda, estado);
    }

    public List<Robot> robots_disponibles(String estado){
        return robotRepository.findByEstado(estado);
    }

    public Combate findById(Long id){
        return combateRepository.findById(id).get();
    }

    public String convertirDateTime(CombateRequest combateRequest){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String auxiliar = combateRequest.getFechacombate().format(formatter);
        return auxiliar;
    }

    public void ocultar_combate(long id, Principal principal){
        Usuario usuario = userRepository.findByUsername(principal.getName())            
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Combate combate = combateRepository.findById(id).get();
        combate.setEstado("Eliminado");
        combate.setEliminadoVetado(true);
        combate.setCombateEliminadoPor(usuario);
        Robot robot1 = combate.getRobot1();
        Robot robot2 = combate.getRobot2();
        if (robot1 != null) {
            robot1.setEstado("Disponible");
            robotRepository.save(robot1);
        }
        if (robot2 != null) {
            robot2.setEstado("Disponible");
            robotRepository.save(robot2);
        }
        combateRepository.save(combate);
    }

    //Actualización y registro de nuevos combates
    public CombateRequest ActualData(Combate combate){
        CombateRequest combateRequest = new CombateRequest();
        combateRequest.setId(combate.getId());
        combateRequest.setEstado(combate.getEstado());
        combateRequest.setFechacombate(combate.getFechacombate());
        combateRequest.setAuxiliar_fecha(convertirDateTime(combateRequest));
        combateRequest.setRobot1_id(combate.getRobot1().getId());
        combateRequest.setRobot2_id(combate.getRobot2().getId());
        return combateRequest;
    }

    public Combate saveCombate(List<Long> robotsSeleccionados, CombateRequest combateRequest, Principal principal){

        Usuario usuario = userRepository.findByUsername(principal.getName())            
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Combate combate;

        if(combateRequest.getId()== null){
            combate = new Combate();
            combateRequest.setAuxiliar_fecha(null);
        }else{
            combate = combateRepository.findById(combateRequest.getId())
            .orElseThrow(() -> new IllegalArgumentException("Combate no encontrado para ID: " + combateRequest.getId()));
        }

        combateRequest.setRobot1_id(robotsSeleccionados.get(0));
        combateRequest.setRobot2_id(robotsSeleccionados.get(1));

        Robot robot1 = robotRepository.findById(combateRequest.getRobot1_id())
        .orElseThrow(() -> new IllegalArgumentException("Robot 1 no encontrado"));

        Robot robot2 = robotRepository.findById(combateRequest.getRobot2_id())
        .orElseThrow(() -> new IllegalArgumentException("Robot 2 no encontrado"));

        combate.setOrganizadoPor(usuario);
        
        combate.setRobot1(robot1);
        combate.setRobot2(robot2);

        combate.setFechacombate(combateRequest.getFechacombate());
        
        combate.setEstado(combateRequest.getId() == null ? "Pendiente" : combateRequest.getEstado());

        try {
            return combateRepository.save(combate);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el combate: " + e.getMessage());
        }
    }
}
