package com.Proyecto.Final.Controlladores;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.Proyecto.Final.Entity.Apuesta;
import com.Proyecto.Final.Entity.Combate;
import com.Proyecto.Final.Service.ApuestaService;
import com.Proyecto.Final.Service.CombateService;
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
    CombateService combateService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    ImagenService imagenService;

    @Autowired
    ApuestaService apuestaService;

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
    public String confirm(@Valid @ModelAttribute("user") PersonaRequest personaRequest, BindingResult result,
    @RequestParam(value="rol", defaultValue = "ROLE_USER") String Rol, Model model, Principal principal){

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
            model.addAttribute("esAdmin", true);
            model.addAttribute("esEdicion", false);
            return "register";
        }

        try{
            personaService.newUser(personaRequest, Rol, principal);
            return "redirect:/data/usuarios";
        } catch (Exception ex) {
                result.addError(new FieldError("user", "nombre", ex.getMessage()));
                model.addAttribute("user", personaRequest);
                model.addAttribute("editable", true);
                model.addAttribute("esAdmin", true);
                model.addAttribute("esEdicion", false);
                return "register";
        }    
    }


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

        //Lista de combates para que los usuarios apuesten
    @GetMapping("/combates")
    public String combates(Model model,
    @RequestParam(value="estado", defaultValue="Programado") String estado,
    @RequestParam(value="busqueda", required=false) String busqueda,
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin){
        
        LocalDateTime inicio = (fechaInicio != null) ? fechaInicio.atStartOfDay() : null; 
        LocalDateTime fin = (fechaFin != null) ? fechaFin.atTime(23, 59, 59) : null; 

        List<Combate> combates = combateService.getCombatesForAdmin(estado, busqueda, inicio, fin);
        System.out.println("Combates: " + combates);
        model.addAttribute("combates", combates);
        model.addAttribute("estado", estado);
        return "combates";
    }



    @GetMapping("/apuestas")
    public String mostrarApuestasAdmin(
            @RequestParam(name = "estado", required = false, defaultValue = "Pendiente") String estado,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin,
            Model model) {
        
        // Obtener apuestas basadas en los filtros proporcionados
        List<Apuesta> apuestas = apuestaService.getApuestasAdmin(estado, username, fechaInicio, fechaFin);
        
        // Agregar las apuestas al modelo para mostrarlas en la vista
        model.addAttribute("apuestas", apuestas);
        
        return "apuestas"; // Nombre de la vista Thymeleaf (apuestas.html)
    }
}
