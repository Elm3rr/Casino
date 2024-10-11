package com.Proyecto.Final.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Usuario;

public interface UserRepository extends JpaRepository <Usuario, Long> {
//Manejar la creacion de moderador y admin
    List<Usuario> findByEstadoAndEstadoNotIn(String estado, List<String> estados);

    List<Usuario> findByEstadoAndUsernameContainingOrEstadoAndCui(String estado, String busqueda, String estado2, String busqueda2);

    Optional<Usuario> findByUsername(String username);

}