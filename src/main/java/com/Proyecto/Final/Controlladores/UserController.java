package com.Proyecto.Final.Controlladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Proyecto.Final.DTO.RobotRequest;

@Controller
@RequestMapping("/user")
public class UserController {
    @GetMapping("/create_robot")
    public String create(Model model){
        RobotRequest robotRequest = new RobotRequest();
        model.addAttribute("robot", robotRequest);
        return "rrobot";
    }
}
