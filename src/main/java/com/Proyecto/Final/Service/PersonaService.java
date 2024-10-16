package com.Proyecto.Final.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.DTO.PersonaRequest;
import com.Proyecto.Final.Entity.Administrador;
import com.Proyecto.Final.Entity.Moderador;
import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Repository.PersonaRepository;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Logica para busquedas
    public List<Persona> lista_personas(String role, String estado){
        return personaRepository.findByRoleAndEstadoOrderByFechaCreacion(role, estado, PageRequest.of(0, 10));
    }

    public List<Persona> lista_vetados(String role){
        return personaRepository.findByRoleAndEstadoOrderByFechaActualizacion(role, "Vetado");
    }

    public List<Persona> lista_busqueda_vetados(String rol, String busqueda){
        return personaRepository.filtrarPorUsernameVetador(rol, "Vetado", busqueda, rol, "Vetado", busqueda);
    }

    public List<Persona> lista_busqueda_personas(String role, String estado, String busqueda){
        return personaRepository.findByRoleAndEstadoAndUsernameContainingOrRoleAndEstadoAndCuiContaining(role, estado, busqueda, role, estado, busqueda);
    }

    //Logica de las Validaciones Específicas para la Creación de nuevos Usuarios
    
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


    public boolean DueñoCUI(String cui, Long id) {
        Persona personaExistente = personaRepository.findById(id).get();
        if (personaExistente == null) {
            return false;
        }
        return personaExistente.getCui().equals(cui);
    }

    public boolean DueñoEmail(String email, Long id) {
        Persona personaExistente = personaRepository.findById(id).get();

        if (personaExistente == null) {
            return false;
        }
        return personaExistente.getEmail().equals(email);
    }

    public boolean DueñoUsername(String username, Long id) {
        Persona personaExistente = personaRepository.findById(id).get();

        if (personaExistente == null) {
            return false;
        }
        return personaExistente.getUsername().equals(username);
    }

    //Logica para eliminar cuenta (El mismo usuario borra su cuenta)
    public void eliminar_cuenta(Principal principal){
        Optional<Persona> user = personaRepository.findByUsername(principal.getName());
        
        if(user.isPresent()){
            Persona usuario = user.get();
            usuario.setEliminadoVetado(true);
            usuario.setEstado("Eliminado");
            personaRepository.save(usuario);
        }else{
            throw new RuntimeException("Error...");
        }
    }

    //Logica para vetar usuarios
    public void vetar_cuenta(long id, String motivo, Principal principal){
        Persona verdugo = personaRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Persona persona = personaRepository.findById(id).get();
        persona.setEstado("Vetado");
        persona.setMotivoEliminacion(motivo);
        persona.setVetadoPor(verdugo);
        persona.setEliminadoVetado(true);
        personaRepository.save(persona);
    }

    //Logica para restaurar usuarios
    public void restaurar_usuario(long id){
        Persona persona = personaRepository.findById(id).get();
        persona.setEstado("Habilitado");
        persona.setEliminadoVetado(false);
        persona.setMotivoEliminacion(null);
        persona.setVetadoPor(null);
        personaRepository.save(persona); 
    }

    //Logica para crear y actualizar (USUARIO, MOD Y ADMINISTRADOR)
    public PersonaRequest ActualData(String username){
        Persona persona = personaRepository.findByUsername(username).get();
        PersonaRequest user = new PersonaRequest();
        user.setId(persona.getId());
        user.setNombre(persona.getNombre());
        user.setApellido(persona.getApellido());
        user.setEmail(persona.getEmail());
        user.setConfirmEmail(persona.getEmail());
        user.setUsername(persona.getUsername());
        user.setCui(persona.getCui());
        return user;
    }

    public Persona newUser(PersonaRequest personaRequest, String ROL, Principal principal) {
        Persona persona;
    
        // Solo busca el creador si el Principal no es null
        Persona creador = null;
        if (principal != null) {
            creador = personaRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        }
    
        if (personaRequest.getId() == null) {
            switch (ROL) {
                case "ROLE_MOD":
                    persona = new Moderador();
                    persona.setRole(ROL);
                    if (creador != null) { // Solo establece el creador si fue encontrado
                        ((Moderador) persona).setModeradorcreadoPor(creador);
                    }
                    break;
                case "ROLE_ADMIN":
                    persona = new Administrador();
                    persona.setRole(ROL);
                    break;
                case "ROLE_USER":
                default:
                    persona = new Usuario();
                    persona.setRole(ROL);
                    ((Usuario) persona).setSaldo(0.0);
                    break;
            }
        } else {
            persona = personaRepository.findById(personaRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        }
    
        persona.setNombre(personaRequest.getNombre());
        persona.setApellido(personaRequest.getApellido());
        persona.setEmail(personaRequest.getEmail());
        persona.setEstado("Habilitado");
        persona.setEliminadoVetado(false);
        persona.setUsername(personaRequest.getUsername());
        persona.setCui(personaRequest.getCui());
    
        if (personaRequest.getId() == null || personaRequest.getPassword() != null && !personaRequest.getPassword().isEmpty()) {
            persona.setPassword(passwordEncoder.encode(personaRequest.getPassword()));
        }
    
        return personaRepository.save(persona);
    }
    


}
