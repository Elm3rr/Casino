package com.Proyecto.Final.Service;

import java.util.Date;
import java.util.List;

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
        return combateRepository.findByEstadoOrderByFechacombateAsc("pendiente", PageRequest.of(0,10));
    }

    public List<Robot> robots_disponibles(){
        return robotRepository.findByOcupadoFalse();
    }

    public Combate saveCombate(List<Long> robotsSeleccionados, CombateRequest combateRequest){
        Combate combate;

        if(combateRequest.getId()== null){
            combate = new Combate();
            combate.setFecha_creacion(new Date());
        }else{
            combate = combateRepository.findById(combateRequest.getId()).orElse(new Combate());
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
        
        combate.setEstado("pendiente");

        return combateRepository.save(combate);
    }

}
