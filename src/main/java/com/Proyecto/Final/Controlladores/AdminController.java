package com.Proyecto.Final.Controlladores;

import java.security.Principal;

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

import com.Proyecto.Final.DTO.PersonaRequest;
import com.Proyecto.Final.Service.ImagenService;
import com.Proyecto.Final.Service.ModService;
import com.Proyecto.Final.Service.PersonaService;
import com.Proyecto.Final.Service.TransactionService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    ModService modService;
    
    @Autowired
    PersonaService personaService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    ImagenService imagenService;

    @GetMapping("/registerUsers")
    public String register(Model model){
        PersonaRequest user = new PersonaRequest();
        model.addAttribute("user",user);
        model.addAttribute("editable", true);
        model.addAttribute("esAdmin", true);
        model.addAttribute("esEdicion", false);
        return "register";
    }

    @PostMapping("/registerUsers")
    public String confirm(@Valid @ModelAttribute("user") PersonaRequest personaRequest, 
    @RequestParam(value="ROL", defaultValue = "ROLE_USER") String Rol, 
    BindingResult result, Model model, Principal principal){

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
            model.addAttribute("editable", true);
            model.addAttribute("SelectRole", true);
            return "register";
        }

        try{
            personaService.newUser(personaRequest, Rol, principal);
            model.addAttribute("user", new PersonaRequest());
            model.addAttribute("editable", true);
            model.addAttribute("SelectRole", true);
        } catch (Exception ex) {
            result.addError(
                new FieldError("user", "nombre", ex.getMessage())
            );
        }    
        return "register";
    }

    //Logica para vetar usuarios
    @PostMapping("/declineTransaction")
    public String decline(@RequestParam Long id, @RequestParam("motivo") String motivo){
        try {
            transactionService.rechazar_transaccion(id, motivo);
            return "redirect:/usuarios";
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/data/transactions";
    }
    //Logica para restaurar usuarios (solo admins)
    //Mover a admincontroller
    @PostMapping("/approveTransaction")
    public String aprove(@RequestParam Long id, 
    @RequestParam(required = false) MultipartFile imagenBoleta,
    Principal principal){
        try {
            String nombreImagen = null;
            if(imagenBoleta != null && !imagenBoleta.isEmpty()){
                nombreImagen = imagenService.buildImage(imagenBoleta);
            }
            transactionService.aprobar_transaccion(id, nombreImagen, principal);
            return "redirect:/data/transactions";
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
        return "redirect:/data/transactions";
    }


}
