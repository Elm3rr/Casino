package com.Proyecto.Final.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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


}
