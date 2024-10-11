package com.Proyecto.Final.Controlladores;

import java.security.Principal;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Proyecto.Final.DTO.PersonaRequest;
import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Service.CombateService;
import com.Proyecto.Final.Service.UserService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private CombateService combateService;

    @Autowired
    private UserService userService;

    @GetMapping("/eliminacion")
    public String eliminar_cuenta(Principal principal, RedirectAttributes redirectAttributes){
        try {
            userService.eliminar_cuenta(principal);
            redirectAttributes.addFlashAttribute("mensaje", "Cuenta eliminada exitosamente.");
            return "redirect:/login"; 
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la cuenta: " + ex.getMessage());
             return "redirect:/home";
        }
    }

    @GetMapping("/combates")
    public String combates(Model model,
    @RequestParam(value="estado", defaultValue="Programado") String estado,
    @RequestParam(value="busqueda", required=false) String busqueda){
        List<Combate> combates;
        if(busqueda !=null && !busqueda.isEmpty()){
            combates = combateService.Combate_buscado(estado, busqueda);
        }else{
            combates = combateService.Lista_Combates_Pendientes(estado);
        }
        model.addAttribute("combates", combates);
        return "combates";
    }

    @GetMapping("/perfil")
    public String show(Model model, Principal principal){
        String username = principal.getName();
        Persona persona = userService.findByUsername(username);
        PersonaRequest user = userService.ActualData(persona);
        model.addAttribute("user", user);
        model.addAttribute("editable", false);
        return "register";
    }
    @GetMapping("/edit")
    public String editarDatos(Model model, Principal principal){
        String username = principal.getName();
        Persona persona = userService.findByUsername(username);
        PersonaRequest user = userService.ActualData(persona);
        model.addAttribute("user", user);
        model.addAttribute("editable", true);
        return "register";
    }

    @PostMapping("/edit")
    public String confirm(@Valid @ModelAttribute("user") PersonaRequest personaRequest, BindingResult result, Model model) {
        
        if (!personaRequest.getPassword().equals(personaRequest.getConfirmPassword())) {
            result.addError(
                new FieldError("user", "confirmPassword", "Las contraseñas no coinciden")
            );
        }
    
        if (userService.email_ocupado(personaRequest.getEmail()) && !userService.DueñoUsername(personaRequest.getUsername(), personaRequest.getId())) {
            result.addError(
                new FieldError("user", "email", "Correo ya utilizado")
            );
        }
    
        if (userService.CUI_ocupado(personaRequest.getCui()) && !userService.DueñoCUI(personaRequest.getCui(), personaRequest.getId())) {
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
        } catch (Exception ex) {
            result.addError(
                new FieldError("user", "nombre", ex.getMessage())
            );
        }
    
        return "redirect:/user/perfil";
    }


    
}
