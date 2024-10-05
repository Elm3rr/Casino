package com.Proyecto.Final.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.DTO.UserRequest;
import com.Proyecto.Final.Entity.Persona;
import com.Proyecto.Final.Repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean username_ocupado(String username){
        Optional<Persona> username_ = userRepository.findByUsername(username);
        return username_.isPresent();
    }

    public boolean email_ocupado(String email){
        Optional<Persona> e_mail = userRepository.findByEmail(email);
        return e_mail.isPresent();
    }

    public boolean CUI_ocupado(String cui){
        Optional<Persona> cui_ = userRepository.findByCui(cui);
        return cui_.isPresent();
    }

    public Usuario saveUser(UserRequest userRequest){

        Usuario user;
        if(userRequest.getId()!=null){
            user = null;
        }else{
            user = new Usuario();
        }
        
        user.setNombre(userRequest.getNombre());
        user.setApellido(userRequest.getApellido());
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setCui(userRequest.getCui());

        user.setSaldo(0);

        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        user.setRole("ROLE_USER");

        return userRepository.save(user);
    }

    public Usuario findByUsername(String username) {
        return userRepository.findApostadorByUsername(username).orElse(null);
    }
}
