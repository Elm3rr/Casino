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
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Service.*;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private RobotService robotService;

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if(principal !=null){
            String username = principal.getName();
            Usuario usuario = userService.findByUsername(username);
            if(usuario !=null){
                double saldo = usuario.getSaldo();
                model.addAttribute("saldo", saldo);
                model.addAttribute("username", username);
            }else{
                model.addAttribute("saldo", "N/A");
            }
        }else{
            model.addAttribute("saldo", "N/A");
        }
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
        PersonaRequest user = new PersonaRequest();
        model.addAttribute("user",user);
        model.addAttribute("exito", false);
        model.addAttribute("editable", true);
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") PersonaRequest personaRequest, BindingResult result, Model model) {
        
        if (!personaRequest.getPassword().equals(personaRequest.getConfirmPassword())) {
            result.addError(
                new FieldError("user", "confirmPassword", "Las contraseñas no coinciden")
            );
        }
    
        if (userService.username_ocupado(personaRequest.getUsername())) {
            result.addError(
                new FieldError("user", "username", "Nombre de usuario ya tomado, elegí otro")
            );
        }
    
        if (userService.email_ocupado(personaRequest.getEmail())) {
            result.addError(
                new FieldError("user", "email", "Correo ya utilizado")
            );
        }
    
        if (userService.CUI_ocupado(personaRequest.getCui())) {
            result.addError(
                new FieldError("user", "cui", "CUI ya utilizado")
            );
        }
    
        if (result.hasErrors()) {
            model.addAttribute("editable", true);
            return "register";
        }
    
        try {
            userService.saveUser(personaRequest);
            model.addAttribute("user", new PersonaRequest());
            model.addAttribute("exito", true);
            model.addAttribute("editable", true);
        } catch (Exception ex) {
            result.addError(
                new FieldError("user", "nombre", ex.getMessage())
            );
        }
    
        return "register";
    }
    
}
