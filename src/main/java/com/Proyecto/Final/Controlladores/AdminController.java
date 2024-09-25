package com.Proyecto.Final.Controlladores;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.Proyecto.Final.DTO.RobotRequest;
import com.Proyecto.Final.Service.RobotService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    private RobotService robotService;
    
    @GetMapping("/create_robot")
    public String create(Model model){
        RobotRequest robotRequest = new RobotRequest();
        model.addAttribute("robot", robotRequest);
        return "rrobot";
    }

    @PostMapping("/create_robot")
    public String create_robot(@Valid @ModelAttribute RobotRequest robotRequest, BindingResult result){
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
            String direccion_subida = "public/images/";
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
        return "redirect:/ranking";
    }


}
