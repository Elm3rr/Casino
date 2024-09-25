package com.Proyecto.Final.Controlladores;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.Proyecto.Final.DTO.PersonaRequest;
import com.Proyecto.Final.Entity.Robot;
import com.Proyecto.Final.Service.*;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private RobotService robotService;


    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/register")
    public String register(Model model){
        PersonaRequest user = new PersonaRequest();
        model.addAttribute("user",user);
        model.addAttribute("exito", false);
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") PersonaRequest personaRequest, BindingResult result, Model model) {
        
        if (!personaRequest.getPassword().equals(personaRequest.getConfirmPassword())) {
            result.addError(
                new FieldError("user", "confirmPassword", "Las contraseñas no coinciden")
            );
        }
    
        if (personaService.username_ocupado(personaRequest.getUsername())) {
            result.addError(
                new FieldError("user", "username", "Nombre de usuario ya tomado, elegí otro")
            );
        }
    
        if (personaService.email_ocupado(personaRequest.getEmail())) {
            result.addError(
                new FieldError("user", "email", "Correo ya utilizado")
            );
        }
    
        if (personaService.CUI_ocupado(personaRequest.getCui())) {
            result.addError(
                new FieldError("user", "cui", "CUI ya utilizado")
            );
        }
    
        if (result.hasErrors()) {
            return "register";
        }
    
        try {
            personaService.saveUser(personaRequest);
            model.addAttribute("user", new PersonaRequest());
            model.addAttribute("exito", true);
        } catch (Exception ex) {
            result.addError(
                new FieldError("user", "nombre", ex.getMessage())
            );
        }
    
        return "register";
    }
    


    @GetMapping({"","/"})
    public String home() {
        return "home";
    }
    

    @GetMapping("/ranking")
    public String ranking(Model model){
        List<Robot> robots = robotService.robots_list();
        model.addAttribute("robots", robots);
        return "ranking";
    }


    
}
