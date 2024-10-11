package com.Proyecto.Final.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.Proyecto.Final.Entity.Moderador;

public interface ModeradorRepository extends JpaRepository<Moderador, Long> {
    
    Optional<Moderador> findByUsername(String username);

    List<Moderador> findByEstadoOrderByFechaCreacion(String estado, Pageable pageable);

    List<Moderador> findByEstadoAndUsernameContainingOrEstadoAndApellidoContaining(String estado, String busqueda, String estado2, String busqueda2);

}
