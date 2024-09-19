package com.Proyecto.Final.Service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.DTO.PersonaRequest;
import com.Proyecto.Final.Entity.Apostador;
import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Repository.PersonaRepository;

@Service
public class PersonaService {
    
    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean username_ocupado(String username){
        Optional<Persona> username_ = personaRepository.findByUsername(username);
        return username_.isPresent();
    }

    public boolean email_ocupado(String email){
        Optional<Persona> e_mail = personaRepository.findByEmail(email);
        return e_mail.isPresent();
    }

    public boolean CUI_ocupado(String cui){
        Optional<Persona> cui_ = personaRepository.findByCui(cui);
        return cui_.isPresent();
    }

    public Apostador saveUser(PersonaRequest personaRequest){
        Apostador user =  new Apostador();

        user.setNombre(personaRequest.getNombre());
        user.setApellido(personaRequest.getApellido());
        user.setEmail(personaRequest.getEmail());
        user.setUsername(personaRequest.getUsername());
        user.setCui(personaRequest.getCui());
        user.setActivo(true);
        user.setSaldo(0);
        user.setFecha_creacion(new Date());

        user.setPassword(passwordEncoder.encode(personaRequest.getPassword()));

        user.setRole("ROLE_USER");

        return personaRepository.save(user);
    }
}
