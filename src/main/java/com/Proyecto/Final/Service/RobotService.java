package com.Proyecto.Final.Service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.DTO.RobotRequest;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Repository.RobotRepository;

@Service
public class RobotService {

    @Autowired
    private RobotRepository robotRepository;

    public Robot findRobot(Long id){
        return robotRepository.findById(id).get();
    }
    public List<Robot> robots_list(){
        return robotRepository.findAll();
    }

    public RobotRequest ActualData(Robot robot){
        RobotRequest robotRequest = new RobotRequest();
        robotRequest.setId(robot.getId());
        robotRequest.setNombre(robot.getNombre());
        robotRequest.setVictorias(robot.getVictorias());
        robotRequest.setAltura(robot.getAltura());
        robotRequest.setPeso(robot.getPeso());
        robotRequest.setEnvergadura(robot.getEnvergadura());
        robotRequest.setDescalificado(robot.isDescalificado());
        robotRequest.setJugador(robot.getJugador());
        robotRequest.setArmas(robot.getArmas());
        return robotRequest;
    }

    public Robot saveRobot(RobotRequest robotRequest, String nombre){
        Robot robot = robotRepository.findById(robotRequest.getId()).orElse(new Robot()); // Asegúrate de que RobotRequest tenga el ID
        
        robot.setNombre(robotRequest.getNombre());
        robot.setVictorias(0);
        robot.setDerrotas(0);
        robot.setAltura(robotRequest.getAltura());
        robot.setPeso(robotRequest.getPeso());
        robot.setEnvergadura(robotRequest.getEnvergadura());
        robot.setN_armas(0);
        robot.setDescalificado(false);
        robot.setJugador(robotRequest.getJugador());
        robot.setFecha_creacion(new Date());
        robot.setArmas(robotRequest.getArmas());
        robot.setImage(nombre);

        return robotRepository.save(robot);
    }


}
