package com.Proyecto.Final.Controlladores;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Proyecto.Final.DTO.RobotRequest;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Service.RobotService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/robot")
public class RobotController {
    
    @Autowired
    private RobotService robotService;
    
    @GetMapping("/crear")
    public String create(Model model){
        RobotRequest robotRequest = new RobotRequest();
        model.addAttribute("robot", robotRequest);
        return "rrobot";
    }
    @PostMapping("/crear")
    public String create_robot(@Valid @ModelAttribute("robot") RobotRequest robotRequest, BindingResult result){
        if(robotRequest.getImage().isEmpty()){
            result.addError(new FieldError("robotRequest", "image", "El archivo de imagen no puede estar vacio"));
        }

        if(result.hasErrors()){
            return "rrobot";
        }
        
        MultipartFile image = robotRequest.getImage();
        Date creado = new Date();
        String Nombre_imagen = creado.getTime() + "-" + image.getOriginalFilename();

        try {
            String direccion_subida = "src/main/resources/static/images/";
            Path uploadPath = Paths.get(direccion_subida);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(direccion_subida, Nombre_imagen), StandardCopyOption.REPLACE_EXISTING);                
            }
        }catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        robotService.saveRobot(robotRequest, Nombre_imagen);
        return "redirect:/public/ranking";
    }

    @GetMapping("/editar")
    public String edit_robot(Model model, @RequestParam long id){
        try{
            Robot robot = robotService.findRobot(id);
            model.addAttribute("robot", robot);
            RobotRequest robotRequest = robotService.ActualData(robot);
            model.addAttribute("request", robotRequest);

        }catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/public/ranking";
        }
        
        return "edit_robot";
    }

    @PostMapping("/editar")
    public String edit_robot(Model model, @RequestParam long id, @Valid @ModelAttribute("request") RobotRequest robotRequest, BindingResult result){
        try{
            Robot robot = robotService.findRobot(id);
            model.addAttribute("robot", robot);

            if(result.hasErrors()){
                return "edit_robot";
            }
            String Nombre_imagen;

            if(!robotRequest.getImage().isEmpty()){
                String direccion_subida = "src/main/resources/static/images/";
                Path oldPath = Paths.get(direccion_subida + robot.getImage());
                try{
                    Files.delete(oldPath);
                }catch(Exception ex){
                    System.out.println("Exception: " + ex.getMessage());
                }
                        
                MultipartFile image = robotRequest.getImage();
                Date creado = new Date();
                Nombre_imagen = creado.getTime() + "-" + image.getOriginalFilename();
                try(InputStream inputStream = image.getInputStream()){
                    Files.copy(inputStream, Paths.get(direccion_subida, Nombre_imagen), StandardCopyOption.REPLACE_EXISTING);                
                }
            }else{
                Nombre_imagen = robot.getImage();
            }

            robotService.saveRobot(robotRequest, Nombre_imagen);


        }catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/public/ranking";
    }


    @GetMapping("/eliminar")
    public String deleteProduct(@RequestParam long id){
        try{
            robotService.ocultar_robot(id);
            return "redirect:/public/ranking";

        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage()); 
        }
        return "redirect:/public/ranking";
    }

    @GetMapping("/buscar")
    public String buscar_robot(@RequestParam("query") String query, Model model){
        List<Robot> robots = robotService.searchByname(query);
        model.addAttribute("robots", robots);
        return "ranking"; 
    }
    
}
