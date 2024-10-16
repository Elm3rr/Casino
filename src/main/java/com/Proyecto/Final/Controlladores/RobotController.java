package com.Proyecto.Final.Controlladores;

import java.io.IOException;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.Proyecto.Final.DTO.RobotRequest;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Service.ImagenService;
import com.Proyecto.Final.Service.RobotService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/robot")
public class RobotController {

    @Autowired
    RobotService robotService;

    @Autowired
    ImagenService imagenService;

    @PostMapping("/eliminar") 
    public String eliminate(@RequestParam Long id, @RequestParam("motivo") String motivo, Principal principal){
        try {
            robotService.ocultar_robot(id, motivo, principal);
            return "redirect:/robots"; 
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/robots"; 
    }

    @GetMapping("/restaurar") 
    public String restaurar(@RequestParam Long id){
        try {
            robotService.restaurar_robot(id);
            return "redirect:/robots"; 
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/robots"; 
    }

    @GetMapping("/buscar")
    public String buscar_robot(Model model, 
    @RequestParam(value="estado", defaultValue = "Disponible") String estado,
    @RequestParam(value="busqueda", required=false) String busqueda){
        List<Robot> robots;
        if(busqueda != null && !busqueda.isEmpty()){
            robots = robotService.robot_busqueda(estado, busqueda);
        }else{
            robots = robotService.robots_list_habilitados(estado);
        }
        model.addAttribute("estado", estado);
        model.addAttribute("robots", robots);
        
        return "robots"; 
    }
    
    @GetMapping("/guardar")
    public String create(@RequestParam(required = false) Long id, Model model){
        RobotRequest robotRequest;
        if(id!=null){
            try{
                Robot robot = robotService.findRobot(id);
                model.addAttribute("robot", robot);
                robotRequest = robotService.ActualData(robot);
            }catch(Exception ex){
                System.out.println("Exception: " + ex.getMessage());
                return "redirect: /robots";
            }
        }else{
            robotRequest = new RobotRequest();
            model.addAttribute("robot", new Robot());
        }
        model.addAttribute("request", robotRequest);
        return "r_robot";
    }

    @PostMapping("/guardar")
    public String save(@RequestParam(required = false) Long id,
    @Valid  @ModelAttribute("request") RobotRequest robotRequest,
    BindingResult result, Model model, Principal principal){
        if(result.hasErrors()){
            model.addAttribute("robot", (id!=null) ? robotService.findRobot(id): new Robot());
            return "r_robot";
        }try{
            String nombreImagen = null;

            if(id!=null){
                Robot robot = robotService.findRobot(id);

                if(!robotRequest.getImage().isEmpty() && robot.getImage()!=null){
                    imagenService.deleteImage(robot.getImage());
                }
            }

            if(!robotRequest.getImage().isEmpty()){
                nombreImagen = imagenService.buildImage(robotRequest.getImage());
            }

            robotService.saveRobot(robotRequest, nombreImagen, principal);

        }catch(IOException ex){
            System.out.println("Error: " + ex.getMessage());
            return "r_robot";
        }

        return "redirect:/robots";
    }
    
}
