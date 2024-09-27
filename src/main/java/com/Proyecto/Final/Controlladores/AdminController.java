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

import com.Proyecto.Final.DTO.CombateRequest;
import com.Proyecto.Final.DTO.RobotRequest;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Service.CombateService;
import com.Proyecto.Final.Service.RobotService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    private RobotService robotService;

    @Autowired
    private CombateService combateService;
    
    @GetMapping("/create_robot")
    public String create(Model model){
        RobotRequest robotRequest = new RobotRequest();
        model.addAttribute("robot", robotRequest);
        return "rrobot";
    }
    @PostMapping("/create_robot")
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

    @GetMapping("/edit_robot")
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

    @PostMapping("/edit_robot")
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


    @GetMapping("/delete_robot")
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
    
    @PostMapping("/combates")
    public String crear_combate(@RequestParam("robotsSeleccionados") List<Long> robotsSeleccionados,
                                @Valid @ModelAttribute("combateRequest") CombateRequest combateRequest, 
                                BindingResult result, Model model) {
    
        // Verificar la cantidad de robots seleccionados
        System.out.println("Cantidad de robots seleccionados: " + robotsSeleccionados.size());
    
        // Verificar si no se seleccionaron exactamente 2 robots
        if(robotsSeleccionados.size() != 2){
            System.out.println("Error: No se seleccionaron exactamente dos robots.");
            model.addAttribute("error", "Debes seleccionar exactamente dos robots");
            List<Robot> robots = combateService.robots_disponibles();
            model.addAttribute("robots", robots);
            return "seleccion";
        }
    
        if (result.hasErrors()) {
            System.out.println("Error: Hay errores en los datos del formulario.");
            result.getAllErrors().forEach(error -> {
                System.out.println("Error: " + error.getDefaultMessage());
            });
            
            List<Robot> robots = combateService.robots_disponibles();
            model.addAttribute("robots", robots);
            return "seleccion";
        }
    
        // Verificar que se intenta guardar el combate
        System.out.println("Guardando combate con robots seleccionados: " + robotsSeleccionados);
    
        try {
            combateService.saveCombate(robotsSeleccionados, combateRequest);
            System.out.println("Combate guardado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al guardar el combate: " + e.getMessage());
            model.addAttribute("error", "Ocurrió un error al guardar el combate");
            List<Robot> robots = combateService.robots_disponibles();
            model.addAttribute("robots", robots);
            return "seleccion";
        }
    
        // Redireccionar al listado de combates
        System.out.println("Redireccionando a /user/combates");
        return "redirect:/user/combates";
    }
    
    
    
    @GetMapping("/create_combate")
    public String select_robot(Model model){
        List<Robot> robots = combateService.robots_disponibles();
        model.addAttribute("robots", robots);
        model.addAttribute("combateRequest", new CombateRequest());
        return "seleccion";
    }
}
