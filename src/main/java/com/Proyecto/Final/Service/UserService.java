package com.Proyecto.Final.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Usuario findByUsername(String username){
        return userRepository.findByUsername(username).get();
    }

    

}