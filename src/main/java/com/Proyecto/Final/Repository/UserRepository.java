package com.Proyecto.Final.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Usuario;
import com.Proyecto.Final.Entity.Persona;

public interface UserRepository extends JpaRepository <Usuario, Long> {

    Optional<Persona> findByUsername(String username);
    Optional<Persona> findByEmail(String email);
    Optional<Persona> findByCui(String cui);
    Optional<Usuario> findApostadorByUsername(String username);
}
