package com.Proyecto.Final.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Usuario;

public interface UserRepository extends JpaRepository <Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

}