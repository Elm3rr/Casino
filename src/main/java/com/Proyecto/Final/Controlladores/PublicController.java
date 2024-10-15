package com.Proyecto.Final.Controlladores;

import java.security.Principal;
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
@RequestMapping("")
public class PublicController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private RobotService robotService;

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        return "home";
    }
    

    @GetMapping("/robots")
    public String ranking(Model model, @RequestParam(value="estado", defaultValue = "Disponible") String estado){
        List<Robot> robots = robotService.robots_ranking(estado);
        model.addAttribute("robots", robots);
        model.addAttribute("estado", estado);
        return "robots";
    }

    @GetMapping("/terms-QA")
    public String information(){
        return "information";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user",new PersonaRequest());
        model.addAttribute("exito", false);
        model.addAttribute("esEdicion", false);
        model.addAttribute("esAdmin", false);
        model.addAttribute("editable", true);

        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") PersonaRequest personaRequest, BindingResult result, 
    @RequestParam(value="ROL", defaultValue = "ROLE_USER") String Rol, Model model) {
        
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
            model.addAttribute("user", personaRequest);
            model.addAttribute("esEdicion", false);
            model.addAttribute("esAdmin", false);
            model.addAttribute("editable", true);
            return "register";
        }
    
        try {
            personaService.newUser(personaRequest, Rol, null);
            model.addAttribute("user", new PersonaRequest());
            model.addAttribute("exito", true);
        } catch (Exception ex) {
            result.addError(new FieldError("user", "nombre", ex.getMessage()));
            model.addAttribute("exito", false);
        }
        
        model.addAttribute("esEdicion", false);
        model.addAttribute("esAdmin", false);
        model.addAttribute("editable", true);

        return "register";
    }
    
}
