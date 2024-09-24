package com.Proyecto.Final.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.DTO.RobotRequest;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Repository.RobotRepository;

@Service
public class RobotService {

    @Autowired
    private RobotRepository robotRepository;

    public Robot saveRobot(RobotRequest robotRequest){
        Robot object = new Robot();
        

        return robotRepository.save(object);
    }


}
