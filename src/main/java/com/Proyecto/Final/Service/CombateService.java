package com.Proyecto.Final.Service;

import java.util.Date;
import java.util.List;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.DTO.CombateRequest;
import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Repository.CombateRepository;
import com.Proyecto.Final.Repository.RobotRepository;

@Service
public class CombateService {
    @Autowired
    private CombateRepository combateRepository;

    @Autowired
    private RobotRepository robotRepository;

    public List<Combate> Lista_Combates_Pendientes(){
        return combateRepository.findByEstadoAndEliminadoFalseOrderByFechacombateAsc("Pendiente", PageRequest.of(0,10));
    }

    public List<Combate> Lista_robot_buscado(String nombre1, String nombre2){
        return combateRepository.findByRobot1NombreContainingOrRobot2NombreContaining(nombre1, nombre2);
    }

    public List<Robot> robots_disponibles(){
        return robotRepository.findByOcupadoFalse();
    }

    public Combate findById(Long id){
        return combateRepository.findById(id).get();
    }

    public String convertirDateTime(CombateRequest combateRequest){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String auxiliar = combateRequest.getFechacombate().format(formatter);
        return auxiliar;
    }
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

    public void ocultar_combate(long id){
        Combate combate = combateRepository.findById(id).get();
        combate.setEliminado(true);
        combateRepository.save(combate);
    }

    public Combate saveCombate(List<Long> robotsSeleccionados, CombateRequest combateRequest){
        Combate combate;

        if(combateRequest.getId()== null){
            combate = new Combate();
            combate.setFecha_creacion(new Date());
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

        combate.setRobot1(robot1);
        combate.setRobot2(robot2);

        combate.setFechacombate(combateRequest.getFechacombate());
        
        combate.setEstado(combateRequest.getId() == null ? "Pendiente" : combateRequest.getEstado());

        return combateRepository.save(combate);
    }
}
