package com.Proyecto.Final.Controlladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.Proyecto.Final.DTO.PersonaRequest;
import com.Proyecto.Final.Entity.Apostador;
import com.Proyecto.Final.Service.PersonaService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private PersonaService personaService;

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @PostMapping("/register")
    public ResponseEntity<Apostador> saveUser(@RequestBody @Valid PersonaRequest personaRequest){
        return new ResponseEntity<>(personaService.saveUser(personaRequest), HttpStatus.CREATED);
    }

    @GetMapping("/home")
    public String home() {
        return "home.html";
    }
    



    
}
