package com.Proyecto.Final.Controlladores;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Repository.PersonaRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private PersonaRepository personaRepository;

    @ModelAttribute
    public void addGlobalAttributes(Model model, Principal principal){
        if(principal!=null){
            String username = principal.getName();
            Persona persona = personaRepository.findByUsername(username).orElse(null);
            if(persona!=null){
                model.addAttribute("username", username);
                if("ROLE_USER".equals(persona.getRole())){
                    Usuario usuario = (Usuario) persona;
                    double saldo = usuario.getSaldo();
                    model.addAttribute("Saldo", saldo);
                }else{
                    model.addAttribute("Saldo", "N/A");
                }
            }           
        }
    }
}
