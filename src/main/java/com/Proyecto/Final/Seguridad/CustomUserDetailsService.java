package com.Proyecto.Final.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Repository.PersonaRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonaRepository personaRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Persona persona = personaRepository.findByUsername(username)
            .orElseThrow(()-> new UsernameNotFoundException("Usuario no encontrado"));
        return new CustomUserDetails(persona);
    }

}
