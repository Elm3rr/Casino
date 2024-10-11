package com.Proyecto.Final.Service;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.DTO.PersonaRequest;
import com.Proyecto.Final.Repository.PersonaRepository;
import com.Proyecto.Final.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    //Logica de Restauracion y Eliminación(Also vetos)
    public boolean eliminarOVetarUsuario(Long userId, String nuevoEstado) {
        Optional<Usuario> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            Usuario user = optionalUser.get();
            if ("Eliminado".equalsIgnoreCase(nuevoEstado) || "Vetado".equalsIgnoreCase(nuevoEstado)) {
                user.setEstado(nuevoEstado);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public void eliminar_cuenta(Principal principal){
        Optional<Usuario> user = userRepository.findByUsername(principal.getName());

        if(user.isPresent()){
            Usuario usuario = user.get();
            usuario.setEliminadoVetado(true);
            usuario.setEstado("Eliminado");
            userRepository.save(usuario);
        }else{
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    //Logica de Validaciones Específicas para los Usuarios

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
        Persona personaExistente = userRepository.findById(id).get();
        if (personaExistente == null) {
            return false;
        }
        return personaExistente.getCui().equals(cui);
    }

    public boolean DueñoEmail(String email, Long id) {
        Persona personaExistente = userRepository.findById(id).get();

        if (personaExistente == null) {
            return false;
        }
        return personaExistente.getEmail().equals(email);
    }

    public boolean DueñoUsername(String username, Long id) {
        Persona personaExistente = userRepository.findById(id).get();

        if (personaExistente == null) {
            return false;
        }
        return personaExistente.getUsername().equals(username);
    }

    //Logica de las Busquedas 
    public List<Usuario> Lista_Usuarios(String estado) {
        List<String> estadosExcluidos = Arrays.asList("Eliminado", "Vetado");
        return userRepository.findByEstadoAndEstadoNotIn(estado, estadosExcluidos);
    }

    public List<Usuario> Lista_busqueda(String estado, String busqueda){       
        return userRepository.findByEstadoAndUsernameContainingOrEstadoAndCui(estado, busqueda, estado, busqueda);
    }

    public Usuario findByUsername(String username){
        return userRepository.findByUsername(username).get();
    }

    //Logica de Guardado y Actualización de Usuarios(Apostadores)
    public Usuario saveUser(PersonaRequest userRequest){

        Usuario user;
        if(userRequest.getId()==null){
            user = new Usuario();
            user.setSaldo(0);
            user.setEstado("Habilitado");
            user.setEliminadoVetado(false);
            user.setRole("ROLE_USER");
        }else{
            user = userRepository.findById(userRequest.getId()).orElse(new Usuario());
        }
        
        user.setNombre(userRequest.getNombre());
        user.setApellido(userRequest.getApellido());
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setCui(userRequest.getCui());

        if (userRequest.getId() == null || userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        return userRepository.save(user);
    }

    public PersonaRequest ActualData(Persona persona){
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
}