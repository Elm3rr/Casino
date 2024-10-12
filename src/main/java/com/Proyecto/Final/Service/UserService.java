package com.Proyecto.Final.Service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
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

}