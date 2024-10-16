package com.Proyecto.Final.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
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
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Verificar si la persona está vetada o eliminada
        if (persona.isEliminadoVetado()) {
            String motivo = persona.getMotivoEliminacion();
            if (motivo != null && !motivo.isEmpty()) {
                throw new DisabledException("Vetado: " + motivo); 
            } else {
                throw new DisabledException("Eliminaste tu cuenta, ya no puedes acceder."); // Eliminado
            }
        }

        return new CustomUserDetails(persona);
    }

}
